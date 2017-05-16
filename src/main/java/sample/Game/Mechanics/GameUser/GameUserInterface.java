package sample.Game.Mechanics.GameUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.ServerMessages.HandInfo;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public interface GameUserInterface {
    enum ErrorCodes{
        OK,
        @SuppressWarnings("unused")DISCONNECTED,
    }
    void init(HandInfo hand);
    @SuppressWarnings({"SameReturnValue", "unused"})
    ErrorCodes chooseCardFromHand(ObjectMapper mapper);
    @SuppressWarnings({"SameReturnValue", "unused"})
    ErrorCodes chooseCardFromTable(ObjectMapper mapper);
    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    ErrorCodes send(BaseServerMessage msg);
    void close();
    boolean isUser();
}
