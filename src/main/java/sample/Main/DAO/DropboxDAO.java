package sample.Main.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import sample.Main.Views.CardView;

import java.util.List;

/**
 * Created by lieroz on 26.05.17.
 */
public class DropboxDAO {
    private final JdbcTemplate jdbcTemplate;

    public DropboxDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addCard(final String url) {
        final String sql = "INSERT INTO cards (url) VALUES (?)";
        jdbcTemplate.update(sql, url);
    }

    public List<CardView> getDeck() {
        final String sql = "SELECT * FROM cards";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CardView(rs.getInt("id"), rs.getString("url")));
    }
}
