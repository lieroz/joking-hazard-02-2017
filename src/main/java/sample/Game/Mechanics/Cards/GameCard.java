package sample.Game.Mechanics.Cards;

/**
 * Created by ksg on 26.04.17.
 */
public class GameCard {
    int id;
    boolean red;
    public GameCard(int id, boolean red){
        this.id = id;
        this.red = red;
    }
    public int getId(){
        return id;
    }
    public boolean getRed(){
        return red;
    }
}
