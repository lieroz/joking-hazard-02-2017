package sample.Game.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Mechanics.MainMechanics;
import sample.Game.Messages.BaseMessageContainer;
import sample.Lobby.Views.LobbyGameView;
import sample.ResourceManager.ResourceManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.Runnable;

/**
 * Created by ksg on 25.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ServerWorker {//implements Runnable{
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

    private final ResourceManager manager;
    private final KeyGenerator generator;
    private final ObjectMapper mapper;
    private final Map<Integer,MainMechanics> games;
    private final ServerManager master;

    public ServerWorker(ServerManager master){
        manager = new ResourceManager();
        generator = new KeyGenerator();
        mapper = new ObjectMapper();
        games = new ConcurrentHashMap<>();
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

    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    public ErrorCodes handleMessage(BaseMessageContainer container){
        Integer gameIndex = container.getIndex().getIndex();
        MainMechanics mechanics = games.get(gameIndex);
        MainMechanics.ErrorCodes err = mechanics.handleMessage(container);
        if(err == MainMechanics.ErrorCodes.FINISHED){
            master.deleteUsers(mechanics.getUsersView());
            mechanics.finishGame();
        }
        return ErrorCodes.OK;
    }

    //public void run(){

    //}

}
