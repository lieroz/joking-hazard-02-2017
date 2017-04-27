package sample.Game.Mechanics.GameUser;

import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Messages.BaseGameMessage;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.ServerMessages.HandInfo;
import sun.java2d.xr.MutableInteger;

import java.util.Map;
import java.util.Vector;

/**
 * Created by ksg on 26.04.17.
 */
public interface GameUserInterface {
    public enum ErrorCodes{
        OK,
        DISCONNECTED,
    }
    public void init(HandInfo hand);
    public ErrorCodes getCardFromDeck();
    public ErrorCodes chooseCardFromTable();
    public ErrorCodes send(BaseServerMessage msg);
    public void close();
}
