package sample.Game.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sample.Game.Messages.ServerMessages.ServerErrorMessage;
import sample.Game.Messages.SystemMessages.MessageContainer;
import sample.Game.Messages.SystemMessages.UserConnectedMessage;
import sample.Game.Messages.UserMessages.UserMessageContainer;
import sample.Lobby.Messages.ErrorMessage;
import sample.Lobby.Views.LobbyGameView;
import sample.Lobby.Views.UserGameView;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ksg on 25.04.17.
 */


@SuppressWarnings("DefaultFileTemplate")
@Service
public class ServerManager {

    public enum ErrorCodes {
        @SuppressWarnings("EnumeratedConstantNamingConvention")OK,
        ERROR_GAME_CREATION,
        ERROR_GAME_CONNECTION,
    }

    public static class GameIndex {
        final int index;

        public GameIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    private final ServerWorker worker;
    private final Map<String, GameIndex> indexMap;
    private final ObjectMapper mapper;

    public ServerManager() {
        indexMap = new ConcurrentHashMap<>();
        mapper = new ObjectMapper();
        worker = new ServerWorker(this); // it'll be pull
    }

    public ErrorCodes createGame(LobbyGameView view) {
        final Integer ind = worker.createGame(view);
        if (ind < 0) {
            return ErrorCodes.ERROR_GAME_CREATION;
        }
        final GameIndex index = new GameIndex(ind);
        final Vector<UserGameView> ulist = view.getList();
        for (UserGameView user : ulist) {
            final String id = user.getUserId();
            indexMap.put(id, index);
        }
        return ErrorCodes.OK;
    }

    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    private ErrorCodes sendError(WebSocketSession session, String errorText) {
        final ServerErrorMessage err = new ServerErrorMessage(mapper, errorText);
        @SuppressWarnings({"unused", "LocalVariableNamingConvention"}) String ErrorText;
        try {
            session.sendMessage(new TextMessage(err.getJson()));
        } catch (IOException ignored) {

        }
        return ErrorCodes.OK;
    }

    //TODO: обернуть websocketsession
    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    public ErrorCodes connectUser(WebSocketSession session) {//TODO: change work with session level
        final String userId = (String) session.getAttributes().get("userLogin");
        if (userId == null) {
            sendError(session, "No login");
            return ErrorCodes.ERROR_GAME_CONNECTION;
        }
        final GameIndex index = indexMap.get(userId);
        if (index == null) {
            sendError(session, "No login in game system");
            return ErrorCodes.ERROR_GAME_CONNECTION;
        }
        final UserConnectedMessage msg = new UserConnectedMessage(session);
        worker.handleMessage(new MessageContainer(userId, index, msg));
        return ErrorCodes.OK;
    }

    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    public ErrorCodes deleteUsers(LobbyGameView view) {
        final Vector<UserGameView> ulist = view.getList();
        for (UserGameView user : ulist) {
            final String id = user.getUserId();
            indexMap.remove(id);
        }
        return ErrorCodes.OK;
    }

    public void addMessage(WebSocketSession session, String userMessage) {
        final String userId = (String) session.getAttributes().get("userLogin");
        if (userId == null) {
            final ErrorMessage msg = new ErrorMessage("Invalid user session", mapper);
            @SuppressWarnings("LocalVariableNamingConvention") final String text_msg = msg.getJson();
            try {
                session.sendMessage(new TextMessage(text_msg));
            } catch (IOException ignored) {

            }
            return;
        }
        final GameIndex index = indexMap.get(userId);
        if (index == null) {
            sendError(session, "No login in game system");
            return;
        }
        worker.handleMessage(new UserMessageContainer(userId, index, userMessage));
    }

    public boolean userExist(String userId) {
        return indexMap.containsKey(userId);
    }
}
