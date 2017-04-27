package sample.Game.WebSockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sample.Game.Services.ServerManager;
import sample.Lobby.Services.LobbyService;

import javax.naming.AuthenticationException;

/**
 * Created by ksg on 24.04.17.
 */
public class GameWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    ServerManager serverManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyService.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        LOGGER.debug("connection establisher handler");
        serverManager.connectUser(webSocketSession);
    }

}
