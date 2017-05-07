package sample.Main.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import sample.Main.Views.UserInfo;

/**
 * Created by ksg on 17.03.17.
 */

@SuppressWarnings("DefaultFileTemplate")
public final class UserInfoModel {
    private String userMail;
    private String userLogin;

    @JsonCreator
    public UserInfoModel(@SuppressWarnings("SameParameterValue") @JsonProperty("userMail") String userMail,
                         @SuppressWarnings("SameParameterValue") @JsonProperty("userLogin") String userLogin) {
        this.userLogin = userLogin;
        this.userMail = userMail;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(final String userLogin) {
        this.userLogin =  userLogin;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(final String userMail) {
        this.userMail = userMail;
    }

    public UserInfo getUserInfo(){
        return new UserInfo(userMail,userLogin);
    }
}