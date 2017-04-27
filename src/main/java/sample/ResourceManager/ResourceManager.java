package sample.ResourceManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sample.Game.Mechanics.Cards.CardDeck;
import sample.Game.Mechanics.Cards.GameCard;

import java.util.ArrayList;
import java.util.List;
import com.google.common.io.Resources;
import java.net.URL;

class Props{
    Props(){

    }
    public int numOfCardsInDeck;
    public int defaultMaxNumber;
    public int numberOfCardsInHand;
    @SuppressWarnings("unused")
    public void setNumOfCardsInDeck(int n){
        numOfCardsInDeck = n;
    }
    @SuppressWarnings("unused")
    public void setDefaultMaxNumber(int n ){
        defaultMaxNumber = n;
    }
    @SuppressWarnings("unused")
    public void setNumberOfCardsInHand(int n){
        numberOfCardsInHand = n;
    }
    @SuppressWarnings("unused")
    public int getNumOfCardsInDeck(){
        return numOfCardsInDeck;
    }
    @SuppressWarnings("unused")
    public int getDefaultMaxNumber(){
        return defaultMaxNumber;
    }
    @SuppressWarnings("unused")
    public int getNumberOfCardsInHand(){
        return numOfCardsInDeck;
    }
}

@Service
public class ResourceManager {
    private static final Logger logger = LoggerFactory.getLogger(ResourceManager.class);

    private Props props;
    public ResourceManager(){
        //numOfCardsInDeck = 50;
        //vdefaultMaxNumber = 4;
        //vnumberOfCardsInHand = 7;
        ObjectMapper mapper = new ObjectMapper();
        try {
            URL fl = Resources.getResource("Property");

            props = mapper.readValue(fl, Props.class);
            //assert props != null;
        } catch (Exception e){
           logger.error("can't read props", e);
        }

    }
   public CardDeck getCardDeck(){
       List<GameCard> lst = new ArrayList<>();
       for(int i = 0; i< props.numOfCardsInDeck; i++){
           GameCard card = new GameCard(i, (i%7)==0);
           lst.add(card);
       }
       return  new CardDeck(lst);
   }
   public int defaultMaxNumber(){
       return props.defaultMaxNumber;
   }

   public int numberOfCardsInHand(){
       return props.numberOfCardsInHand;
   }
}
