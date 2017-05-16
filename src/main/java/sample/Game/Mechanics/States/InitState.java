package sample.Game.Mechanics.States;

import org.springframework.web.socket.WebSocketSession;
import sample.Game.Mechanics.GameUser.GameUser;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Messages.SystemMessages.UserConnectedMessage;

import java.util.Map;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class InitState extends GameState{
    private final MainMechanics.GameContext context;
    private int num_connected;

    public InitState(MainMechanics.GameContext context){
        this.context = context;
        num_connected = 0;
    }

    @Override
    protected ErrorCodes addUser(BaseMessageContainer msg){
        String userId = msg.getUserId();
        GameUserItem item = context.mp.get(userId);
        Class cls = msg.getMsg(context.mapper).getClassOfMessage();
        UserConnectedMessage conMessage = (UserConnectedMessage) cls.cast(msg.getMsg(context.mapper));
        WebSocketSession session = conMessage.getSession();
        GameUser user = new GameUser(session);
        item.setStrategy(user);
        num_connected++;
        if(num_connected == context.numberOfPlayers){
            GameState state = new RoundBeginState(context);
            return state.transfer();
        }
        return ErrorCodes.OK;
    }


    @Override
    public ErrorCodes handle(BaseMessageContainer msg) {
        String type = msg.getMsg(context.mapper).getType();
        switch (type){
            case "UserConnected":{
                return addUser(msg);
            }
            default:
                break;
        }
        return ErrorCodes.INVALID_COMMAND;
    }

    public ErrorCodes transfer(){
        context.state = this;
        return ErrorCodes.OK;
    }
}
