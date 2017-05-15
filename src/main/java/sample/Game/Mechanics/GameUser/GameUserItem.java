package sample.Game.Mechanics.GameUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.ServerMessages.GameUserInfo;
import sample.Game.Messages.ServerMessages.HandInfo;

import java.util.Vector;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class GameUserItem {
    private final Vector<GameCard> hand;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Integer score;
    private GameUserInterface user;
    private final ObjectMapper mapper;
    public GameUserItem(Vector<GameCard> hand, ObjectMapper mapper){
        this.mapper = mapper;
        this.hand = new Vector<>(hand);
        score = 0;

    }
    public void setStrategy(GameUserInterface inter){
        inter.init(new HandInfo(mapper, hand));
        user = inter;
    }
    public void sendMessage(BaseServerMessage msg){
        if(user != null)
            user.send(msg);
    }
    public boolean isUser(){
        return user.isUser();
    }
    public void close(){
        user.close();
    }
    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }
}
