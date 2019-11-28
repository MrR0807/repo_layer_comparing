package lt.comparing.plainjdbc.repo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcHelper {

    private final DataSource dataSource;

    public JdbcHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T get(String select, ActionWithPreparedStatement<T> function) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(select)) {

            return function.action(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot find employee");
        }
    }
}
