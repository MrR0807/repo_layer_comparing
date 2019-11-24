package lt.comparing.plainjdbc;

import lt.comparing.Repo;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.Project;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static lt.comparing.plainjdbc.EmployeeSQLStatements.*;
import static lt.comparing.plainjdbc.ResultSetToEmployeeMapper.toEmployee;
import static lt.comparing.plainjdbc.ResultSetToEmployeeMapper.toEmployeeWithProjects;

public class RepoJDBC implements Repo {

    private final DataSource dataSource;

    public RepoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Employee getEmployee(long employeeId) {
        ActionWithPreparedStatement<Optional<Employee>> action = ps -> {
            ps.setLong(1, employeeId);
            ResultSet resultSet = ps.executeQuery();

            return resultSet.next()
                    ? Optional.of(toEmployee(resultSet))
                    : Optional.empty();
        };

        return actionOnPreparedStatement(SELECT_EMPLOYEE, action).orElseThrow();
    }

    private  <T> T actionOnPreparedStatement(String select, ActionWithPreparedStatement<T> function) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(select)) {

            return function.inPreparedStatement(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot find employee");
        }
    }

    @Override
    public List<Employee> getEmployees() {
        ActionWithPreparedStatement<List<Employee>> action = ps -> {
            ResultSet resultSet = ps.executeQuery();
            List<Employee> employees = new LinkedList<>();

            while (resultSet.next()) {
                var employee = toEmployee(resultSet);
                employees.add(employee);
            }

            return employees;
        };


        return actionOnPreparedStatement(SELECT_EMPLOYEES, action);
    }

    @Override
    public Employee getEmployeeFullGraph(long employeeId) {
        ActionWithPreparedStatement<Employee> action = ps -> {
            ps.setLong(1, employeeId);
            ResultSet resultSet = ps.executeQuery();

            return toEmployeeWithProjects(resultSet);
        };

        return actionOnPreparedStatement(SELECT_EMPLOYEE_AND_PROJECTS, action);
    }

    @Override
    public List<Employee> getEmployeesFullGraph() {
        ActionWithPreparedStatement<List<Employee>> action = ps -> {
            ResultSet resultSet = ps.executeQuery();
            List<Employee> employees = new LinkedList<>();

            while (resultSet.next()) {
                var employee = toEmployee(resultSet);
                employees.add(employee);
            }

            return employees;
        };

        return actionOnPreparedStatement(SELECT_ALL_EMPLOYEES_AND_PROJECTS, action);
    }

    @Override
    public Project getProject(long projectId) {
        return null;
    }
}
