package sample.Lobby.Commands;

/**
 * Created by ksg on 24.04.17.
 */
public class Command {
    String cmd;
    String arguments;
    public String getCmd(){
        return cmd;
    }
    public String getArguments(){
        return arguments;
    }
    public void setCmd(String cmd){
        this.cmd = cmd;
    }
    public void setArguments(String arguments){
        this.arguments = arguments;
    }
}
