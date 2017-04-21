package sample.WebSockets;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import sample.Lobby.WebSockets.LobbyWebSocketHandler;
/**
 * Created by ksg on 11.04.17.
 */
@Configuration
@EnableWebSocket
public class LobbySocketConfig implements WebSocketConfigurer{
    /*@NotNull
    private final WebSocketHandler webSocketHandler;
    public LobbySocketConfig (@NotNull WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }*/
    // TODO: CORSWEBSOCKET!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*
    // 403 FOREVER!!!!
    //
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(LobbyWebSocketHandler(), "/lobby");
    }
    @Bean
    public WebSocketHandler LobbyWebSocketHandler(){
        return new LobbyWebSocketHandler();
    }

}
