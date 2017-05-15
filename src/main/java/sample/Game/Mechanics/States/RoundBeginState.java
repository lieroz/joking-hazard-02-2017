package sample.Game.Mechanics.States;

import org.springframework.web.socket.WebSocketSession;
import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Mechanics.GameUser.GameUser;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.ServerMessages.GameUserInfo;
import sample.Game.Messages.ServerMessages.GetCardFromHand;
import sample.Game.Messages.ServerMessages.RoundInfo;
import sample.Game.Messages.ServerMessages.TableInfo;
import sample.Game.Messages.SystemMessages.MessageContainer;
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
            user.sendMessage(tableInfoMsg);
        }
    }

    private ErrorCodes addUser(MessageContainer msg){
        String userId = msg.getUserId();
        GameUserItem item = context.mp.get(userId);
        Class cls = msg.getMsg().getClassOfMessage();
        UserConnectedMessage conMessage = (UserConnectedMessage) cls.cast(msg.getMsg());
        WebSocketSession session = conMessage.getSession();
        GameUser user = new GameUser(session);
        item.setStrategy(user);
        return ErrorCodes.OK;
    }

    private GameUserItem getMaster(){
        GameUserItem it;
        it = context.masterQeue.poll();
        while ((it!=null) && (!it.isUser())){
            it = context.masterQeue.poll();
        }
        return it;
    }

    private ErrorCodes chooseCardFromHand(MessageContainer msg){
        String userId = msg.getUserId();
        GameUserItem item = context.mp.get(userId);
        Class cls = msg.getMsg().getClassOfMessage();
        ChooseCardFromHand conMessage = (ChooseCardFromHand) cls.cast(msg.getMsg());
        //TODO: Logic
        return ErrorCodes.OK;
    }

    @Override
    public ErrorCodes handle(MessageContainer msg) {
        String type = msg.getMsg().getType();
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
        GameUserItem it = getMaster();
        if (it == null){
            FinishState state = new FinishState(context);
            state.transfer();
            return ErrorCodes.OK;
        }
        prepareTable(it);
        prepateCards();
        for(Map.Entry<String, GameUserItem> entry : context.mp.entrySet()){
            GameUserItem user = entry.getValue();
            if (it == user) {
                user.sendMessage(new GetCardFromHand(context.mapper));
            }
        }
        this.context.state = this;
        //this.notifyAll(msg);
        return ErrorCodes.OK;
    }
}