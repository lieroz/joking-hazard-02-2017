package sample.Main.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sample.Main.DAO.DropboxDAO;
import sample.Main.Views.CardView;
import sample.Main.Views.ResponseCode;

import java.util.List;
import java.util.Locale;

/**
 * Created by lieroz on 26.05.17.
 */
@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class ResourceController {
    @Autowired
    private MessageSource messageSource;

    private DropboxDAO dropboxDAO;

    public ResourceController(final JdbcTemplate jdbcTemplate) {
        dropboxDAO = new DropboxDAO(jdbcTemplate);
    }

    @RequestMapping(value = "/api/resources/jpg", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseCode> getCardsDeckJpg() {
        List<CardView> deck;
        try {
            deck = dropboxDAO.getDeckJpg();
        } catch (DataAccessException ex) {
            return new ResponseEntity<>(new ResponseCode<>(false,
                    messageSource.getMessage("dropbox.bad", null, Locale.ENGLISH)),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseCode<>(true,
                messageSource.getMessage("msgs.ok", null, Locale.ENGLISH),
                deck), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/resources/webp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseCode> getCardsDeckWebp() {
        List<CardView> deck;
        try {
            deck = dropboxDAO.getDeckWebp();
        } catch (DataAccessException ex) {
            return new ResponseEntity<>(new ResponseCode<>(false,
                    messageSource.getMessage("dropbox.bad", null, Locale.ENGLISH)),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseCode<>(true,
                messageSource.getMessage("msgs.ok", null, Locale.ENGLISH),
                deck), HttpStatus.OK);
    }
}
