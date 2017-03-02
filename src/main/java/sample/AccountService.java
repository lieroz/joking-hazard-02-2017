package sample;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Service
public class AccountService {

    private final Map<String, UserData> userNameToUserProfile = new HashMap<>();
    @NotNull
    public  ResponceCode register(@NotNull UserData data) {
        boolean result = true;
        String message = "ok";
        if(userNameToUserProfile.containsKey(data.getUserLogin())){
            result = false;
            final ApplicationContext context = new ClassPathXmlApplicationContext("local.xml");
            message = context.getMessage("msgs.login_occupied",null, Locale.ENGLISH);
        } else {
            userNameToUserProfile.put(data.getUserLogin(), data);
        }
        return new ResponceCode(result, message);
    }

    public ResponceCode login(@NotNull LogInController.LogInData data) {
        final String login = data.getUserLogin();
        boolean result = false;
        String msg = "Invalid login";
        final String hash = userNameToUserProfile.get(login).getPassHash();
        if(hash == null){
            result = false;
            msg = "invalid session";
        } else {
            if (hash.compareTo(data.getPassHash()) == 0) {
                result = true;
                msg = "Ok";
            }
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
            msg = "invalid session";
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
            msg = "invalid session";
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
                    msg = "invalid pass";
                }
            }else
            {
                res = false;
                msg = "invalid session";
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