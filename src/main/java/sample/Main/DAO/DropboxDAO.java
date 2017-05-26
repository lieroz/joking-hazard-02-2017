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

    public void addCard(final String jpgUrl, final String webpUrl) {
        final String checkSql = "SELECT COUNT(*) FROM cards WHERE jpg_url = ? AND webp_url = ?";
        if (jdbcTemplate.queryForObject(checkSql, new Object[]{jpgUrl, webpUrl}, Integer.class) == 0) {
            final String insertSql = "INSERT INTO cards (jpg_url, webp_url) VALUES (?, ?)";
            jdbcTemplate.update(insertSql, jpgUrl, webpUrl);
        }
    }

    public List<CardView> getDeckJpg() {
        final String sql = "SELECT id, jpg_url FROM cards";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CardView(rs.getInt("id"), rs.getString("jpg_url")));
    }

    public List<CardView> getDeckWebp() {
        final String sql = "SELECT id, webp_url FROM cards";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CardView(rs.getInt("id"), rs.getString("webp_url")));
    }
}
