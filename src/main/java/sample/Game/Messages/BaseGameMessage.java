package sample.Game.Messages;

import sample.Game.Messages.SystemMessages.BaseSystemMessage;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class BaseGameMessage {
    @SuppressWarnings("unused")
    public String getType(){
        return "BaseGameMessage";
    }
    public Class getClassOfMessage(){
        return BaseGameMessage.class;
    }
}
