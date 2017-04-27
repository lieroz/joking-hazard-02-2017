package sample.Game.Mechanics.States;

import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.SystemMessages.MessageContainer;

/**
 * Created by ksg on 26.04.17.
 */
public interface GameState {
    enum ErrorCodes{
        OK,
        INVALID_COMMAND,
        FINISHED
    }
    public ErrorCodes handle(MessageContainer msg);
}
