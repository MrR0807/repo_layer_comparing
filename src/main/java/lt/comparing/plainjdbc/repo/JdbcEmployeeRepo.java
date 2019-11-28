package lt.comparing.plainjdbc.repo;

import lt.comparing.plainjdbc.entity.*;
import lt.comparing.repo.EmployeeRepo;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.Objects.isNull;
import static lt.comparing.plainjdbc.repo.EmployeeSQLStatements.*;

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
    public Set<Employee> getEmployees() {
        ActionWithPreparedStatement<Set<Employee>> action = ps -> {
            ResultSet resultSet = ps.executeQuery();
            Set<Employee> employees = new LinkedHashSet<>();

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

            return toEmployeeMap(resultSet).get(employeeId);
        };

        return Optional.ofNullable(jdbcHelper.get(SELECT_EMPLOYEE_FULL_GRAPH, action));
    }

    @Override
    public Set<Employee> getEmployeesFullGraph() {
        ActionWithPreparedStatement<Set<Employee>> action = ps -> {
            ResultSet resultSet = ps.executeQuery();
            Map<Long, Employee> employeeMap = toEmployeeMap(resultSet);

            return new HashSet<>(employeeMap.values());
        };

        return jdbcHelper.get(SELECT_ALL_EMPLOYEES_FULL_GRAPH, action);
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

    private static Map<Long, Employee> toEmployeeMap(ResultSet resultSet) throws SQLException {
        Map<Long, Employee> employeeMap = new LinkedHashMap<>();

        while (resultSet.next()) {
            var employeeId = resultSet.getLong("e_id");
            Employee employee = employeeMap.get(employeeId);
            if (isNull(employee)) {
                employee = toEmployee(resultSet);
                employeeMap.put(employee.getId(), employee);
            }
            if (isNull(employee.getCubicle())) {
                var building = toBuilding(resultSet);
                var cubicle = toCubicle(resultSet, building);
                employee.setCubicle(cubicle);
            }
            employee.addProject(toProject(resultSet));
        }
        return employeeMap;
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
