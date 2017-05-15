package sample.Game.WebSockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sample.Game.Messages.UserMessages.UserMessageContainer;
import sample.Game.Services.ServerManager;
import sample.Lobby.Messages.ErrorMessage;
import sample.Lobby.Services.LobbyService;

import javax.naming.AuthenticationException;
import java.io.IOException;

/**
 * Created by ksg on 24.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class GameWebSocketHandler extends TextWebSocketHandler {
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    ServerManager serverManager;
    ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyService.class);


    public GameWebSocketHandler() {
        objectMapper = new ObjectMapper();
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        LOGGER.debug("text message handler");

        serverManager.addMessage(session, message.getPayload());
        //serverManager.addMessage(userId, new UserMessageContainer(message.getPayload()));
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        LOGGER.debug("connection establisher handler");
        serverManager.connectUser(webSocketSession);
    }

}
