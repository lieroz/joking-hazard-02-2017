package sample.Game.Messages.UserMessages;

import com.fasterxml.jackson.annotation.JsonCreator;
import sample.Game.Messages.BaseGameMessage;

/**
 * Created by ksg on 02.06.17.
 */
public class UserExit extends BaseUserMessage {
    @JsonCreator
    public UserExit(){}
    @Override
    public String getType() {
        return "UserExited";
    }
}
