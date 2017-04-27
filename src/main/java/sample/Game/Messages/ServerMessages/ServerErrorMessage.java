package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ksg on 26.04.17.
 */
public class ServerErrorMessage extends BaseServerMessage {
    String msg;
    public ServerErrorMessage(ObjectMapper mapper, String msg){
        super(mapper);
        this.msg = msg;
    }
    public String getType(){
        return "ErrorMsg";
    }
    public String getMsg(){
        return msg;
    }
}
