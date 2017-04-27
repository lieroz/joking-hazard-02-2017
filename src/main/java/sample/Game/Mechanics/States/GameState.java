package sample.Game.Mechanics.States;

import sample.Game.Messages.SystemMessages.MessageContainer;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public interface GameState {
    enum ErrorCodes{
        OK,
        INVALID_COMMAND,
        FINISHED
    }
    ErrorCodes handle(MessageContainer msg);
}
