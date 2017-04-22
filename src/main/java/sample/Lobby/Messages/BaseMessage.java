package sample.Lobby.Messages;

import sample.Lobby.Services.LobbyService;
import sample.Lobby.Services.MyBeanConfig;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

/**
 * Created by ksg on 12.04.17.
 */
public class BaseMessage {
    @JsonIgnore
    ObjectMapper mapper; //Make it static? or make fabric?
    public BaseMessage (ObjectMapper mapper){
        this.mapper = mapper;
    }
    public String getType(){
        return "NoType";
    }
    @JsonIgnore
    public String getJson(){
        String res = null;
        try{
            res = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e){
        }
        return res;
    }

}
