package sample.Lobby.Messages;

/**
 * Created by ksg on 12.04.17.
 */
public class ErrorMessage extends  BaseMessage {

        String errorMsg;

        public ErrorMessage(String errorMsg){
            this.errorMsg = errorMsg;
        }

        @Override
        public String getType(){
            return "ErrorMessage";
        }
        public String getErrorMsg(){
            return errorMsg;
        }

    }