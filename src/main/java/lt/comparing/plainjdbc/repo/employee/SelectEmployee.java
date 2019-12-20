package lt.comparing.plainjdbc.repo.employee;

import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.sqlfunction.Select;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SelectEmployee {

    private static final String SELECT_EMPLOYEE = """
            SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary,
            e.employee_type e_employee_type
            FROM company.employee e WHERE e.id = ?""";

    public static String preparedStatementSQL() {
        return SELECT_EMPLOYEE;
    }

    public static Select<Employee> select(Long employeeId) {
        return ps -> extractEmployee(employeeId, ps);
    }

    private static Employee extractEmployee(Long employeeId, PreparedStatement ps) throws SQLException {
        ps.setLong(1, employeeId);
        ResultSet resultSet = ps.executeQuery();

        return resultSet.next() ? toEmployee(resultSet) : null;
    }

    private static Employee toEmployee(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("e_id");
        var firstName = resultSet.getString("e_first_name");
        var lastName = resultSet.getString("e_last_name");
        var salary = resultSet.getBigDecimal("e_salary");
        var employeeType = EmployeeType.valueOf(resultSet.getString("e_employee_type"));
        var listOfProjects = new ArrayList<Project>();
        return new Employee(id, firstName, lastName, salary, employeeType, null, listOfProjects);
    }
}