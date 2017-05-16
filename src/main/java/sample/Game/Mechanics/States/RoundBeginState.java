package sample.Game.Mechanics.States;

import org.springframework.web.socket.WebSocketSession;
import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Mechanics.GameUser.GameUser;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.ServerMessages.*;
import sample.Game.Messages.SystemMessages.MessageContainer;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.SystemMessages.UserConnectedMessage;
import sample.Game.Messages.UserMessages.ChooseCardFromHand;
import sample.Main.Views.UserInfo;

import java.util.Map;
import java.util.Vector;

/**
 * Created by ksg on 14.05.17.
 */
public class RoundBeginState extends GameState {
    private final MainMechanics.GameContext context;


    public RoundBeginState(MainMechanics.GameContext context){
        this.context = context;
    }
    private void prepareTable(GameUserItem it){
        Vector<GameUserInfo> resvect = new Vector<>();
        for(Map.Entry<String, GameUserItem> entry : context.mp.entrySet()){
            String userId = entry.getKey();
            GameUserItem user = entry.getValue();
            resvect.add(new GameUserInfo(context.mapper, userId, (it == user), user.getScore()));
        }
        RoundInfo msg = new RoundInfo(context.mapper,resvect,context.currentRound);
        for(Map.Entry<String, GameUserItem> entry : context.mp.entrySet()){
            GameUserItem user = entry.getValue();
            user.sendMessage(msg);
        }
    }

    private void prepateCards(){
        GameCard card = context.deck.popCard();
        if (!card.getRed()) {
            context.cards[1] = card;
        } else {
            context.cards[2] = card;
        }

        TableInfo tableInfoMsg = new TableInfo(context.mapper, context.cards);
        for(Map.Entry<String, GameUserItem> entry : context.mp.entrySet()){
            GameUserItem user = entry.getValue();
            user.sendHand();
            user.sendMessage(tableInfoMsg);
        }
    }

    private void addMasterCard(GameCard card){
        if (context.cards[1] == null){
            context.cards[2] = card;
        } else {
            context.cards[1] = card;
        }
    }


    private GameUserItem getMaster(){
        GameUserItem it;
        it = context.masterQeue.poll();
        while ((it!=null) && (!it.isUser())){
            it = context.masterQeue.poll();
        }
        return it;
    }


    private ErrorCodes chooseCardFromHand(BaseMessageContainer msg){
        String userId = msg.getUserId();
        GameUserItem item = context.mp.get(userId);
        if(item != context.master){
            return ErrorCodes.BAD_BEHAVIOR;
        }
        Class cls = msg.getMsg(context.mapper).getClassOfMessage();
        ChooseCardFromHand conMessage = (ChooseCardFromHand) cls.cast(msg.getMsg(context.mapper));
        //TODO: Logic
        int index = conMessage.getChosenCard();
        GameCard card = context.master.getCardFromHandByIndex(index);
        if(card == null){
            return ErrorCodes.OUT_OF_RANGE;
        }
        addMasterCard(card);
        RoundState state = new RoundState(context);
        return state.transfer();
    }

    @Override
    public ErrorCodes handle(BaseMessageContainer msg) {
        BaseGameMessage ser_msg =msg.getMsg(context.mapper);
        if(ser_msg == null){
            return ErrorCodes.SERIALIZATION_ERROR;
        }
        String type = ser_msg.getType();
        switch (type) {
            case "UserConnected": {
                return addUser(msg);
            }
            case "ChooseCardFromHand": {
                return chooseCardFromHand(msg);
            }
            default:
                break;
        }
        return ErrorCodes.INVALID_COMMAND;
    }

    public ErrorCodes transfer(){
        context.reset();
        context.currentRound++;
        for(Map.Entry<String, GameUserItem> entry : context.mp.entrySet()){
            GameUserItem user = entry.getValue();
            if (!user.reget(context.deck)){
                return ErrorCodes.SERVER_ERROR;
            }
        }
        context.master = getMaster();
        if (context.master == null){
            FinishState state = new FinishState(context);
            return state.transfer();
        }
        prepareTable(context.master);
        prepateCards();
        context.master.getCardFromHand();
        this.context.state = this;
        //this.notifyAll(msg);
        return ErrorCodes.OK;
    }
}