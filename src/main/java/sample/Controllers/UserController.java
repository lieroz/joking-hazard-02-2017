package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import sample.Services.AccountService;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import java.util.Locale;

@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class UserController {
    @SuppressWarnings("unused")
    @NotNull
    final AccountService accServ;
    @SuppressWarnings("unused")
    private final MessageSource messageSource;

    @SuppressWarnings("unused")
    public UserController(@NotNull AccountService accountService, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.accServ = accountService;
    }

    @RequestMapping(path = "/api/who_i_am", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<UserData.UserInfo> getWho(HttpSession httpSession, ModelMap model) {
        final UserData.UserInfo data;
        final String id = (String) httpSession.getAttribute("userLogin");
        if (id != null) {
            return new ResponseEntity<UserData.UserInfo>( accServ.getUserData(id),HttpStatus.OK);
        }
        return new ResponseEntity<UserData.UserInfo>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/api/logout", method = RequestMethod.GET)
    public void logOut(HttpSession httpSession) {
        httpSession.invalidate();
    }

    @RequestMapping(path = "/api/user/changeMail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseCode changeMail(@RequestBody StringContainer str, HttpSession httpSession) {
        final ResponseCode resp;
        final String login = (String) httpSession.getAttribute("userLogin");
        if (login != null) {
            return accServ.changeMail(str.getStrCont(), login);
        }
        final boolean result = false;
        final String msg = messageSource.getMessage("msgs.error", null, Locale.ENGLISH);
        return new ResponseCode(false, msg);

    }

    @RequestMapping(path = "/api/user/changePass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseCode changePass(@RequestBody PassForm form, HttpSession httpSession) {
        final String msg;
        final ResponseCode resp;
        final String login = (String) httpSession.getAttribute("userLogin");
        if (login == null) {
            msg = messageSource.getMessage("msgs.invalid_session", null, Locale.ENGLISH);
            return new ResponseCode(false, msg);
        }
        if (!accServ.checkPass(form.getOldPassHash(), login).getResult()) {
            msg = messageSource.getMessage("msgs.invalid_password", null, Locale.ENGLISH);
            return new ResponseCode(false, msg);
        }
        return accServ.changePassHash(form.getNewPassHash(), login);
    }

    public static final class StringContainer {
        String strCont;

        @SuppressWarnings("unused")
        public StringContainer(@JsonProperty("strCont") String strCont) {
            this.strCont = strCont;
        }

        @SuppressWarnings("unused")
        public String getStrCont() {
            return strCont;
        }

        @SuppressWarnings("unused")
        public void setStrCont(String strCont) {
            this.strCont = strCont;
        }
    }

    public static final class PassForm {
        final String oldPassHash;
        final String newPassHash;

        @SuppressWarnings("unused")
        @JsonCreator
        public PassForm(@JsonProperty("oldPass") String oldPassHash,
                        @JsonProperty("newPass") String newPassHash) {
            this.oldPassHash = oldPassHash;
            this.newPassHash = newPassHash;
        }

        @SuppressWarnings("unused")
        public String getOldPassHash() {
            return oldPassHash;
        }

        @SuppressWarnings("unused")
        public String getNewPassHash() {
            return newPassHash;
        }
    }

}