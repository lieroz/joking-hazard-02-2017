package sample.Game.Messages.SystemMessages;

import sample.Game.Services.ServerManager;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MessageContainer {
    private final String userId;
    private final ServerManager.GameIndex index;
    private final BaseSystemMessage msg;
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
