package lt.comparing.plainjdbc;

import lt.comparing.Repo;
import lt.comparing.config.H2DataSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepoJDBC implements Repo {

    private final static String SELECT_QUERY = "SELECT * FROM test WHERE last_name = 'goodbye'";

    private final DataSource dataSource = H2DataSource.dataSource();

    @Override
    public String get() {
        try (var con = dataSource.getConnection();
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
