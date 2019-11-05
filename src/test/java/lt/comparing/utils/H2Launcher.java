package lt.comparing.utils;

import lt.comparing.plainjdbc.JDBCUtil;
import org.h2.tools.Server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class H2Launcher {

    private Server server = null;
    private static final String URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    public H2Launcher() {
        try {
            server = Server.createTcpServer().start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initDatabase() {
        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();
            var stm = connection.createStatement();

            stm.execute(this.loadSQL("schema.sql"));
            stm.execute(this.loadSQL("data.sql"));
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JDBCUtil.rollback(connection);
        } finally {
            JDBCUtil.closeConnection(connection);
        }
    }

    public void restart() {
        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();
            var stm = connection.createStatement();

            stm.execute(this.loadSQL("clear.sql"));
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JDBCUtil.rollback(connection);
        } finally {
            JDBCUtil.closeConnection(connection);
        }
    }

    public void close() {
        server.shutdown();
    }

    private String loadSQL(String resourceName) {
        try {
            URI resource = getClass().getClassLoader().getResource(resourceName).toURI();
            return String.join("", Files.readAllLines(Path.of(resource)));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load file");
        }
    }
}