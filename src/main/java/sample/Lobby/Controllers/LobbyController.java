package sample.Lobby.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import sample.Lobby.Messages.GameReadyMessage;
import sample.Lobby.Messages.UserAddedMessage;
import sample.Lobby.Messages.BaseMessage;
import sample.Main.Controllers.UserController;
import sample.Main.Models.UserInfoModel;
import sample.Lobby.Messages.UserExitedMessage;

import sun.font.TrueTypeFont;

import java.io.IOException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ksg on 11.04.17.
 */
public class LobbyController {
    public enum  ErrorCodes {
        OK,
        SERVER_ERROR,
        INVALID_LOGIN,
        LOGIN_EXIST,
        LOBBY_IS_FOOL,
        SEND_MESSAGE_ERROR,
        INVALID_USER,
        INVALID_USER_IN_LOBBY,
        ERROR_SERIALIZATION
    };

    static final int DEFAULT_MAX_NUMBER = 4;
    int maxPlayers;

    Map<String, LobbyUserController> users;

    public LobbyController(int maxPlayers){
        this.maxPlayers = maxPlayers;
    }

    public LobbyController(){
        this.users = new ConcurrentHashMap<String, LobbyUserController>();
        this.maxPlayers = DEFAULT_MAX_NUMBER;
    }

    public synchronized ErrorCodes addUser(@NotNull  LobbyUserController user) {
        String userId = user.getUserId();
        if(userId == null){
            return ErrorCodes.INVALID_USER;
        }
        users.put(userId, user);
        if(users.size() == maxPlayers){
            return ErrorCodes.LOBBY_IS_FOOL;
        }
        if(users.size() > maxPlayers){
            return  ErrorCodes.SERVER_ERROR;
        }
        return sendMessageAll(new UserAddedMessage(user.getUserDataView()));
    }


    public ErrorCodes removeUser(@NotNull  String userId){
        if(! users.containsKey(userId)){
            return ErrorCodes.INVALID_LOGIN;
        }
        LobbyUserController user = users.get(userId);
        users.remove(userId);
        user.close();
        return sendMessageAll(new UserExitedMessage(user.getUserDataView()));
    }

    public boolean isFool(){
        return (maxPlayers == users.size());
    }

    public ErrorCodes closeConnections(){
        for(Map.Entry<String,LobbyUserController> entry: users.entrySet()) {
            LobbyUserController user = entry.getValue();
            user.close();
        }
        return ErrorCodes.OK;
    }

    public ErrorCodes sendMessageAll(String msg){
        boolean ok = true;
        for(Map.Entry<String,LobbyUserController> entry: users.entrySet()){
            LobbyUserController user = entry.getValue();
            LobbyUserController.ErrorCodes err = user.sendMessageToUser(msg);
            switch ( err){
                case OK:{
                    break;
                }
                case NOT_INITIALIZED:{
                    return ErrorCodes.INVALID_USER_IN_LOBBY;
                }
                case ERROR_SEND_MESSAGE: {
                    ok = false;
                    break;
                }
                default: {
                    return ErrorCodes.SERVER_ERROR;
                }
            }
        }
        if(!ok){
            return ErrorCodes.SEND_MESSAGE_ERROR;
        }
        return ErrorCodes.OK;
    }

    public ErrorCodes sendMessageAll(BaseMessage msg){
        String result;
        result = msg.getJson();
        if(result == null){
            return ErrorCodes.ERROR_SERIALIZATION;
        }
        return  sendMessageAll(result);
    }

    public ErrorCodes gameStart(){
        return  sendMessageAll(new GameReadyMessage());
    }

}