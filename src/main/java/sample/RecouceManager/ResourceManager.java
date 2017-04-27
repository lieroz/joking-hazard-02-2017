package sample.RecouceManager;

/**
 * Created by ksg on 26.04.17.
 */

import sample.Game.Mechanics.Cards.CardDeck;
import sample.Game.Mechanics.Cards.GameCard;

import java.util.ArrayList;
import java.util.List;

public class ResourceManager {
    int numOfCardsInDeck;
    public ResourceManager(){
        numOfCardsInDeck = 50;
    }
   public CardDeck getCardDeck(){
       List<GameCard> lst = new ArrayList<>();
       for(int i = 0; i< numOfCardsInDeck; i++){
           GameCard card = new GameCard(i, (i%7)==0);
           lst.add(card);
       }
       return  new CardDeck(lst);
   }
}
