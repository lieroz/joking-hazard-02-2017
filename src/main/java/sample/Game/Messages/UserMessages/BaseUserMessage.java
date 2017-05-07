package sample.Game.Messages.UserMessages;

import sample.Game.Messages.BaseGameMessage;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings({"unused", "DefaultFileTemplate"})
public class BaseUserMessage extends BaseGameMessage {
    @Override
    public String getType() {
        return "BaseUserMessage";
    }
}
