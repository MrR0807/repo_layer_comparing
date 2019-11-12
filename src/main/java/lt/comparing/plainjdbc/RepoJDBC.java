package lt.comparing.plainjdbc;

import lt.comparing.Repo;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepoJDBC implements Repo {

    private final static String SELECT_QUERY = "SELECT * FROM company.employee e WHERE e.id = ?";

    private final DataSource dataSource;

    public RepoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Employee getEmployeesFullGraph(long employeeId) {
        return null;
    }

    @Override
    public Employee getEmployee(long employeeId) {
        try (var connection = dataSource.getConnection();
             var ps = connection.prepareStatement(SELECT_QUERY)) {

            ps.setLong(1, employeeId);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var firstName = resultSet.getString("first_name");
                var lastName = resultSet.getString("last_name");
                var salary = resultSet.getBigDecimal("salary");
                var employeeType = EmployeeType.valueOf(resultSet.getString("employee_type"));

                return new Employee(id, firstName, lastName, salary, employeeType, null);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Cannot find employee");
    }
}
