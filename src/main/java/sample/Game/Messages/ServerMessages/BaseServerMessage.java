package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ksg on 26.04.17.
 */
public class BaseServerMessage {
    @JsonIgnore
    ObjectMapper mapper; //Make it static? or make fabric?
    public BaseServerMessage (ObjectMapper mapper){
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
