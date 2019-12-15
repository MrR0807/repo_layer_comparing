package lt.comparing.plainjdbc.repo;

import lt.comparing.plainjdbc.repo.sqlfunction.Select;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DoInConnection {

    private final DataSource dataSource;

    public DoInConnection(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T select(String sql, Select<T> extractor) {
        Connection conn = null;
        PreparedStatement ps = null;
        T result = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            result = extractor.action(ps);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.rollback(conn);
        } finally {
            JdbcUtil.closeStatement(ps);
            JdbcUtil.closeConnection(conn);
        }
        return result;
    }
}
