package sample.Lobby.WebSockets;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.AuthenticationException;
import java.io.IOException;

import sample.Lobby.Commands.Command;
import sample.Lobby.Messages.*;
import sample.Lobby.Services.LobbyService;

/**
 * Created by ksg on 11.04.17.
 */

public class LobbyWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    LobbyService lobbyService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        LOGGER.debug("connection establisher handler");
        lobbyService.addUser(webSocketSession);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

        final String userId = (String) webSocketSession.getAttributes().get("userLogin");
        if(userId != null) {
            lobbyService.removeUser(userId);
        }
        webSocketSession.close();
        //TODO: sercer closed connection status?
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        final String  userId = (String) session.getAttributes().get("userLogin");
        if (userId == null) {
            ErrorMessage msg = new ErrorMessage("Invalid user session", objectMapper);
            String text_msg = msg.getJson();
            try {
                session.sendMessage(new TextMessage(text_msg));
            } catch (IOException e) {

            }
        } else {
            handleMessage(userId, session, message);
        }
    }

    private void handleMessage(String userId, WebSocketSession session, TextMessage text) {

        Command cmd;
        try {
            cmd= objectMapper.readValue(text.getPayload(), Command.class);
        } catch (IOException ex) {
            LOGGER.error("wrong json format at first level", ex);
            ErrorMessage msg = new ErrorMessage("Invalid first level json format", objectMapper);
            try {
                session.sendMessage(new TextMessage(msg.getJson()));
            }catch (IOException e) {
            }
            return;
        }
        switch (cmd.getCmd()){
            case "EndSession":{
                endSession(userId, session);
                break;
            }
            default:{
                LOGGER.error("wrong command");
                ErrorMessage msg = new ErrorMessage("Invalid command", objectMapper);
                try {
                    session.sendMessage(new TextMessage(msg.getJson()));
                }catch (IOException e) {
                }
            }

        }
    }
    private void endSession(String userId, WebSocketSession session){
        LobbyService.ErrorCodes codes = lobbyService.removeUser(userId);
        switch (codes){
            case OK: {
                OkMessage msg = new OkMessage("Ok", objectMapper);
                try {
                    session.sendMessage(new TextMessage(msg.getJson()));
                }catch (IOException e) {
                }
                break;
            }
        }
    }
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
