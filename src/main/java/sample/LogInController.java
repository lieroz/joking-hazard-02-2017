package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@SuppressWarnings("ALL")
@RestController
public class LogInController {
    @NotNull
    final AccountService accServ;
    public LogInController(@NotNull AccountService accountService){
        this.accServ = accountService;
    }
    @RequestMapping(path = "/api/user/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public  ResponceCode getMsg(@RequestBody LogInData body, HttpSession httpSession) {
        System.out.println("GetCalled");
        final ResponceCode resp = accServ.login(body);
        if (resp.getloginResult())  {
            httpSession.setAttribute("userLogin", body.getUserLogin());
        }
        return resp;
    }
    public static final class LogInData{
        final String userLogin;
        final String passHash;
        @JsonCreator
        public  LogInData(@JsonProperty("userId") String userLogin, @JsonProperty("passHash") String passHash){
            this.userLogin = userLogin;
            this.passHash = passHash;
        }
        public String getUserLogin() {
            return userLogin;
        }
        public String getPassHash() {
            return  passHash;
        }
    }
    public static final class ResponceCode{
        final boolean loginResult;
        @JsonCreator
        public ResponceCode(@JsonProperty("loginResult") boolean loginResult){
            this.loginResult = loginResult;
        }
        public boolean getloginResult(){
            return loginResult;
        }
    }
}
