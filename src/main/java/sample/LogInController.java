package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;


@RestController
public class LogInController {
    @NotNull
    final AccountService accServ;
    public LogInController(@NotNull AccountService accountService){
        this.accServ = accountService;
    }
    @CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
    @RequestMapping(path = "/api/user/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public  ResponceCode getMsg(@RequestBody LogInData body, HttpSession httpSession) {
        final ResponceCode resp = accServ.login(body);
        if (resp.getResult())  {
            httpSession.setAttribute("userLogin", body.getUserLogin());
        }
        return resp;
    }
    public static final class LogInData{
        final String userLogin;
        final String passHash;
        @SuppressWarnings("unused")
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
}
