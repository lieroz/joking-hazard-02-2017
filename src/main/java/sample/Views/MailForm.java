package sample.Views;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 18.03.17.
 */

// Changed name to merge pull request
public final class MailForm {
    private String userMail;

    @SuppressWarnings("unused")
    public MailForm(@JsonProperty("userMail") String userMail) {
        this.userMail = userMail;
    }

    @SuppressWarnings("unused")
    public String getUserMail() {
        return userMail;
    }

    @SuppressWarnings("unused")
    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
}
