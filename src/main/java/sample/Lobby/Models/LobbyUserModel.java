package sample.Lobby.Models;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketMessage;

import org.springframework.web.socket.WebSocketSession;
import sample.Lobby.Controllers.LobbyController;
import sample.Lobby.Controllers.LobbyUserController;
import sample.Lobby.Views.LobbyView;
import sample.Main.Models.UserInfoModel;
import sample.Main.Services.AccountService;
import sample.Main.Views.UserInfo;

/**
 * Created by ksg on 13.04.17.
 */
public class LobbyUserModel {
    public enum ErrorCodes{
        OK,
        INVALID_LOGIN,
        DATABASE_ERROR,
        SERVER_ERROR
    }
    WebSocketSession session;
    UserInfoModel userInfo;
    AccountService accountService;

    public LobbyUserModel(WebSocketSession session, AccountService accountService){
        this.accountService = accountService;
        this.session = session;
        this.userInfo = null;
    }

    public ErrorCodes lobbyUserModelInit(String userId){
        final UserInfoModel userInfoModel = new UserInfoModel(null, null);
        final AccountService.ErrorCodes resp = accountService.getUserData(userId, userInfoModel);
        switch (resp) {
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
        this.userInfo = userInfoModel;
        return ErrorCodes.OK;
    }

    public String getUserId(){
        return userInfo.getUserLogin();
    }

    public WebSocketSession getSession(){
        return session;
    }

    public UserInfoModel getUserInfo(){
        return userInfo;
    }
}
