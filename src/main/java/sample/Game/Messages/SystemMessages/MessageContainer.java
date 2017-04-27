package sample.Game.Messages.SystemMessages;

import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Services.ServerManager;

/**
 * Created by ksg on 26.04.17.
 */
public class MessageContainer {
    String userId;
    ServerManager.GameIndex index;
    BaseSystemMessage msg;
    public MessageContainer(String userId, ServerManager.GameIndex index, BaseSystemMessage msg){
        this.userId = userId;
        this.index = index;
        this.msg = msg;
    }
    public String getUserId(){
        return userId;
    }
    public ServerManager.GameIndex getIndex(){
        return index;
    }
    public BaseSystemMessage getMsg(){
        return msg;
    }
}
