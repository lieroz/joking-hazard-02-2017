package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Messages.SystemMessages.BaseSystemMessage;

/**
 * Created by ksg on 27.04.17.
 */
public class ServerFinishedMessage extends BaseServerMessage{
    public ServerFinishedMessage(ObjectMapper mapper){
        super(mapper);
    }
    public String getType(){
        return "Game Finished Message";
    }
}
