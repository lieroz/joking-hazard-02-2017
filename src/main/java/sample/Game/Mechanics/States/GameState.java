package sample.Game.Mechanics.States;

import com.sun.xml.internal.ws.resources.ServerMessages;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.SystemMessages.MessageContainer;
import sample.Game.Services.ServerManager;

import java.util.Map;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public abstract class GameState {
    public enum ErrorCodes{
        OK,
        INVALID_COMMAND,
        FINISHED
    }
    protected MainMechanics.GameContext context;
    abstract public ErrorCodes handle(MessageContainer msg);
    abstract public ErrorCodes transfer();
    public void notifyAll(BaseServerMessage msg){

    }
}
