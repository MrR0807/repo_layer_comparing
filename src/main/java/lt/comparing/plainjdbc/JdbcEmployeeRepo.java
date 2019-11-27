package lt.comparing.plainjdbc;

import lt.comparing.EmployeeRepo;
import lt.comparing.plainjdbc.entity.*;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static lt.comparing.plainjdbc.EmployeeSQLStatements.*;

public class JdbcEmployeeRepo implements EmployeeRepo {

    private final JdbcHelper jdbcHelper;

    public JdbcEmployeeRepo(DataSource dataSource) {
        this.jdbcHelper = new JdbcHelper(dataSource);
    }

    @Override
    public Optional<Employee> getEmployee(long employeeId) {
        ActionWithPreparedStatement<Employee> action = ps -> {
            ps.setLong(1, employeeId);
            ResultSet resultSet = ps.executeQuery();

            return resultSet.next()
                    ? toEmployee(resultSet)
                    : null;
        };

        return Optional.ofNullable(jdbcHelper.get(SELECT_EMPLOYEE, action));
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


        return jdbcHelper.get(SELECT_EMPLOYEES, action);
    }

    @Override
    public Optional<Employee> getEmployeeFullGraph(long employeeId) {
        ActionWithPreparedStatement<Employee> action = ps -> {
            ps.setLong(1, employeeId);
            ResultSet resultSet = ps.executeQuery();

            Employee employee = null;
            Cubicle cubicle = null;
            Building building = null;

            while (resultSet.next()) {
                if (isNull(building)) {
                    building = toBuilding(resultSet);
                }
                if (isNull(cubicle)) {
                    cubicle = toCubicle(resultSet, building);
                }

                if (isNull(employee)) {
                    employee = toEmployee(resultSet, cubicle);
                }
                employee.addProject(toProject(resultSet));
            }

            return employee;
        };

        return Optional.ofNullable(jdbcHelper.get(SELECT_EMPLOYEE_FULL_GRAPH, action));
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

        return jdbcHelper.get(SELECT_ALL_EMPLOYEES_AND_PROJECTS, action);
    }

    @Override
    public void saveEmployeeFullGraph(Employee employee) {

    }

    @Override
    public void updateEmployeeFullGraph(Employee employee) {

    }

    private static Employee toEmployee(ResultSet resultSet) throws SQLException {
        return toEmployee(resultSet, null);
    }

    private static Employee toEmployee(ResultSet resultSet, Cubicle cubicle) throws SQLException {
        var id = resultSet.getLong("e_id");
        var firstName = resultSet.getString("e_first_name");
        var lastName = resultSet.getString("e_last_name");
        var salary = resultSet.getBigDecimal("e_salary");
        var employeeType = EmployeeType.valueOf(resultSet.getString("e_employee_type"));
        var listOfProjects = new ArrayList<Project>();
        return new Employee(id, firstName, lastName, salary, employeeType, cubicle, listOfProjects);
    }

    private static Project toProject(ResultSet resultSet) throws SQLException {
        var projectId = resultSet.getLong("p_id");
        var projectName = resultSet.getString("p_project_name");
        return new Project(projectId, projectName);
    }

    private static Building toBuilding(ResultSet resultSet) throws SQLException {
        var buildingId = resultSet.getLong("b_id");
        var buildingName = resultSet.getString("b_name");
        var buildingAddress = resultSet.getString("b_address");
        return new Building(buildingId, buildingName, buildingAddress);
    }

    private static Cubicle toCubicle(ResultSet resultSet, Building building) throws SQLException {
        var id =  resultSet.getLong("c_id");
        return new Cubicle(id, building);
    }
}
