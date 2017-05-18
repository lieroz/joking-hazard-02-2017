package sample.Game.Mechanics.GameUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.ServerMessages.HandInfo;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public interface GameUserInterface {
    enum ErrorCodes {
        @SuppressWarnings("EnumeratedConstantNamingConvention")OK,
        @SuppressWarnings("unused")DISCONNECTED,
    }

    void init(HandInfo hand);

    @SuppressWarnings({"SameReturnValue", "unused"})
    void chooseCardFromHand(ObjectMapper mapper);

    @SuppressWarnings({"SameReturnValue", "unused"})
    void chooseCardFromTable(ObjectMapper mapper);

    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    ErrorCodes send(BaseServerMessage msg);

    void sendHandInfo(HandInfo hand);

    void close();

    @SuppressWarnings("SameReturnValue")
    boolean isUser();
}
