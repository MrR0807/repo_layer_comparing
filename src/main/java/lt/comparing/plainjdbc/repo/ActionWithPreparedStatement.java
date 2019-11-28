package lt.comparing.plainjdbc.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ActionWithPreparedStatement<T> {

    T action(PreparedStatement preparedStatement) throws SQLException;
}
