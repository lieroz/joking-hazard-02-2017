package sample.Lobby.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyService.class);
    LobbyController currentLobby;

    void resetLobby(){
        LOGGER.debug("Lobby reseted");
        if(currentLobby != null) {
            currentLobby.closeConnections();
        }
        currentLobby = new LobbyController();
    }

    void createLobby(){
        LOGGER.debug("Lobby created");
        currentLobby = new LobbyController();
    }


    public LobbyService() {

    }

    public ErrorCodes addUser(WebSocketSession userSession){
        LOGGER.debug("User adding starts.");
        LobbyUserController user = new LobbyUserController();
        LobbyUserController.ErrorCodes initErr = user.lobbyUserControllerInit(userSession);
        switch (initErr){
            case OK:{
                LOGGER.debug("User inited");
                break;
            }
            case INVALID_LOGIN:{
                LOGGER.debug("Login is invalid");
                user.sendMessageToUser(new ErrorMessage("Invalid Login"));
                user.close();
                return ErrorCodes.INVALID_LOGIN;
            }
            case DATABASE_ERROR:{
                LOGGER.debug("Database error");
                user.sendMessageToUser(new ErrorMessage("Database Error"));
                user.close();
                return  ErrorCodes.DATABASE_ERROR;
            }
            case INVALID_SESSION:{
                LOGGER.debug("Invalid session");
                user.sendMessageToUser(new ErrorMessage("Invalid Session"));
                user.close();
                return ErrorCodes.INVALID_SESSION;
            }
            case SERVER_ERROR:{
                LOGGER.error("Server error in user Lobby Controller initialisation");
                user.sendMessageToUser(new ErrorMessage("Server Error"));
                user.close();
                return ErrorCodes.SERVER_ERROR;
            }
        }

        LobbyController.ErrorCodes err = currentLobby.addUser(user);
        switch (err){
            case OK:{
                LOGGER.debug("User added");
                break;
            }
            case LOBBY_IS_FOOL: {
                LOGGER.debug("Lobby is fool, game start init");
                //TODO: StartGame, retry to add new lobby
                currentLobby.gameStart();
                resetLobby();
                break;
            }
            case INVALID_LOGIN: {
                LOGGER.debug("Login is invalid");
                user.sendMessageToUser(new ErrorMessage("Invalid Login"));
                user.close();
                return ErrorCodes.INVALID_LOGIN;
            }
            case SERVER_ERROR: {
                LOGGER.error("Server error in user adding");
                user.sendMessageToUser(new ErrorMessage("Server Error"));
                user.close();
                resetLobby();
                return ErrorCodes.SERVER_RESETED;
            }
        }
        return ErrorCodes.OK;
    }

    public ErrorCodes removeUser(String UserId){
        LOGGER.debug("User removing starts");
        currentLobby.removeUser(UserId);
        LOGGER.debug("User removed");
        return ErrorCodes.OK;
    }
}
