package lt.comparing.plainjdbc.repo.sqlfunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface InsertReturning2<R> {

    R action(PreparedStatement preparedStatement) throws SQLException;
}