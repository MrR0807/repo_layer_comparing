package lt.comparing.plainjdbc.repo.employeeproject;

import javax.sql.DataSource;

public class JdbcEmployeeProjectRepo {

    private final DataSource dataSource;

    public JdbcEmployeeProjectRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
