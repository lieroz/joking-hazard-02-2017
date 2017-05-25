package sample.Main.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/api/resources", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseCode> getCardsDeck() {
        List<CardView> deck = jdbcTemplate.query("SELECT * FROM cards", (rs, rowNum) ->
                new CardView(rs.getInt("id"), rs.getString("url")));
        return new ResponseEntity<>(new ResponseCode<>(true,
                messageSource.getMessage("msgs.ok", null, Locale.ENGLISH),
                deck), HttpStatus.OK);
    }
}
