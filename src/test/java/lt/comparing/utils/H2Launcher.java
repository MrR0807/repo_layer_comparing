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

public class H2Launcher {

    private Server server = null;
    private DataSource dataSource = H2DataSource.dataSource();

    public H2Launcher() {
        try {
            server = Server.createTcpServer().start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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