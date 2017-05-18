package sample.Game.Mechanics.GameUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.ServerMessages.GetCardFromHand;
import sample.Game.Messages.ServerMessages.GetCardFromTable;
import sample.Game.Messages.ServerMessages.HandInfo;

import java.io.IOException;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class GameUser implements GameUserInterface {
    private final WebSocketSession session;

    public GameUser(WebSocketSession session) {
        this.session = session;
    }

    //for bots using in pattern strategy
    @Override
    public void init(HandInfo info) {
        send(info);
    }

    @Override
    public void sendHandInfo(HandInfo info) {
        send(info);
    }

    @Override
    public void chooseCardFromHand(ObjectMapper mapper) {
        send(new GetCardFromHand(mapper));
    }

    @Override
    public void chooseCardFromTable(ObjectMapper mapper) {
        send(new GetCardFromTable(mapper));
    }

    @Override
    public ErrorCodes send(BaseServerMessage msg) {
        try {
            session.sendMessage(new TextMessage(msg.getJson()));
        } catch (IOException ignored) {

        }
        return ErrorCodes.OK;
    }

    @Override
    public void close() {
        try {
            session.close();
        } catch (IOException ignored) {

        }
    }

    @Override
    public boolean isUser() {
        return true;
    }
}
