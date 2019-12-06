package lt.comparing.plainjdbc.repo;

import javax.sql.DataSource;

public class JdbcEmployeeProjectRepo {

    private final DataSource dataSource;

    public JdbcEmployeeProjectRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
