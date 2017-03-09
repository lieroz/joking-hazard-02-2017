package sample;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
public class SignUpController {
    @NotNull
    final AccountService accServ;
    public SignUpController(@NotNull AccountService accountService){
        this.accServ = accountService;
    }
    @CrossOrigin(origins = "http://jokinghazard.herokuapp.com")
    @RequestMapping(path = "/api/user/signup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public  ResponceCode getMsg(@RequestBody UserData body, HttpSession httpSession) {
        final ResponceCode result = accServ.register(body);
        if(result.getResult()){
            httpSession.setAttribute("userLogin", body.getUserLogin());
        }
        return result;
    }
}
