package sample.Game.Messages.UserMessages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * Created by ksg on 15.05.17.
 */
public class ChooseCardFromTable extends BaseUserMessage{
    int chosenCard;
    @JsonCreator
    public ChooseCardFromTable(
            @JsonProperty("chosenCard") int chosenCard
    ){
        this.chosenCard = chosenCard;
    }
    public String getType(){
        return "ChooseCardFromTable";
    }
    public int getCard(){
        return chosenCard;
    }
}
