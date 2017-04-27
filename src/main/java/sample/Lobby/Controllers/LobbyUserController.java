package sample.Lobby.Controllers;


import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sample.Lobby.Messages.BaseMessage;
import sample.Lobby.Models.LobbyUserModel;
import sample.Lobby.Views.UserGameView;
import sample.Main.Models.UserInfoModel;
import sample.Main.Services.AccountService;
import sample.Main.Views.UserInfo;

import java.io.IOException;


/**
 * Created by ksg on 13.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
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

    private LobbyUserModel model;

    public   LobbyUserController(){
    }

    public ErrorCodes lobbyUserControllerInit(WebSocketSession session, AccountService accountService){
        final String userId = (String) session.getAttributes().get("userLogin");
        model = new LobbyUserModel(session, accountService);
        if (userId == null) {
            return ErrorCodes.INVALID_SESSION;
        }
        LobbyUserModel.ErrorCodes result = model.lobbyUserModelInit(userId);
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

    @SuppressWarnings("UnusedReturnValue")
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

    public UserGameView getGameView(){
        if(model == null){
            return null;
        }

        return model.getGameView();
    }
    public void close(){
        WebSocketSession ses;
        if(model != null) {
            ses = model.getSession();
            try {
                ses.close();
            } catch (IOException ignored){

            }
        }
    }
}
