package sample;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class SignUpController {
    @SuppressWarnings("unused")
    @NotNull
    final AccountService accServ;

    @SuppressWarnings("unused")
    public SignUpController(@NotNull AccountService accountService) {
        this.accServ = accountService;
    }

    @RequestMapping(path = "/api/user/signup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseCode getMsg(@RequestBody UserData body, HttpSession httpSession) {
        final ResponseCode result = accServ.register(body);
        if (result.getResult()) {
            httpSession.setAttribute("userLogin", body.getUserLogin());
        }
        return result;
    }
}
