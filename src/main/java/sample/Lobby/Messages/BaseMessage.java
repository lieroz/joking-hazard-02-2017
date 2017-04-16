package sample.Lobby.Messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * Created by ksg on 12.04.17.
 */
public class BaseMessage {
    @Autowired
    @JsonIgnore
    ObjectMapper mapper;
    public String getType(){
        return "NoType";
    }
    public String getJson(){
        String res = null;
        try{
            res = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e){
        }
        return res;
    }

}
