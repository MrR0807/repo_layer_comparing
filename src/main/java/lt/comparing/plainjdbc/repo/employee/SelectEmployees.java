package lt.comparing.plainjdbc.repo.employee;

import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.sqlfunction.Select;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class SelectEmployees {

    private static final String SELECT_EMPLOYEES = """
            SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary,
            e.employee_type e_employee_type
            FROM company.employee e""";

    public static String preparedStatementSQL() {
        return SELECT_EMPLOYEES;
    }

    public static Select<Set<Employee>> select(Collection<Employee> employees) {
        return SelectEmployees::extractEmployees;
    }

    private static Set<Employee> extractEmployees(PreparedStatement ps) throws SQLException {
        ResultSet resultSet = ps.executeQuery();
        Set<Employee> employeeSet = new LinkedHashSet<>();

        while (resultSet.next()) {
            var employee = toEmployee(resultSet);
            employeeSet.add(employee);
        }

        return employeeSet;
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