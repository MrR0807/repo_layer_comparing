package lt.comparing.plainjdbc.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface InsertWithId<T> {

    long action(PreparedStatement preparedStatement, T t) throws SQLException;
}
