package sample.Game.Messages.UserMessages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 15.05.17.
 */
public class ChooseCardFromHand extends BaseUserMessage{
    int chosenCard;
    @JsonCreator
    public ChooseCardFromHand(
            @JsonProperty("chosenCard") int chosenCard
    ){
        this.chosenCard = chosenCard;
    }

    public String getType(){
        return "ChooseCardFromHand";
    }
    public int getChosenCard(){
        return chosenCard;
    }
}
