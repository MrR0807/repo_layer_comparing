package lt.comparing.plainjdbc.repo.employee;

import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.repo.JdbcHelper;
import lt.comparing.plainjdbc.repo.sqlfunction.InsertReturning;
import lt.comparing.plainjdbc.repo.sqlfunction.Select;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JdbcEmployeeRepo {

    private final JdbcHelper jdbcHelper;

    public JdbcEmployeeRepo(DataSource dataSource) {
        this.jdbcHelper = new JdbcHelper(dataSource);
    }

    public Optional<Employee> getEmployee(long employeeId) {
        String sql = SelectEmployee.preparedStatementSQL();
        Select<Employee> select = SelectEmployee.select(employeeId);

        return Optional.ofNullable(jdbcHelper.select(sql, select));
    }

    public Set<Employee> getEmployees() {
        String sql = SelectEmployees.preparedStatementSQL();
        Select<Set<Employee>> select = SelectEmployees.select(List.of());

        return jdbcHelper.select(sql, select);
    }

    public Optional<Employee> getEmployeeFullGraph(long employeeId) {
        String sql = SelectEmployeeFullGraph.preparedStatementSQL();
        Select<Employee> select = SelectEmployeeFullGraph.select(employeeId);

        return Optional.ofNullable(jdbcHelper.select(sql, select));
    }

    public Set<Employee> getEmployeesFullGraph() {
        String sql = SelectEmployeesFullGraph.preparedStatementSQL();
        Select<Set<Employee>> select = SelectEmployeesFullGraph.select();

        return jdbcHelper.select(sql, select);
    }

    public long saveEmployee(Employee employee) {
        String sql = InsertEmployee.preparedStatementSQL();
        InsertReturning<List<Long>> insert = InsertEmployee.insert(employee);
        List<Long> generatedKeys = jdbcHelper.insertReturnGeneratedKeys(sql, insert);
        return generatedKeys.get(0);
    }

    public void updateEmployee(Employee employee) {

    }
}