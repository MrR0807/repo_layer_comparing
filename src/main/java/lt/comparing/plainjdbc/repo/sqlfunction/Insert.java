package lt.comparing.plainjdbc.repo.sqlfunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Insert {

    void doInConnection(PreparedStatement preparedStatement) throws SQLException;
}
