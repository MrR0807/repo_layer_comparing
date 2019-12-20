package lt.comparing.plainjdbc.repo.employee;

import lt.comparing.plainjdbc.entity.Building;
import lt.comparing.plainjdbc.entity.Cubicle;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.sqlfunction.Select;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class SelectEmployeeFullGraph {

    private static final String SELECT_EMPLOYEE_FULL_GRAPH = """
            SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary, e.employee_type e_employee_type,
            p.id p_id, p.project_name p_project_name,
            c.id c_id,
            b.id b_id, b.name b_name, b.address b_address
            FROM company.employee e
            INNER JOIN company.employee_project ep ON e.id = ep.employee_id
            INNER JOIN company.project p ON ep.project_id = p.id
            INNER JOIN company.cubicle c ON e.cubicle_id = c.id
            INNER JOIN company.building b ON c.building_id = b.id
            WHERE e.id = ?""";

    public static String preparedStatementSQL() {
        return SELECT_EMPLOYEE_FULL_GRAPH;
    }

    public static Select<Employee> select(Long employeeId) {
        return ps -> extractEmployee(employeeId, ps);
    }

    private static Employee extractEmployee(Long employeeId, PreparedStatement ps) throws SQLException {
        ps.setLong(1, employeeId);
        ResultSet resultSet = ps.executeQuery();

        return toEmployeeMap(resultSet).get(employeeId);
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
