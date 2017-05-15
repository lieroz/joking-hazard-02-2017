package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Mechanics.Cards.GameCard;

import java.util.Vector;

/**
 * Created by ksg on 14.05.17.
 */
public class TableInfo extends BaseServerMessage{
    private final GameCard[] table;
    public TableInfo(ObjectMapper mapper,GameCard[] table){
        super(mapper);
        this.table = table;

    }
    public String getType(){
        return "TableInfo";
    }
    @SuppressWarnings("unused")
    public GameCard[] getHand(){
        return table;
    }
}
