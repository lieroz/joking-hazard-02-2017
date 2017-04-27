package sample.Game.Messages.SystemMessages;

import org.springframework.web.socket.WebSocketSession;

/**
 * Created by ksg on 26.04.17.
 */
public class UserConnectedMessage extends BaseSystemMessage {
    WebSocketSession session;
    public UserConnectedMessage(WebSocketSession session){
        this.session = session;
    }
    public String getType(){
        return "UserConnected";
    }
    public WebSocketSession getSession(){
        return session;
    }
    public Class getClassOfMessage(){
        return UserConnectedMessage.class;
    }
}
