package lt.comparing.plainjdbc;

import lt.comparing.Repo;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepoJDBC implements Repo {

    private final static String SELECT_QUERY = "SELECT * FROM test WHERE last_name = 'goodbye'";

    @Override
    public String get() {
        var url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

        try (var con = DriverManager.getConnection(url);
            var ps = con.prepareStatement(SELECT_QUERY)) {

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("last_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
