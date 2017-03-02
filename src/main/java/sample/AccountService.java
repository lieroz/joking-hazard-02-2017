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
    public  SignUpController.ResponceCode register(@NotNull SignUpController.SignUpData data) {
        boolean result = true;
        String message = "ok";
        if(userNameToUserProfile.containsKey(data.getUserLogin())){
            result = false;
            ApplicationContext context = new ClassPathXmlApplicationContext("local.xml");
            message = context.getMessage("msgs.login_occupied",new Object[] {28, "" }, Locale.ENGLISH);
        } else {
            userNameToUserProfile.put(data.getUserLogin(), data);
        }
        return new SignUpController.ResponceCode(result, message);
    }

    public LogInController.ResponceCode login(@NotNull LogInController.LogInData data) {
        final String login = data.getUserLogin();
        boolean result = false;
        try {
            final String hash = userNameToUserProfile.get(login).getPassHash();
            if (hash.compareTo(data.getPassHash()) == 0){
                result = true;
            }
        } catch (NullPointerException b){
            result = false;
        }
        return new LogInController.ResponceCode(result);
    }

    public void changeMail(@NotNull String newMail, @NotNull String login){
        userNameToUserProfile.get(login).setUserMail(newMail);
    }
    public void changePassHash(@NotNull String newPassHash, @NotNull String login){
        userNameToUserProfile.get(login).setPassHash(newPassHash);
    }

    public boolean checkPass(@NotNull String passHash, @NotNull String login){
        final String passH = userNameToUserProfile.get(login).getPassHash();
        return  (passHash.compareTo(passH) == 0);
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