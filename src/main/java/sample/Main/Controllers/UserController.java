package sample.Main.Controllers;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.Main.Services.AccountService;
import sample.Main.Views.MailForm;
import sample.Main.Views.PassForm;
import sample.Main.Views.ResponseCode;
import sample.Main.Views.UserInfoView;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class UserController {
    @NotNull
    private final MessageSource messageSource;

    @NotNull
    private final AccountService accountService;

    public UserController(@NotNull AccountService accountService, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.accountService = accountService;
    }

    @RequestMapping(path = "/api/user/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseCode<UserInfoView>> getWho(HttpSession httpSession) {
        final UserInfoView data = new UserInfoView();
        final String id = (String) httpSession.getAttribute("userLogin");

        if (id == null) {
            return new ResponseEntity<>(new ResponseCode<>(false,
                    messageSource.getMessage("msgs.not_found", null, Locale.ENGLISH)),
                    HttpStatus.NOT_FOUND);
        }

        final AccountService.ErrorCodes error = accountService.getUserView(id, data);

        //noinspection EnumSwitchStatementWhichMissesCases
        switch (error) {

            case INVALID_LOGIN: {
                return new ResponseEntity<>(new ResponseCode<>(false,
                        messageSource.getMessage("msgs.forbidden", null, Locale.ENGLISH)),
                        HttpStatus.FORBIDDEN);
            }

            case OK: {
                return new ResponseEntity<>(new ResponseCode<>(true,
                        messageSource.getMessage("msgs.ok", null, Locale.ENGLISH),
                        data), HttpStatus.OK);
            }

            default: {
                return new ResponseEntity<>(new ResponseCode<>(false,
                        messageSource.getMessage("msgs.internal_server_error", null, Locale.ENGLISH)),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping(path = "/api/user/logout", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseCode> logOut(HttpSession httpSession) {
        final String login = (String) httpSession.getAttribute("userLogin");

        if (login == null) {
            return new ResponseEntity<>(new ResponseCode(false,
                    messageSource.getMessage("msgs.not_found", null, Locale.ENGLISH)),
                    HttpStatus.NOT_FOUND);
        }

        httpSession.invalidate();
        return new ResponseEntity<>(new ResponseCode(true,
                messageSource.getMessage("msgs.ok", null, Locale.ENGLISH)),
                HttpStatus.OK);
    }

    @RequestMapping(path = "/api/user/changeMail", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseCode> changeMail(@RequestBody MailForm body, HttpSession httpSession) {
        if (body.getUserMail() == null) {
            return new ResponseEntity<>(new ResponseCode(false,
                    messageSource.getMessage("msgs.bad_request", null, Locale.ENGLISH)),
                    HttpStatus.BAD_REQUEST);
        }

        final String login = (String) httpSession.getAttribute("userLogin");
        final AccountService.ErrorCodes result = accountService.changeMail(body.getUserMail(), login);

        //noinspection EnumSwitchStatementWhichMissesCases
        switch (result) {

            case INVALID_SESSION: {
                return new ResponseEntity<>(new ResponseCode(false,
                        messageSource.getMessage("msgs.not_found", null, Locale.ENGLISH)),
                        HttpStatus.NOT_FOUND);
            }

            case OK: {
                return new ResponseEntity<>(new ResponseCode(true,
                        messageSource.getMessage("msgs.ok", null, Locale.ENGLISH)),
                        HttpStatus.OK);
            }

            default: {
                return new ResponseEntity<>(new ResponseCode(false,
                        messageSource.getMessage("msgs.internal_server_error", null, Locale.ENGLISH)),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping(path = "/api/user/changePass", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseCode> changePass(@RequestBody PassForm form, HttpSession httpSession) {
        final String login = (String) httpSession.getAttribute("userLogin");

        if (login == null) {
            return new ResponseEntity<>(new ResponseCode(false,
                    messageSource.getMessage("msgs.not_found", null, Locale.ENGLISH)),
                    HttpStatus.NOT_FOUND);
        }

        if (form.getOldPassHash() == null || form.getNewPassHash() == null) {
            return new ResponseEntity<>(new ResponseCode(false,
                    messageSource.getMessage("msgs.bad_request", null, Locale.ENGLISH)),
                    HttpStatus.BAD_REQUEST);
        }

        if (!accountService.checkPass(form.getOldPassHash(), login)) {
            return new ResponseEntity<>(new ResponseCode(false,
                    messageSource.getMessage("msgs.forbidden", null, Locale.ENGLISH)),
                    HttpStatus.FORBIDDEN);
        }

        final AccountService.ErrorCodes error = accountService.changePassHash(form.getNewPassHash(), login);

        //noinspection EnumSwitchStatementWhichMissesCases
        switch (error) {

            case OK: {
                return new ResponseEntity<>(new ResponseCode(true,
                        messageSource.getMessage("msgs.ok", null, Locale.ENGLISH)),
                        HttpStatus.OK);
            }

            default: {
                return new ResponseEntity<>(new ResponseCode(false,
                        messageSource.getMessage("msgs.internal_server_error", null, Locale.ENGLISH)),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping(path = "/api/user/delete", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseCode> deleteUser(HttpSession httpSession) {
        final String login = (String) httpSession.getAttribute("userLogin");

        if (login == null) {
            return new ResponseEntity<>(new ResponseCode(false,
                    messageSource.getMessage("msgs.not_found", null, Locale.ENGLISH)),
                    HttpStatus.NOT_FOUND);
        }

        final AccountService.ErrorCodes error = accountService.deleteUserData(login);

        //noinspection EnumSwitchStatementWhichMissesCases
        switch (error) {

            case OK: {
                httpSession.invalidate();
                return new ResponseEntity<>(new ResponseCode(true,
                        messageSource.getMessage("msgs.ok", null, Locale.ENGLISH)),
                        HttpStatus.OK);
            }

            default: {
                return new ResponseEntity<>(new ResponseCode(false,
                        messageSource.getMessage("msgs.internal_server_error", null, Locale.ENGLISH)),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping(path = "/api/score", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getScoreBoard(HttpSession httpSession) {
        final String login = (String) httpSession.getAttribute("userLogin");
        if (login == null) {
            return new ResponseEntity<>(new ResponseCode(false,
                    messageSource.getMessage("msgs.not_found", null, Locale.ENGLISH)),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseCode<>(true,
                messageSource.getMessage("msgs.ok", null, Locale.ENGLISH),
                accountService.getScoreBoard(login)), HttpStatus.OK);
    }
}