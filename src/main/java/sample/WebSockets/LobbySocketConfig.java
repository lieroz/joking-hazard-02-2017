package sample.WebSockets;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import sample.Lobby.WebSockets.LobbyWebSocketHandler;
import sample.Game.WebSockets.GameWebSocketHandler;
/**
 * Created by ksg on 11.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
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
        registry.addHandler(LobbyWebSocketHandler(), "/lobby")
                .setAllowedOrigins("https://jokinghazard.herokuapp.com")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
        registry.addHandler(GameWebSocketHandler(), "/game")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }
    @Bean
    public WebSocketHandler LobbyWebSocketHandler(){
        return new LobbyWebSocketHandler();
    }
    @Bean
    public WebSocketHandler GameWebSocketHandler(){
        return new GameWebSocketHandler();
    }
}
