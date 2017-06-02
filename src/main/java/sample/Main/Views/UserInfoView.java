package sample.Main.Views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

/**
 * Created by lieroz on 2.06.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public final class UserInfoView {
    private Integer id;
    private String userMail;
    private String userLogin;
    private Integer score;

    public UserInfoView() {

    }

    @JsonCreator
    public UserInfoView(@Nullable @JsonProperty("id") Integer id,
                        @Nullable @JsonProperty("userMail") String userMail,
                        @Nullable @JsonProperty("userLogin") String userLogin,
                        @Nullable @JsonProperty("score") Integer score) {
        this.id = id;
        this.userLogin = userLogin;
        this.userMail = userMail;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(final String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(final String userMail) {
        this.userMail = userMail;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(final Integer score) {
        this.score = score;
    }
}