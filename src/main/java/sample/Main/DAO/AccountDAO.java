package sample.Main.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import sample.Main.Models.UserData;

/**
 * Created by lieroz on 26.03.17.
 */


@SuppressWarnings("DefaultFileTemplate")
public final class AccountDAO {
    private final JdbcTemplate jdbcTemplate;

    public AccountDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserData getUserByLogin(final String login) {
        final String sql = "select * from users where login = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{login}, (rs, rowNum) ->
                new UserData(
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("password")
                )
        );
    }

    /**
     * a1a2a
     * b1b2b
     * a1b2b
     * b1b2b
     *
     * @param userData
     */
    @SuppressWarnings("JavaDoc")
    public void insertUserIntoDb(final UserData userData) {
        final String sql = "insert into users (login, email, password) values(?, ?, ?)";
        jdbcTemplate.update(sql, userData.getUserLogin(),
                userData.getUserMail(), userData.getPassHash());
    }

    public void changeUserMail(final UserData userData) {
        final String sql = "update users set email = ? where login = ?";
        jdbcTemplate.update(sql, userData.getUserMail(),
                userData.getUserLogin());
    }

    public void changeUserPass(final UserData userData) {
        final String sql = "update users set password = ? where login = ?";
        jdbcTemplate.update(sql, userData.getPassHash(),
                userData.getUserLogin());
    }

    public void deleteUserFromDb(final String login) {
        final String sql = "delete from users where login = ?";
        jdbcTemplate.update(sql, login);
    }
}
