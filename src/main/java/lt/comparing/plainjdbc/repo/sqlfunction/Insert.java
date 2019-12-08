package lt.comparing.plainjdbc.repo.sqlfunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Insert<T> {

    void action(PreparedStatement preparedStatement, T t) throws SQLException;
}
