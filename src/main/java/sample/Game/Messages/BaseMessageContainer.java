package sample.Game.Messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Services.ServerManager;

/**
 * Created by ksg on 16.05.17.
 */
public abstract  class BaseMessageContainer {
    protected String userId;
    protected  ServerManager.GameIndex index;
    public BaseMessageContainer(String userId, ServerManager.GameIndex index){
        this.userId = userId;
        this.index = index;
    }
    public String getUserId(){
        return userId;
    }
    public ServerManager.GameIndex getIndex(){
        return index;
    }
    abstract public BaseGameMessage getMsg(ObjectMapper mapper);
}
