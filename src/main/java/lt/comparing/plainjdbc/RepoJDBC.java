package lt.comparing.plainjdbc;

import lt.comparing.Repo;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.plainjdbc.entity.Project;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.util.Objects.isNull;

public class RepoJDBC implements Repo {

    private static final String SELECT_EMPLOYEE = "" +
            "SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary, " +
            "e.employee_type e_employee_type " +
            "FROM company.employee e WHERE e.id = ?";
    private static final String SELECT_EMPLOYEE_AND_PROJECTS = "" +
            "SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary, e.employee_type e_employee_type, " +
            "p.id p_id, p.project_name p_project_name " +
            "FROM company.employee e " +
            "INNER JOIN company.employee_project ep ON e.id = ep.employee_id " +
            "INNER JOIN company.project p ON ep.project_id = p.id " +
            "WHERE e.id = ?";

    private final DataSource dataSource;

    public RepoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Employee getEmployee(long employeeId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_EMPLOYEE)) {

            ps.setLong(1, employeeId);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return toEmployee(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Cannot find employee");
    }

    @Override
    public Employee getEmployeeFullGraph(long employeeId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_EMPLOYEE_AND_PROJECTS)) {

            ps.setLong(1, employeeId);
            ResultSet resultSet = ps.executeQuery();

            return toEmployeeWithProjects(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Cannot find employee");
    }

    private Employee toEmployeeWithProjects(ResultSet resultSet) throws SQLException {
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

    private Employee toEmployee(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("e_id");
        var firstName = resultSet.getString("e_first_name");
        var lastName = resultSet.getString("e_last_name");
        var salary = resultSet.getBigDecimal("e_salary");
        var employeeType = EmployeeType.valueOf(resultSet.getString("e_employee_type"));
        var listOfProjects = new ArrayList<Project>();
        return new Employee(id, firstName, lastName, salary, employeeType, null, listOfProjects);
    }

    @Override
    public Project getProject(long projectId) {
        return null;
    }
}
