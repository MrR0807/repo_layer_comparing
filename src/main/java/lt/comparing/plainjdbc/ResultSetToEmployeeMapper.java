package lt.comparing.plainjdbc;

import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.plainjdbc.entity.Project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.util.Objects.isNull;

public class ResultSetToEmployeeMapper {


    public static Employee toEmployeeWithProjects(ResultSet resultSet) throws SQLException {
        Employee employee = null;

        while (resultSet.next()) {
            if (isNull(employee)) {
                employee = toEmployee(resultSet);
            }

            var projectId = resultSet.getLong("p_id");
            var projectName = resultSet.getString("p_project_name");
            employee.addProject(new Project(projectId, projectName));
        }

        return employee;
    }

    public static Employee toEmployee(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("e_id");
        var firstName = resultSet.getString("e_first_name");
        var lastName = resultSet.getString("e_last_name");
        var salary = resultSet.getBigDecimal("e_salary");
        var employeeType = EmployeeType.valueOf(resultSet.getString("e_employee_type"));
        var listOfProjects = new ArrayList<Project>();
        return new Employee(id, firstName, lastName, salary, employeeType, null, listOfProjects);
    }
}
