package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Mechanics.Cards.GameCard;

import java.util.Vector;

/**
 * Created by ksg on 27.04.17.
 */
public class HandInfo extends BaseServerMessage {
    Vector<GameCard> hand;
    public HandInfo(ObjectMapper mapper, Vector<GameCard> hand){
        super(mapper);
        this.hand = hand;

    }
    public String getType(){
        return "HandInfo";
    }
    public Vector<GameCard> getHand(){
        return hand;
    }
}
