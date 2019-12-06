package lt.comparing.plainjdbc.repo;

import lt.comparing.plainjdbc.entity.Building;
import lt.comparing.plainjdbc.entity.Cubicle;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.plainjdbc.entity.Project;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;
import static lt.comparing.plainjdbc.repo.EmployeeSQLStatements.INSERT_EMPLOYEE;
import static lt.comparing.plainjdbc.repo.EmployeeSQLStatements.SELECT_ALL_EMPLOYEES_FULL_GRAPH;
import static lt.comparing.plainjdbc.repo.EmployeeSQLStatements.SELECT_EMPLOYEE;
import static lt.comparing.plainjdbc.repo.EmployeeSQLStatements.SELECT_EMPLOYEES;
import static lt.comparing.plainjdbc.repo.EmployeeSQLStatements.SELECT_EMPLOYEE_FULL_GRAPH;

public class JdbcEmployeeRepo {

    private final JdbcHelper jdbcHelper;

    public JdbcEmployeeRepo(DataSource dataSource) {
        this.jdbcHelper = new JdbcHelper(dataSource);
    }

    public Optional<Employee> getEmployee(long employeeId) {
        Select<Employee> action = ps -> {
            ps.setLong(1, employeeId);
            ResultSet resultSet = ps.executeQuery();

            return resultSet.next() ? toEmployee(resultSet) : null;
        };

        return Optional.ofNullable(jdbcHelper.get(SELECT_EMPLOYEE, action));
    }

    public Set<Employee> getEmployees() {
        Select<Set<Employee>> action = ps -> {
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

    public Optional<Employee> getEmployeeFullGraph(long employeeId) {
        Select<Employee> action = ps -> {
            ps.setLong(1, employeeId);
            ResultSet resultSet = ps.executeQuery();

            return toEmployeeMap(resultSet).get(employeeId);
        };

        return Optional.ofNullable(jdbcHelper.get(SELECT_EMPLOYEE_FULL_GRAPH, action));
    }

    public Set<Employee> getEmployeesFullGraph() {
        Select<Set<Employee>> action = ps -> {
            ResultSet resultSet = ps.executeQuery();
            Map<Long, Employee> employeeMap = toEmployeeMap(resultSet);

            return new HashSet<>(employeeMap.values());
        };

        return jdbcHelper.get(SELECT_ALL_EMPLOYEES_FULL_GRAPH, action);
    }

    public long saveEmployee(Employee employee) {
        InsertWithId<Employee> insertWithId = (ps, e) -> {
            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setBigDecimal(3, e.getSalary());
            ps.setString(4, e.getEmployeeType().toString());
            ps.setLong(5, e.getCubicle().getId());
            ps.executeUpdate();

            long key = getGeneratedKey(ps);
            employee.setId(key);
            return key;
        };

        long employeeId = jdbcHelper.insert(INSERT_EMPLOYEE, insertWithId, employee);
        return employeeId;
    }

    private long getGeneratedKey(PreparedStatement ps) throws SQLException {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new RuntimeException("Could not find generated key");
            }
        }
    }

    public void updateEmployee(Employee employee) {

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

    private static Employee toEmployee(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("e_id");
        var firstName = resultSet.getString("e_first_name");
        var lastName = resultSet.getString("e_last_name");
        var salary = resultSet.getBigDecimal("e_salary");
        var employeeType = EmployeeType.valueOf(resultSet.getString("e_employee_type"));
        var listOfProjects = new ArrayList<Project>();
        return new Employee(id, firstName, lastName, salary, employeeType, null, listOfProjects);
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