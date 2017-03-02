package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ApplicationContext;


import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import java.util.Locale;


@RestController
public class UserController {
    @NotNull
    final AccountService accServ;
    public UserController(@NotNull AccountService accountService){
        this.accServ = accountService;
    }
    //@CrossOrigin(origins = "http://localhost")
    @RequestMapping(path = "/api/who_i_am", method = RequestMethod.GET, produces = "application/json")
    public UserData.UserInfo getWho(HttpSession httpSession, ModelMap model) {
        final UserData.UserInfo data;
        final String id = (String) httpSession.getAttribute("userLogin");
        if(id != null){
            data = accServ.getUserData(id);
        } else {
            data = new UserData.UserInfo("", "");
        }
        return data;
    }
    //@CrossOrigin(origins = "http://localhost")
    @RequestMapping(path = "/api/logout", method = RequestMethod.GET)
    public void logOut(HttpSession httpSession) {
        httpSession.invalidate();
    }

    //@CrossOrigin(origins = "http://localhost")
    @RequestMapping(path = "/api/user/changeMail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponceCode changeMail(@RequestBody StringContainer str, HttpSession httpSession){
        final ApplicationContext context = new ClassPathXmlApplicationContext("local.xml");
        final ResponceCode resp;
        final String login = (String) httpSession.getAttribute("userLogin");
        if (login != null){
           resp = accServ.changeMail(str.getStrCont(), login );
        } else {
            final boolean result = false;
            final String msg = context.getMessage("msgs.error", null, Locale.ENGLISH);
            resp = new ResponceCode(false, msg);
        }
        return resp;
    }

    //@CrossOrigin(origins = "http://localhost")
    @RequestMapping(path = "/api/user/changePass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponceCode changePass(@RequestBody PassForm form, HttpSession httpSession) {
        final String msg;
        final ApplicationContext context = new ClassPathXmlApplicationContext("local.xml");
        final ResponceCode resp;
        final String login = (String) httpSession.getAttribute("userLogin");
        if (login != null) {
            if (accServ.checkPass(form.getOldPassHash(), login).getResult()) {
                resp = accServ.changePassHash(form.getNewPassHash(), login);
            } else {
                msg = context.getMessage("msgs.invalid_password",null, Locale.ENGLISH);
                resp = new ResponceCode(false, msg);
            }
        } else {
            msg = "Login? Which login?";
            resp = new ResponceCode(false, msg);
        }
        return resp;
    }

    public static final  class StringContainer{
        String strCont;
        @SuppressWarnings("unused")
        public StringContainer(@JsonProperty("strCont") String strCont){
            this.strCont = strCont;
        }
        public String getStrCont(){return strCont;}
        @SuppressWarnings("unused")
        public void setStrCont(String strCont){this.strCont = strCont;}
    }

    public static final  class PassForm{
        final String oldPassHash;
        final String newPassHash;
        @SuppressWarnings("unused")
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

}