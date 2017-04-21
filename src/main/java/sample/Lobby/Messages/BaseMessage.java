package sample.Lobby.Messages;

import sample.Lobby.Services.LobbyService;
import sample.Lobby.Services.MyBeanConfig;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * Created by ksg on 12.04.17.
 */
public class BaseMessage {
    @JsonIgnore
    @Autowired
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
