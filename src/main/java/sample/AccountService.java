package sample;

import org.jetbrains.annotations.NotNull;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;

import java.util.HashMap;
//import java.util.Locale;
import java.util.Locale;
import java.util.Map;


@Service
public class AccountService {
    private final MessageSource messageSource;
    private final Map<String, UserData> userNameToUserProfile = new HashMap<>();
    public  AccountService(MessageSource messageSource){
        this.messageSource = messageSource;
    }
    @NotNull
    public  ResponceCode register(@NotNull UserData data) {
        boolean result = true;
        String msg = "ok";
        String login = data.getUserLogin();
        if (login != null) {
            if (userNameToUserProfile.containsKey(login)) {
                result = false;
                msg = messageSource.getMessage("msgs.login_occupied", null, Locale.ENGLISH);
            } else {
                userNameToUserProfile.put(login, data);
            }
        } else {
            result = false;
            msg = messageSource.getMessage("msgs.invalid_auth_data", null, Locale.ENGLISH);
        }
        return new ResponceCode(result, msg);
    }

    public ResponceCode login(@NotNull LogInController.LogInData data) {
        boolean result;
        String msg;
        final String login = data.getUserLogin();
        if (login != null) {
            result = false;
            msg = messageSource.getMessage("msgs.invalid_auth_data",null,Locale.ENGLISH);
            final UserData rec = userNameToUserProfile.get(login);
            if (rec == null) {
                result = false;
                msg = messageSource.getMessage("msgs.invalid_auth_data",null,Locale.ENGLISH);
            } else {
                final String hash = rec.getPassHash();
                if (hash.equals(data.getPassHash())) {
                    result = true;
                    msg = "Ok";
                }
            }
        } else {
            result = false;
            msg =  messageSource.getMessage("msgs.invalid_auth_data",null,Locale.ENGLISH);
        }
        return new ResponceCode(result, msg);
    }

    public ResponceCode changeMail(@NotNull String newMail, @NotNull String login){
        String msg = "ok";
        boolean res = true;
        final UserData data = userNameToUserProfile.get(login);
        if (data != null){
            data.setUserMail(newMail);
        }else
        {
            res = false;
            msg =  messageSource.getMessage("msgs.invalid_session",null,Locale.ENGLISH);
        }
        return new ResponceCode(res, msg);
    }
    public ResponceCode changePassHash(@NotNull String newPassHash, @NotNull String login){
        String msg = "ok";
        boolean res = true;
        final UserData data = userNameToUserProfile.get(login);
        if (data != null){
            data.setPassHash(newPassHash);
        }else
        {
            res = false;
            msg = messageSource.getMessage("msgs.invalid_session",null,Locale.ENGLISH);
        }
        return new ResponceCode(res, msg);
    }

    public ResponceCode checkPass(@NotNull String passHash, @NotNull String login){
        String msg = "ok";
        boolean res = true;
            final UserData data = userNameToUserProfile.get(login);
            if (data != null){
                final String passH = data.getPassHash();
                if (!passHash.equals(passH)) {
                    res = false;
                    msg = messageSource.getMessage("msgs.invalid_password",null,Locale.ENGLISH);
                }
            }else
            {
                res = false;
                msg = messageSource.getMessage("msgs.invalid_session",null,Locale.ENGLISH);
            }
        return new ResponceCode(res, msg);
    }



    public UserData.UserInfo getUserData(@NotNull String login) {
        final UserData data = userNameToUserProfile.get(login);
        final UserData.UserInfo dat;
        if (data != null){
            dat = new UserData.UserInfo(data.getUserMail(), data.getUserLogin());
        }else
        {
            dat = new UserData.UserInfo("", "");
        }
        return  dat;
    }

}