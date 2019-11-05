package lt.comparing.plainjdbc;

import lt.comparing.config.HikariConfiguration;

import javax.sql.DataSource;
import java.sql.*;

public class JDBCUtil {

    private static final DataSource dataSource = HikariConfiguration.dataSource();

    public static Connection getConnection() throws SQLException {
        // Get a connection
        Connection conn = dataSource.getConnection();

        // Set the auto-commit off
        conn.setAutoCommit(false);

        return conn;
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void commit(Connection conn) {
        try {
            if (conn != null) {
                conn.commit();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}