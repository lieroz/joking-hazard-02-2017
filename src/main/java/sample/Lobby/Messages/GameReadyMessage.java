package sample.Lobby.Messages;

/**
 * Created by ksg on 12.04.17.
 */
public class GameReadyMessage extends BaseMessage {
    @Override
    public String getType(){
        return "GameReadyMessage";
    }
}
