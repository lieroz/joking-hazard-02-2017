package sample.Lobby.Messages;


import sample.Main.Views.UserDataView;
import sample.Main.Views.UserInfo;

/**
 * Created by ksg on 12.04.17.
 */
public class UserExitedMessage extends  BaseMessage {

    UserInfo view;

    public UserExitedMessage(UserInfo view){
        this.view = view;
    }

    @Override
    public String getType(){
        return "UserExitedMessage";
    }
    public UserInfo getUser(){
        return view;
    }

}
