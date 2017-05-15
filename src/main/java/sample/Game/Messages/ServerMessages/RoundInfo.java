package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.*;

import java.util.Vector;

/**
 * Created by ksg on 14.05.17.
 */
public class RoundInfo extends BaseServerMessage {
    int roundNum;
    Vector<GameUserInfo> users;

    public RoundInfo(ObjectMapper mapper, Vector<GameUserInfo> users, int roundNum){
        super(mapper);
        this.roundNum = roundNum;
        this.users = users;
    }

    public String getType(){
        return "RoundInfo";
    }

    public Vector<GameUserInfo> getUsers(){
        return users;
    }

    public int getRoundNum(){
        return roundNum;
    }
}
