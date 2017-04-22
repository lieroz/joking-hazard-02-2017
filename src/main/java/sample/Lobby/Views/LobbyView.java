package sample.Lobby.Views;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Lobby.Messages.BaseMessage;
import sample.Main.Views.UserInfo;

import java.util.Vector;

/**
 * Created by ksg on 11.04.17.
 */
public class LobbyView extends BaseMessage {
    Vector<UserInfo> users;
    public LobbyView(ObjectMapper mapper, Vector<UserInfo> users){
        super(mapper);
        this.users = users;
    }
    public String getType(){return "Lobby Info";}
    public Vector<UserInfo> getUsers(){
        return users;
    }
}
