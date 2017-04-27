package sample.Lobby.Views;

import java.util.Vector;

/**
 * Created by ksg on 25.04.17.
 */
public class LobbyGameView {
    Vector<UserGameView> list;
    int number;
    public LobbyGameView(Vector<UserGameView> list, int number){
        this.list = list;
        this.number = number;
    }
    public Vector<UserGameView>getList(){
        return list;
    }
    public int getNumber(){
        return number;
    }

}
