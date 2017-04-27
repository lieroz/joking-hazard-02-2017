package sample.Game.Mechanics.GameUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.ServerMessages.HandInfo;
import sun.java2d.xr.MutableInteger;

import java.util.Map;
import java.util.Vector;

/**
 * Created by ksg on 26.04.17.
 */
public class GameUserItem {
    Vector<GameCard> hand;
    Integer score;
    GameUserInterface user;
    ObjectMapper mapper;
    public GameUserItem(Vector<GameCard> hand, ObjectMapper mapper){
        this.mapper = mapper;
        this.hand = new Vector<GameCard>(hand);
        score = new Integer(0);

    }
    public void setStrategy(GameUserInterface inter){
        inter.init(new HandInfo(mapper, hand));
        user = inter;
    }
    public void sendMessage(BaseServerMessage msg){
        if(user != null)
            user.send(msg);
    }
    public void close(){
        user.close();
    }
}
