package sample.Lobby.WebSockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.naming.AuthenticationException;

import sample.Lobby.Services.LobbyService;
/**
 * Created by ksg on 11.04.17.
 */

public class LobbyWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    LobbyService lobbyService;

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
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
