package lt.comparing.plainjdbc.repo;

import lt.comparing.plainjdbc.repo.sqlfunction.Insert;
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

            return function.action(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not execute statement");
        }
    }

    public <T, R> R insert(String insert, InsertReturning<T, R> function, T t) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

            return function.action(ps, t);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not execute statement");
        }
    }

    public <T> List<Long> insertReturnGeneratedKeys(String insert, Insert<T> function, T t) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

            function.action(ps, t);

            return getGeneratedLeys(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not execute statement");
        }
    }

    private static List<Long> getGeneratedLeys(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            List<Long> generatedKeys = new ArrayList<>();
            while (rs.next()) {
                generatedKeys.add(rs.getLong(1));
            }
            return generatedKeys;
        }
    }

    public <T> void insert(String insert, Insert<T> function, T t) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(insert)) {

            function.action(ps, t);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not execute statement");
        }
    }

    public <T> void selectUpdatable(String insert, Insert<T> function, T t) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(insert, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {

            function.action(ps, t);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not execute statement");
        }
    }
}
