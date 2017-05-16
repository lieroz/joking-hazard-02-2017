package sample.Game.Messages.UserMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.SystemMessages.MessageContainer;
import sample.Game.Services.ServerManager;
import sample.Lobby.Messages.BaseMessage;

import java.io.IOException;

/**
 * Created by ksg on 15.05.17.
 */
public class UserMessageContainer extends BaseMessageContainer {
    String msg;
    BaseGameMessage res;
    public UserMessageContainer(String userId, ServerManager.GameIndex index,String msg){
        super(userId, index);
        this.msg = msg;
        this.res = null;
    }
    public String getType(){
        return "UserMessageContainer";
    }

    public BaseGameMessage getMsg(ObjectMapper mapper){
        try {
            if(res == null) {
                res = (BaseGameMessage) mapper.readValue(this.msg, BaseUserMessage.class);
            }
        } catch (IOException e){
            res = null;
        }
        return res;
    }
}
