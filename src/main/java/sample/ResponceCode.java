package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 02.03.17.
 */
@SuppressWarnings("unused")
public final class ResponceCode{
    final boolean result;
    final String errorMsg;
    @JsonCreator
    public ResponceCode(@JsonProperty("result") boolean result,
                        @JsonProperty("errorMsg") String errorMsg){
        this.result = result;
        this.errorMsg = errorMsg;
    }
    public boolean getResult(){
        return result;
    }
    public String getErrorMsg(){
        return errorMsg;
    }
}