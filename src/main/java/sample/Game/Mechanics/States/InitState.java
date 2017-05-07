package sample.Game.Mechanics.States;

import org.springframework.web.socket.WebSocketSession;
import sample.Game.Mechanics.GameUser.GameUser;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.SystemMessages.MessageContainer;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Messages.SystemMessages.UserConnectedMessage;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class InitState implements GameState{
    private final MainMechanics.GameContext context;
    private int num_connected;

    public InitState(MainMechanics.GameContext context){
        this.context = context;
        num_connected = 0;
    }

    private ErrorCodes addUser(MessageContainer msg){
        String userId = msg.getUserId();
        GameUserItem item = context.mp.get(userId);
        Class cls = msg.getMsg().getClassOfMessage();
        UserConnectedMessage conMessage = (UserConnectedMessage) cls.cast(msg.getMsg());
        WebSocketSession session = conMessage.getSession();
        GameUser user = new GameUser(session);
        item.setStrategy(user);
        num_connected++;
        if(num_connected == context.numberOfPlayers){
            context.state = new FinishState(context);
            return ErrorCodes.FINISHED;
        }
        return ErrorCodes.OK;
    }
    @Override
    public ErrorCodes handle(MessageContainer msg) {
        String type = msg.getMsg().getType();
        switch (type){
            case "UserConnected":{
                return addUser(msg);
            }
            default:
                break;
        }
        return ErrorCodes.INVALID_COMMAND;
    }
}
