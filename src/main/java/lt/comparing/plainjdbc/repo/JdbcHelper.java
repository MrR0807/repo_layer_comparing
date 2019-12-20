package lt.comparing.plainjdbc.repo;

import lt.comparing.plainjdbc.repo.sqlfunction.InsertReturning;
import lt.comparing.plainjdbc.repo.sqlfunction.Select;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcHelper {

    private final DataSource dataSource;

    public JdbcHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T select(String select, Select<T> function) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(select)) {

            return function.doInConnection(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not execute statement");
        }
    }

    public <T> List<Long> insertReturnGeneratedKeys(String insert, InsertReturning<T> function) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

            function.doInConnection(ps);
            return getGeneratedKeys(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not execute statement");
        }
    }

    private static List<Long> getGeneratedKeys(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            List<Long> generatedKeys = new ArrayList<>();
            while (rs.next()) {
                generatedKeys.add(rs.getLong(1));
            }
            return generatedKeys;
        }
    }
}
