package sample;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Service
public class AccountService {
    private final MessageSource messageSource;
    private final Map<String, UserData> userNameToUserProfile = new HashMap<>();

    @SuppressWarnings("unused")
    public AccountService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @SuppressWarnings("unused")
    @NotNull
    public ResponseCode register(@NotNull UserData data) {
        String msg = "ok";
        final String login = data.getUserLogin();
        if (login == null) {
            msg = messageSource.getMessage("msgs.invalid_auth_data", null, Locale.ENGLISH);
            return new ResponseCode(false, msg);
        }
        if (userNameToUserProfile.containsKey(login)) {
            msg = messageSource.getMessage("msgs.login_occupied", null, Locale.ENGLISH);
            return new ResponseCode(false, msg);
        }
        userNameToUserProfile.put(login, data);
        final boolean result = true;
        return new ResponseCode(result, msg);
    }

    public ResponseCode login(@NotNull LogInController.LogInData data) {
        final String msg;
        final String login = data.getUserLogin();
        if (login == null) {
            msg = messageSource.getMessage("msgs.invalid_auth_data", null, Locale.ENGLISH);
            return new ResponseCode(false, msg);
        }
        final UserData record = userNameToUserProfile.get(login);
        if (record == null) {
            msg = messageSource.getMessage("msgs.invalid_auth_data", null, Locale.ENGLISH);
            return new ResponseCode(false, msg);
        }
        final String hash = record.getPassHash();
        if (!hash.equals(data.getPassHash())) {
            msg = messageSource.getMessage("msgs.invalid_auth_data", null, Locale.ENGLISH);
            return new ResponseCode(false, msg);
        }

        return new ResponseCode(true, "ok");
    }

    @SuppressWarnings("unused")
    public ResponseCode changeMail(@NotNull String newMail, @NotNull String login) {
        String msg = "ok";
        final UserData data = userNameToUserProfile.get(login);
        if (data == null) {
            msg = messageSource.getMessage("msgs.invalid_session", null, Locale.ENGLISH);
            return new ResponseCode(false, msg);
        }
        data.setUserMail(newMail);
        return new ResponseCode(true, msg);
    }

    @SuppressWarnings("unused")
    public ResponseCode changePassHash(@NotNull String newPassHash, @NotNull String login) {
        String msg = "ok";
        final UserData data = userNameToUserProfile.get(login);
        if (data == null) {
            msg = messageSource.getMessage("msgs.invalid_session", null, Locale.ENGLISH);
            return new ResponseCode(false, msg);
        }
        data.setPassHash(newPassHash);
        return new ResponseCode(true, msg);
    }

    public ResponseCode checkPass(@NotNull String passHash, @NotNull String login) {
        String msg = "ok";
        final UserData data = userNameToUserProfile.get(login);
        if (data == null) {
            msg = messageSource.getMessage("msgs.invalid_session", null, Locale.ENGLISH);
            return new ResponseCode(false, msg);
        }
        final String passH = data.getPassHash();
        if (!passHash.equals(passH)) {
            msg = messageSource.getMessage("msgs.invalid_password", null, Locale.ENGLISH);
            return new ResponseCode(false, msg);
        }
        return new ResponseCode(true, msg);
    }


    @SuppressWarnings("unused")
    public UserData.UserInfo getUserData(@NotNull String login) {
        final UserData data = userNameToUserProfile.get(login);
        if (data != null) {
            return new UserData.UserInfo(data.getUserMail(), data.getUserLogin());
        }
        return new UserData.UserInfo("", "");
    }

}