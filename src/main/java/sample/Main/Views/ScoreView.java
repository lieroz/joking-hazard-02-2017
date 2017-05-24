package sample.Main.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import sample.Main.Models.UserScoreModel;

import java.util.List;

/**
 * Created by lieroz on 24.05.17.
 */
public class ScoreView {
    private List<UserScoreModel> topAndUser;
    @JsonCreator
    public ScoreView(@JsonProperty("topAndUser") List<UserScoreModel> topAndUser) {
        this.topAndUser = topAndUser;
    }

    public List<UserScoreModel> getTopAndUser() {
        return this.topAndUser;
    }
}
