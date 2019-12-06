package lt.comparing.plainjdbc.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Insert<T> {

    void action(PreparedStatement preparedStatement, T t) throws SQLException;
}
