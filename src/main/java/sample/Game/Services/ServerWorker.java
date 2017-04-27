package sample.Game.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.WebSocketSession;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.SystemMessages.MessageContainer;
import sample.Game.Messages.SystemMessages.UserConnectedMessage;
import sample.Lobby.Views.LobbyGameView;
import sample.RecouceManager.ResourceManager;
import sun.applet.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ksg on 25.04.17.
 */
public class ServerWorker {
    enum ErrorCodes{
        OK,

    }
    private class KeyGenerator{
        final int keyMod = 99999; // TODO: MAKE IT AS CONFIG
        int current;
        public KeyGenerator(){
            current = 0;
        }
        public int getIndex(){
            current = (current+1)%keyMod;
            return current;
        }
    }

    ResourceManager manager;
    KeyGenerator generator;
    ObjectMapper mapper;
    Map<Integer,MainMechanics> games;
    ServerManager master;

    public ServerWorker(ServerManager master){
        manager = new ResourceManager();
        generator = new KeyGenerator();
        mapper = new ObjectMapper();
        games = new ConcurrentHashMap<Integer, MainMechanics>();
        this.master = master;
    }
    public Integer createGame(LobbyGameView players){
        MainMechanics game = new MainMechanics(mapper);
        MainMechanics.ErrorCodes error = game.init(players,manager);
        switch (error){
            case OK:{
                break;
            }
            case SERVER_ERROR:{
                return -1;
            }
        }
        Integer key = generator.getIndex();
        games.put(key,game);
        return key;
    }

    public ErrorCodes handleMessage(MessageContainer container){
        Integer gameIndex = container.getIndex().getIndex();
        MainMechanics mechanics = games.get(gameIndex);
        MainMechanics.ErrorCodes err = mechanics.handleMessage(container);
        if(err == MainMechanics.ErrorCodes.FINISHED){
            master.deleteUsers(mechanics.getUsersView());
            mechanics.finishGame();
        }
        return ErrorCodes.OK;
    }

}
