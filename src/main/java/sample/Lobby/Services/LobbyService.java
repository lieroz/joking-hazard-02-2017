package sample.Lobby.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import sample.Lobby.Controllers.LobbyController;
import sample.Lobby.Controllers.LobbyUserController;
import sample.Lobby.Messages.ErrorMessage;

/**
 * Created by ksg on 11.04.17.
 */

@Service
public class LobbyService {
    enum  ErrorCodes{
        DATABASE_ERROR,
        INVALID_LOGIN,
        SERVER_RESETED,
        INVALID_SESSION,
        SERVER_ERROR,
        OK,
    }

    LobbyController currentLobby;

    void resetLobby(){
        currentLobby.closeConnections();
        currentLobby = new LobbyController();
    }
    void createLobby(){
        currentLobby = new LobbyController();
    }


    public LobbyService() {

    }

    public ErrorCodes addUser(WebSocketSession userSession){
        LobbyUserController user = new LobbyUserController();
        LobbyUserController.ErrorCodes initErr = user.lobbyUserControllerInit(userSession);
        switch (initErr){
            case OK:{
                break;
            }
            case INVALID_LOGIN:{
                user.sendMessageToUser(new ErrorMessage("Invalid Login"));
                user.close();
                return ErrorCodes.INVALID_LOGIN;
            }
            case DATABASE_ERROR:{
                user.sendMessageToUser(new ErrorMessage("Database Error"));
                user.close();
                return  ErrorCodes.DATABASE_ERROR;
            }
            case INVALID_SESSION:{
                user.sendMessageToUser(new ErrorMessage("Invalid Session"));
                user.close();
                return ErrorCodes.INVALID_SESSION;
            }
            case SERVER_ERROR:{
                user.sendMessageToUser(new ErrorMessage("Server Error"));
                user.close();
                return ErrorCodes.SERVER_ERROR;
            }
        }

        LobbyController.ErrorCodes err = currentLobby.addUser(user);
        switch (err){
            case LOBBY_IS_FOOL: {
                //TODO: StartGame, retry to add new lobby
                currentLobby.gameStart();
                resetLobby();
                break;
            }
            case INVALID_LOGIN: {
                user.sendMessageToUser(new ErrorMessage("Invalid Login"));
                user.close();
                return ErrorCodes.INVALID_LOGIN;
            }
            case SERVER_ERROR: {
                user.sendMessageToUser(new ErrorMessage("Server Error"));
                user.close();
                resetLobby();
                return ErrorCodes.SERVER_RESETED;
            }
        }
        return ErrorCodes.OK;
    }

    public ErrorCodes removeUser(String UserId){
        currentLobby.removeUser(UserId);
        return ErrorCodes.OK;
    }
}
