package main;

import org.h2.tools.Server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.stream.Collectors;

public class MainStart {

    public static void main(String[] args) throws SQLException {
        MainStart mainStart = new MainStart();

        Server server = Server.createTcpServer(args).start();
        var url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

        try (Connection con = DriverManager.getConnection(url)) {
            Statement stm = con.createStatement();

            stm.execute(mainStart.getSchema());

            stm.execute(mainStart.getData());
            ResultSet persons = stm.executeQuery("SELECT * FROM test");

            while (persons.next()) {
                var lastName = persons.getString("last_name");
                System.out.println(lastName);
            }

        } finally {
            server.stop();
        }
    }

    private String getSchema() {
        try {
            URI resource = getClass().getClassLoader().getResource("schema.sql").toURI();

            return String.join("", Files.readAllLines(Path.of(resource)));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load file");
        }
    }

    private String getData() {
        try {
            URI resource = getClass().getClassLoader().getResource("data.sql").toURI();
            return String.join("", Files.readAllLines(Path.of(resource)));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load file");
        }
    }
}