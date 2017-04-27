package sample.Lobby.Views;

import javax.jws.soap.SOAPBinding;

/**
 * Created by ksg on 25.04.17.
 */
public class UserGameView {
    String UserLogin;
    public UserGameView(String UserLogin){
        this.UserLogin = UserLogin;
    }
    public String getUserId(){
        return this.UserLogin;//пока просто заглушка
    }
    public String getUserLogin(){
        return  this.UserLogin;
    }
}
