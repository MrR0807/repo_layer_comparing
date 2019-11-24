package lt.comparing.plainjdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ActionWithPreparedStatement<T> {

    T inPreparedStatement(PreparedStatement preparedStatement) throws SQLException;
}
