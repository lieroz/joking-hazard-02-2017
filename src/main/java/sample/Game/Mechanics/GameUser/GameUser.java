package sample.Game.Mechanics.GameUser;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.ServerMessages.HandInfo;
import sun.java2d.xr.MutableInteger;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

/**
 * Created by ksg on 26.04.17.
 */
public class GameUser implements GameUserInterface {
    WebSocketSession session;
    public GameUser(WebSocketSession session){
        this.session = session;
    }
    //for bots using in pattern strategy
    public void init(HandInfo info){
        send(info);
    }
    public ErrorCodes getCardFromDeck(){
        return ErrorCodes.OK;
    }

    public ErrorCodes chooseCardFromTable(){

        return ErrorCodes.OK;
    }

    public ErrorCodes send(BaseServerMessage msg){
        try {
            session.sendMessage(new TextMessage(msg.getJson()));
        } catch (IOException e){

        }
        return ErrorCodes.OK;
    }
    public void close(){
        try {
            session.close();
        }catch (IOException e){

        }
    }
}
