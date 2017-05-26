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
        final String checkSql = "SELECT COUNT(*) FROM cards WHERE url = ?";
        if (jdbcTemplate.queryForObject(checkSql, new Object[]{url}, Integer.class) == 0) {
            final String insertSql = "INSERT INTO cards (url) VALUES (?)";
            jdbcTemplate.update(insertSql, url);
        }
    }

    public List<CardView> getDeck() {
        final String sql = "SELECT * FROM cards";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CardView(rs.getInt("id"), rs.getString("url")));
    }
}
