package sample.Game.Mechanics.States;

import org.springframework.web.socket.WebSocketSession;
import sample.Game.Mechanics.GameUser.GameUser;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.ServerMessages.GameUserInfo;
import sample.Game.Messages.ServerMessages.RoundInfo;
import sample.Game.Messages.ServerMessages.TableInfo;
import sample.Game.Messages.SystemMessages.UserConnectedMessage;
import sample.Game.Services.ServerManager;

import java.util.Map;
import java.util.Vector;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public abstract class GameState {
    public enum ErrorCodes{
        OK,
        INVALID_COMMAND,
        FINISHED,
        SERIALIZATION_ERROR,
        OUT_OF_RANGE,
        BAD_BEHAVIOR,
        SERVER_ERROR
    }
    protected void notifyUser(GameUserItem item){
        TableInfo tableInfoMsg = new TableInfo(context.mapper, context.cards);
        item.sendMessage(tableInfoMsg);
        Vector<GameUserInfo> resvect = new Vector<>();
        for(Map.Entry<String, GameUserItem> entry : context.mp.entrySet()){
            String userId = entry.getKey();
            GameUserItem user = entry.getValue();
            resvect.add(new GameUserInfo(context.mapper, userId, (context.master == user), user.getScore()));
        }
        RoundInfo msg = new RoundInfo(context.mapper,resvect,context.currentRound);
        item.sendMessage(msg);
    }
    protected ErrorCodes addUser(BaseMessageContainer msg){
        String userId = msg.getUserId();
        GameUserItem item = context.mp.get(userId);
        Class cls = msg.getMsg(context.mapper).getClassOfMessage();
        UserConnectedMessage conMessage = (UserConnectedMessage) cls.cast(msg.getMsg(context.mapper));
        WebSocketSession session = conMessage.getSession();
        GameUser user = new GameUser(session);
        item.setStrategy(user);
        notifyUser(item);
        return ErrorCodes.OK;
    }
    protected MainMechanics.GameContext context;
    abstract public ErrorCodes handle(BaseMessageContainer msg);
    abstract public ErrorCodes transfer();
    public void notifyAll(BaseServerMessage msg){

    }
}
