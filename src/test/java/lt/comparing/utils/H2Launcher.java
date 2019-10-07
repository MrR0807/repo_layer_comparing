package lt.comparing.utils;

import org.h2.tools.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class H2Launcher {

    private Server server = null;

    public H2Launcher() {
        try {
            server = Server.createTcpServer().start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void launch() {
        var url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

        try (Connection con = DriverManager.getConnection(url)) {
            con.setAutoCommit(false);
            Statement stm = con.createStatement();
            stm.execute(this.loadSQL("schema.sql"));
            stm.execute(this.loadSQL("data.sql"));
            con.commit();

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