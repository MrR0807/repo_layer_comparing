package lt.comparing.plainjdbc.repo.sqlfunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Select<T> {

    T doInConnection(PreparedStatement preparedStatement) throws SQLException;
}
