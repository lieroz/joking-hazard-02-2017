package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 02.03.17.
 */
public final class ResponseCode {
    final boolean result;
    final String errorMsg;

    @SuppressWarnings("unused")
    @JsonCreator
    public ResponseCode(@JsonProperty("result") boolean result,
                        @JsonProperty("errorMsg") String errorMsg) {
        this.result = result;
        this.errorMsg = errorMsg;
    }

    @SuppressWarnings("unused")
    public boolean getResult() {
        return result;
    }

    @SuppressWarnings("unused")
    public String getErrorMsg() {
        return errorMsg;
    }
}