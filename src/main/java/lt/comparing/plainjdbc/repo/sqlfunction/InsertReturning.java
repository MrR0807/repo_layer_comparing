package lt.comparing.plainjdbc.repo.sqlfunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface InsertReturning<R> {

    R doInConnection(PreparedStatement preparedStatement) throws SQLException;
}