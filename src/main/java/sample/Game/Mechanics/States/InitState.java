package sample.Game.Mechanics.States;

import org.springframework.web.socket.WebSocketSession;
import sample.Game.Mechanics.GameUser.GameUser;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.SystemMessages.UserConnectedMessage;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class InitState extends GameState {
    private final MainMechanics.GameContext context;
    @SuppressWarnings("InstanceVariableNamingConvention")
    private int num_connected;

    public InitState(MainMechanics.GameContext context) {
        this.context = context;
        num_connected = 0;
    }

    @Override
    protected ErrorCodes addUser(BaseMessageContainer msg) {
        final String userId = msg.getUserId();
        final GameUserItem item = context.mp.get(userId);
        final Class cls = msg.getMsg(context.mapper).getClassOfMessage();
        final UserConnectedMessage conMessage = (UserConnectedMessage) cls.cast(msg.getMsg(context.mapper));
        final WebSocketSession session = conMessage.getSession();
        final GameUser user = new GameUser(session);
        item.setStrategy(user);
        num_connected++;
        if (num_connected == context.numberOfPlayers) {
            final GameState state = new RoundBeginState(context);
            return state.transfer();
        }
        return ErrorCodes.OK;
    }


    @Override
    public ErrorCodes handle(BaseMessageContainer msg) {
        final String type = msg.getMsg(context.mapper).getType();
        switch (type) {
            case "UserConnected": {
                return addUser(msg);
            }
            default:
                break;
        }
        return ErrorCodes.INVALID_COMMAND;
    }

    @Override
    public ErrorCodes transfer() {
        context.state = this;
        return ErrorCodes.OK;
    }
}
