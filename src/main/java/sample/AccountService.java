package sample;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



@SuppressWarnings("ALL")
@Service
public class AccountService {

    private final Map<String, SignUpController.SignUpData> userNameToUserProfile = new HashMap<>();
    @NotNull
    public  ResponceCode register(@NotNull SignUpController.SignUpData data) {
        boolean result = true;
        String message = "ok";
        if(userNameToUserProfile.containsKey(data.getUserLogin())){
            result = false;
            ApplicationContext context = new ClassPathXmlApplicationContext("local.xml");
            message = context.getMessage("msgs.login_occupied",new Object[] {28, "" }, Locale.ENGLISH);
        } else {
            userNameToUserProfile.put(data.getUserLogin(), data);
        }
        return new ResponceCode(result, message);
    }

    public ResponceCode login(@NotNull LogInController.LogInData data) {
        final String login = data.getUserLogin();
        boolean result = false;
        String msg = "Invalid login";
        try {
            final String hash = userNameToUserProfile.get(login).getPassHash();
            if (hash.compareTo(data.getPassHash()) == 0){
                result = true;
                msg = "Ok";
            }
        } catch (NullPointerException b){
            result = false;
            msg = "invalid session";
        }
        return new ResponceCode(result, msg);
    }

    public ResponceCode changeMail(@NotNull String newMail, @NotNull String login){
        String msg = "ok";
        boolean res = true;
        try {
            userNameToUserProfile.get(login).setUserMail(newMail);
        } catch (NullPointerException a){
            res = false;
            msg = "invalid session";
        }
        return new ResponceCode(res, msg);
    }
    public ResponceCode changePassHash(@NotNull String newPassHash, @NotNull String login){
        String msg = "ok";
        boolean res = true;
        try {
            userNameToUserProfile.get(login).setPassHash(newPassHash);
        } catch (NullPointerException a){
            res = false;
            msg = "invalid session";
        }
        return new ResponceCode(res, msg);
    }

    public ResponceCode checkPass(@NotNull String passHash, @NotNull String login){
        String msg = "ok";
        boolean res = true;
        try {
            final String passH = userNameToUserProfile.get(login).getPassHash();
            if (passHash.compareTo(passH) == 0) {
                res = true;
                msg = "Ok";
            } else {
                res = false;
                msg = "invalid pass";
            }
        } catch (NullPointerException a){
            res = false;
            msg = "invalid session";
        }
        return new ResponceCode(res, msg);
    }



    public UserController.UserData getUserData(@NotNull String id) {
        final SignUpController.SignUpData data = userNameToUserProfile.get(id);
        UserController.UserData dat;
        try {
            dat = new UserController.UserData(data.getUserMail(), data.getUserLogin());
        }catch (NullPointerException e){
            dat = new UserController.UserData("", "");
        }
        return  dat;
    }

}