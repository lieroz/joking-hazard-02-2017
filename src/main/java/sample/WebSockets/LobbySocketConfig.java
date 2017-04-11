package sample.Lobby.WebSockets;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * Created by ksg on 11.04.17.
 */
@EnableWebSocket
public class LobbySocketConfig implements WebSocketConfigurer{
    @NotNull
    private final WebSocketHandler webSocketHandler;
    public LobbySocketConfig (@NotNull WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }
    // TODO: CORSWEBSOCKET!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // 403 FOREVER!!!!
    //
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/lobby")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");
    }

}
