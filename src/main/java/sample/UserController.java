package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import java.util.Locale;


@RestController
public class UserController {
    @NotNull
    final AccountService accServ;
    private final MessageSource messageSource;
    public UserController(@NotNull AccountService accountService, MessageSource messageSource){
        this.messageSource = messageSource;
        this.accServ = accountService;
    }
    @CrossOrigin(origins = "http://jokinghazard.herokuapp.com")
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
    @CrossOrigin(origins = "http://jokinghazard.herokuapp.com")
    @RequestMapping(path = "/api/logout", method = RequestMethod.GET)
    public void logOut(HttpSession httpSession) {
        httpSession.invalidate();
    }

    @CrossOrigin(origins = "http://jokinghazard.herokuapp.com")
    @RequestMapping(path = "/api/user/changeMail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponceCode changeMail(@RequestBody StringContainer str, HttpSession httpSession){
        final ResponceCode resp;
        final String login = (String) httpSession.getAttribute("userLogin");
        if (login != null){
           resp = accServ.changeMail(str.getStrCont(), login );
        } else {
            final boolean result = false;
            final String msg = messageSource.getMessage("msgs.error",null, Locale.ENGLISH);
            resp = new ResponceCode(false, msg);
        }
        return resp;
    }

    @CrossOrigin(origins = "http://jokinghazard.herokuapp.com")
    @RequestMapping(path = "/api/user/changePass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponceCode changePass(@RequestBody PassForm form, HttpSession httpSession) {
        final String msg;
        final ResponceCode resp;
        final String login = (String) httpSession.getAttribute("userLogin");
        if (login != null) {
            if (accServ.checkPass(form.getOldPassHash(), login).getResult()) {
                resp = accServ.changePassHash(form.getNewPassHash(), login);
            } else {
                msg = messageSource.getMessage("msgs.invalid_session", null, Locale.ENGLISH);
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