package sample.Lobby.Controllers;


import com.fasterxml.jackson.databind.deser.Deserializers;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import sample.Lobby.Messages.BaseMessage;
import sample.Lobby.Models.LobbyUserModel;
import sample.Main.Models.UserInfoModel;
import sample.Main.Views.UserDataView;
import sample.Main.Views.UserInfo;

import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * Created by ksg on 13.04.17.
 */
public class LobbyUserController {

    public enum ErrorCodes{
        OK,
        INVALID_SESSION,
        INVALID_LOGIN,
        DATABASE_ERROR,
        SERVER_ERROR,
        NOT_INITIALIZED,
        ERROR_SEND_MESSAGE,
        ERROR_SERIALIZATION,
    }

    LobbyUserModel model;

    public   LobbyUserController(){
    }

    public ErrorCodes lobbyUserControllerInit(WebSocketSession session){
        final String userId = (String) session.getAttributes().get("userId");
        if (userId == null) {
            return ErrorCodes.INVALID_SESSION;
        }
        model = new LobbyUserModel();
        LobbyUserModel.ErrorCodes result = model.lobbyUserModelInit(userId, session);
        switch (result) {
            case OK: {
                break;
            }
            case INVALID_LOGIN: {
                return ErrorCodes.INVALID_LOGIN;
            }
            case DATABASE_ERROR: {
                return ErrorCodes.DATABASE_ERROR;
            }
            default: {
                return ErrorCodes.SERVER_ERROR;
            }
        }
        return ErrorCodes.OK;
    }

    public String getUserId(){
        if(model == null){
            return null;
        }
        return  model.getUserId();
    }

    public ErrorCodes sendMessageToUser(String msg){
        if(model == null){
            return ErrorCodes.NOT_INITIALIZED;
        }
        WebSocketSession session = model.getSession();
        if(session == null){
            return ErrorCodes.SERVER_ERROR;
        }
        try {
            session.sendMessage(new TextMessage(msg));
        } catch (IOException e){
            return ErrorCodes.ERROR_SEND_MESSAGE;
        }
        return ErrorCodes.OK;
    }

    public ErrorCodes sendMessageToUser(BaseMessage msg){
        String result;
        result = msg.getJson();
        if(result == null){
            return ErrorCodes.ERROR_SERIALIZATION;
        }
        return sendMessageToUser(result);
    }

    public UserInfo getUserDataView(){
        if(model == null){
            return null;
        }
        UserInfoModel info = model.getUserInfo();
        return  info.getUserInfo();
    }

    public void close(){
        WebSocketSession ses = model.getSession();
        try {
            ses.close();
        } catch (IOException e){

        }
        model = null;
    }
}
