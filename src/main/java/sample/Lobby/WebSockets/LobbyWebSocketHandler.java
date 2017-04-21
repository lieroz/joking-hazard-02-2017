package sample.Lobby.WebSockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.AuthenticationException;

import sample.Lobby.Services.LobbyService;
/**
 * Created by ksg on 11.04.17.
 */

public class LobbyWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    LobbyService lobbyService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyService.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        LOGGER.debug("connection establisher handler");
        lobbyService.addUser(webSocketSession);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

        final String userId = (String) webSocketSession.getAttributes().get("userId");
        if(userId != null) {
            lobbyService.removeUser(userId);
        }
        //TODO: sercer closed connection status?
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
