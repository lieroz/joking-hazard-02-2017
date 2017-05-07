package sample.Game.Mechanics.Cards;

import java.util.ArrayList;
import java.util.Vector;
import java.util.Collections;
import java.util.List;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class CardDeck {
    private final List<GameCard> container;
    public CardDeck(List<GameCard> deck){
        container = new ArrayList<>(deck);
        Collections.shuffle(container);
    }
    @SuppressWarnings("WeakerAccess")
    public GameCard popCard(){
        if(container.isEmpty()){
            return null;
        }
        GameCard card = container.get(0);
        container.remove(0);
        return card;
    }
    public Vector<GameCard> popCards(int number){
        Vector<GameCard> res = new Vector<>();
        for(int i = 0; i< number; i++){
            GameCard card = popCard();
            if(card == null){
                return null;
            }
            res.add(card);
        }
        return  res;
    }
}
