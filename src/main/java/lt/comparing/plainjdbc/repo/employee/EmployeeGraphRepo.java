package lt.comparing.plainjdbc.repo.employee;

import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.JdbcUtil;
import lt.comparing.plainjdbc.repo.sqlfunction.Insert;
import org.apache.commons.collections4.ListUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeGraphRepo {

    private final JdbcUtil jdbcUtil;
    private final SelectAllProjectsInProjectName selectAllProjectsInProjectName;
    private final InsertAllProjects insertAllProjects;

    static final String INSERT_EMPLOYEE = """
            INSERT INTO company.employee (first_name, last_name, salary, employee_type, cubicle_id)
            VALUES (?, ?, ?, ?, ?)""";

    static final String INSERT_INTO_PROJECTS_EMPLOYEES = """
            INSERT INTO company.employee_project (employee_id, project_id)
            VALUES (?, ?)""";

    public EmployeeGraphRepo(DataSource dataSource, SelectAllProjectsInProjectName selectAllProjectsInProjectName,
                             InsertAllProjects insertAllProjects) {
        this.jdbcUtil = new JdbcUtil(dataSource);
        this.selectAllProjectsInProjectName = selectAllProjectsInProjectName;
        this.insertAllProjects = insertAllProjects;
    }

    public Employee saveEmployeeFullGraph(Employee employee) {
        Connection conn = null;
        PreparedStatement ps = null;
        List<Project> projects = employee.getProjects();

        List<String> projectNames = projects.stream()
                .map(Project::getProjectName)
                .collect(Collectors.toList());

        try {
            conn = jdbcUtil.getConnection();

            List<Project> foundProjects = selectAllProjectsInProjectName.select(projectNames);

            List<String> foundProjectNames = foundProjects.stream().map(Project::getProjectName).collect(Collectors.toList());
            List<String> nonExistingProjects = ListUtils.subtract(projectNames, foundProjectNames);

            List<Long> projectGeneratedKeys = insertAllProjects.insert(nonExistingProjects);

            ps = conn.prepareStatement(INSERT_EMPLOYEE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setBigDecimal(3, employee.getSalary());
            ps.setString(4, employee.getEmployeeType().toString());
            ps.setLong(5, employee.getCubicle().getId());
            ps.executeUpdate();

            List<Long> employeeGeneratedKey = getGeneratedKeys(ps);


            ps = conn.prepareStatement(INSERT_INTO_PROJECTS_EMPLOYEES, Statement.RETURN_GENERATED_KEYS);

            for (Long projectId : projectGeneratedKeys) {
                Long employeeId = employeeGeneratedKey.get(0);
                ps.setLong(1, employeeId);
                ps.setLong(2, projectId);
            }

            ps.executeUpdate();

            employee.setId(employeeGeneratedKey.get(0));

            conn.commit();
            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.rollback(conn);
        } finally {
            JdbcUtil.closeStatement(ps);
            JdbcUtil.closeConnection(conn);
        }

        throw new RuntimeException("Could not execute sql");
    }

    private static List<Long> getGeneratedKeys(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            List<Long> generatedKeys = new ArrayList<>();
            while (rs.next()) {
                generatedKeys.add(rs.getLong(1));
            }
            return generatedKeys;
        }
    }
}