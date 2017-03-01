package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.applet.AppletContext;

import java.util.Locale;


@SuppressWarnings("ALL")
@RestController
public class UserController {
    @NotNull
    final AccountService accServ;
    public UserController(@NotNull AccountService accountService){
        this.accServ = accountService;
    }
    @CrossOrigin(origins = "http://localhost")
    @RequestMapping(path = "/api/who_i_am", method = RequestMethod.GET, produces = "application/json")
    public UserData getWho(HttpSession httpSession, ModelMap model) {
        UserData data;
        //noinspection TryWithIdenticalCatches
        try {
            final String id = (String) httpSession.getAttribute("userLogin");
            data = accServ.getUserData(id);
        } catch (NullPointerException a){
            data = new UserData("", "");
        }
        catch (IllegalArgumentException a){
            data = new UserData("", "");
        }
        return data;
    }
    @CrossOrigin(origins = "http://localhost")
    @RequestMapping(path = "/api/logout", method = RequestMethod.GET)
    public void logOut(HttpSession httpSession) {
        httpSession.invalidate();
    }

    @RequestMapping(path = "/api/user/changeMail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponceCode changeMail(@RequestBody StringContainer str, HttpSession httpSession){
        boolean result = true;
        ApplicationContext context = new ClassPathXmlApplicationContext("local.xml");
        String msg = "ok";
        try{
            accServ.changeMail(str.getStrCont(), (String) httpSession.getAttribute("userLogin"));
        } catch (IllegalArgumentException a){
            result = false;
            msg = context.getMessage("msgs.error",new Object[] {28, "" },Locale.ENGLISH);
        } catch(NullPointerException b){
            msg = context.getMessage("msgs.error",new Object[] {28, "" },Locale.ENGLISH);
        }
        return new ResponceCode(result, msg);
    }

    @RequestMapping(path = "/api/user/changePass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponceCode changePass(@RequestBody PassForm form, HttpSession httpSession){
        boolean result = true;
        String msg = "ok";
        ApplicationContext context = new ClassPathXmlApplicationContext("local.xml");
        try{
            final String login = (String) httpSession.getAttribute("userLogin");
            if (accServ.checkPass(form.getOldPassHash(), login)) {
                accServ.changePassHash(form.getNewPassHash(), login);
            } else {
                result = false;
                //msg = "Wrong pass";
                msg = context.getMessage("msgs.invalid_password",new Object[] {28, "" },Locale.ENGLISH);
            }
        } catch (IllegalArgumentException a){
            result = false;
            msg = "Login? Which login?";
        }
        return new ResponceCode(result, msg);
    }

    public static final  class StringContainer{
        final  String strCont;
        public StringContainer(@JsonProperty("strCont") String strCont){
            this.strCont = strCont;
        }
        public String getStrCont(){return strCont;}
    }

    public static final  class PassForm{
        final String oldPassHash;
        final String newPassHash;
        @JsonCreator
        public PassForm(@JsonProperty("oldPassHash") String oldPassHash,
                        @JsonProperty("newPassHash") String newPassHash) {
            this.oldPassHash = oldPassHash;
            this.newPassHash = newPassHash;
        }

        public String getOldPassHash() {
            return oldPassHash;
        }
        public String getNewPassHash() {
            return  newPassHash;
        }
    }
    public static final class ResponceCode{
        final boolean signUpResult;
        final String errorMsg;
        @JsonCreator
        public ResponceCode(@JsonProperty("signUpResult") boolean loginResult,
                            @JsonProperty("errorMsg") String errorMsg){
            this.signUpResult = loginResult;
            this.errorMsg = errorMsg;
        }
        public boolean getSignUpResult(){
            return signUpResult;
        }
        public String getErrorMsg(){
            return errorMsg;
        }
    }
    public static final class UserData {
        final String userMail;
        final String userLogin;
        @JsonCreator
        public UserData(@JsonProperty("userMail") String userMail,
                        @JsonProperty("userLogin") String userLogin) {
            this.userLogin = userLogin;
            this.userMail = userMail;
        }

        public String getUserLogin() {
            return userLogin;
        }
        public String getUserMail() {
            return  userMail;
        }
    }
}