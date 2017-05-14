package sample.Game.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sample.Game.Messages.SystemMessages.MessageContainer;
import sample.Game.Messages.ServerMessages.ServerErrorMessage;
import sample.Game.Messages.SystemMessages.UserConnectedMessage;
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

    public enum ErrorCodes{
        OK,
        ERROR_GAME_CREATION,
    }
    public class GameIndex{
        final int index;
        public GameIndex(int index){
            this.index = index;
        }
        public int getIndex(){
            return index;
        }
    }

    private final ServerWorker worker;
    private final Map<String,GameIndex> indexMap;
    private final ObjectMapper mapper;
    public ServerManager(){
         indexMap = new ConcurrentHashMap<>();
         mapper = new ObjectMapper();
         worker = new ServerWorker(this); // it'll be pull
    }

    public ErrorCodes createGame(LobbyGameView view){
        Integer ind = worker.createGame(view);
        if(ind <0){
            return ErrorCodes.ERROR_GAME_CREATION;
        }
        GameIndex index = new GameIndex(ind);
        Vector<UserGameView> ulist = view.getList();
        for(UserGameView user: ulist){
            String id = user.getUserId();
            indexMap.put(id, index);
        }
        return ErrorCodes.OK;
    }
    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    private ErrorCodes sendError(WebSocketSession session, String errorText){
        ServerErrorMessage err = new ServerErrorMessage(mapper, errorText);
        @SuppressWarnings("unused") String ErrorText;
        try {
            session.sendMessage(new TextMessage(err.getJson()));
        } catch (IOException ignored){

        }
        return ErrorCodes.OK;
    }
    //TODO: обернуть websocketsession
    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    public ErrorCodes connectUser(WebSocketSession session){
        String userId = (String) session.getAttributes().get("userLogin");
        if(userId == null) {
            sendError(session, "No login");
        }
        GameIndex index = indexMap.get(userId);
        if(index == null){
            sendError(session, "No login in game system");
        }
        UserConnectedMessage msg = new UserConnectedMessage(session);
        worker.handleMessage(new MessageContainer(userId,index,msg));
        return ErrorCodes.OK;
    }
    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    public ErrorCodes deleteUsers(LobbyGameView view){
        Vector<UserGameView> ulist = view.getList();
        for(UserGameView user: ulist){
            String id = user.getUserId();
            indexMap.remove(id);
        }
        return ErrorCodes.OK;
    }

    public boolean userExist(String userId){
        return indexMap.containsKey(userId);
    }
}
