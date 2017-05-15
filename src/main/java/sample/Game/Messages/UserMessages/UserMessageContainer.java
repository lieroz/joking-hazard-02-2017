package sample.Game.Messages.UserMessages;

import sample.Game.Messages.BaseGameMessage;
import sample.Lobby.Messages.BaseMessage;

/**
 * Created by ksg on 15.05.17.
 */
public class UserMessageContainer extends BaseGameMessage {
    String msg;
    public UserMessageContainer(String msg){
        this.msg = msg;
    }
    public String getType(){
        return "UserMessageContainer";
    }

    public String getMsg(){
        return msg;
    }
}
