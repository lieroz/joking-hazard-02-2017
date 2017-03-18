package sample.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.Services.AccountService;
import sample.Models.UserData;
import sample.Views.ResponseCode;
import sample.Views.UserDataView;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Locale;


//@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class SignUpController {
    private final MessageSource messageSource;
    @SuppressWarnings("unused")
    @NotNull
    final AccountService accServ;

    @SuppressWarnings("unused")
    public SignUpController(@NotNull AccountService accountService, @NotNull MessageSource messageSource) {
        this.messageSource = messageSource;
        this.accServ = accountService;
    }

    @RequestMapping(path = "/api/user/signup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseCode> getMsg(@RequestBody UserDataView body, HttpSession httpSession) {
        boolean resCode = false;
        String msg =  messageSource.getMessage("msgs.error", null, Locale.ENGLISH);
        UserDataView.ViewError viewRes = body.valid();
        if(viewRes != UserDataView.ViewError.OK){
            switch (viewRes){
                case INVALID_DATA_ERROR:
                    msg = messageSource.getMessage("msgs.invalid_auth_data", null, Locale.ENGLISH);
                    resCode = false;
                    break;
                default:
                    msg = messageSource.getMessage("msgs.error", null, Locale.ENGLISH);
                    resCode = false;
            }
            return new ResponseEntity<ResponseCode>(new ResponseCode(resCode,msg), HttpStatus.OK);
        }
        UserData body_model = body.getData();
        AccountService.ErrorCodes result = accServ.register(body_model);
        switch (result){
            case INVALID_LOGIN:
                resCode = false;
                msg =  messageSource.getMessage("msgs.invalid_auth_data", null, Locale.ENGLISH);
                break;
            case LOGIN_OCCUPIED:
                resCode = false;
                msg = messageSource.getMessage("msgs.login_occupied", null, Locale.ENGLISH);
                break;
            case OK:
                resCode = true;
                msg = messageSource.getMessage("msgs.ok", null, Locale.ENGLISH);
                httpSession.setAttribute("userLogin", body.getUserLogin());
        }
        return new ResponseEntity<ResponseCode>(new ResponseCode(resCode,msg), HttpStatus.OK);
    }
}
