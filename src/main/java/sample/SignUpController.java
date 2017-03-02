package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@SuppressWarnings("ALL")
@RestController
public class SignUpController {
    @NotNull
    final AccountService accServ;
    public SignUpController(@NotNull AccountService accountService){
        this.accServ = accountService;
    }
    //@CrossOrigin(origins = "http://localhost")
    @RequestMapping(path = "/api/user/signup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public  ResponceCode getMsg(@RequestBody SignUpData body, HttpSession httpSession) {
        System.out.println("SignUpCalled");
        final ResponceCode result = accServ.register(body);
        if(result.getResult()){
            httpSession.setAttribute("userLogin", body.getUserLogin());
        }
        return result;
    }
    public static final class SignUpData{
        final String userLogin;
        String userMail;
        String passHash;
        @JsonCreator
        public  SignUpData(@JsonProperty("userMail") String userMail,
                           @JsonProperty("userLogin") String userLogin,
                           @JsonProperty("passHash") String passHash){
            this.userLogin = userLogin;
            this.passHash = passHash;
            this.userMail = userMail;
        }
        public String getUserLogin() {
            return userLogin;
        }
        public String getPassHash() {
            return  passHash;
        }
        public String getUserMail() {
            return  userMail;
        }
        public void setUserMail(@NotNull String userMail) {
            this.userMail = userMail;
        }
        public void setPassHash(@NotNull String passHash) {
            this.passHash = passHash;
        }
    }
}
