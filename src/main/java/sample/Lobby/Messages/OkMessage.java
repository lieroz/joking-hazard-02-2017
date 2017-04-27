package sample.Lobby.Messages;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ksg on 24.04.17.
 */
public class OkMessage extends BaseMessage {
    String okMsg;

    public OkMessage(String okMsg, ObjectMapper mapper){
        super(mapper);
        this.okMsg = okMsg;
    }

    @Override
    public String getType(){
        return "OkMessage";
    }
    public String getOkMsg(){
        return okMsg;
    }

}
