package lt.comparing.plainjdbc.repo.sqlfunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface InsertReturning<T, R> {

    R action(PreparedStatement preparedStatement, T t) throws SQLException;
}
