package lt.comparing.utils;

import lt.comparing.config.H2DataSource;
import org.h2.tools.Server;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

/**
 * To launch web console as well use startWebServer() and connect to:
 * localhost:8082
 * jdbc:h2:mem:test
 * username: leave blank
 * password: leave blank
 */
public class H2Launcher {

    private DataSource dataSource = H2DataSource.dataSource();

    public static H2Launcher startWebServer() {
        try {
            Server.createWebServer().start();
            return new H2Launcher();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Could not start H2 database");
    }

    public static H2Launcher startTcpServer() {
        try {
            Server.createTcpServer().start();
            return new H2Launcher();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Could not start H2 database");
    }

    private H2Launcher() {
    }

    public void initDatabase() {
        try (var connection = dataSource.getConnection();
             var stm = connection.createStatement()) {
            connection.setAutoCommit(false);

            stm.execute(this.loadSQL("schema.sql"));
            stm.execute(this.loadSQL("data.sql"));
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        try (var connection = dataSource.getConnection();
             var stm = connection.createStatement()) {

            stm.execute(this.loadSQL("clear.sql"));
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String loadSQL(String resourceName) {
        try {
            URI resource = H2Launcher.class.getResource("/resources/" + resourceName).toURI();
            return String.join("", Files.readAllLines(Path.of(resource)));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load file");
        }
    }
}