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

    static final String SELECT_ALL_PROJECTS_IN_PROJECT_NAME = """
            SELECT p.id p_id, p.project_name p_project_name
            FROM company.project p
            WHERE p.project_name IN (%s)""";

    static final String INSERT_ALL_PROJECTS = """
            INSERT INTO company.project (project_name)
            VALUES %s""";

    static final String INSERT_EMPLOYEE = """
            INSERT INTO company.employee (first_name, last_name, salary, employee_type, cubicle_id)
            VALUES (?, ?, ?, ?, ?)""";

    static final String INSERT_INTO_PROJECTS_EMPLOYEES = """
            INSERT INTO company.employee_project (employee_id, project_id)
            VALUES (?, ?)""";

    public EmployeeGraphRepo(DataSource dataSource) {
        this.jdbcUtil = new JdbcUtil(dataSource);
    }


    public Employee saveEmployeeFullGraph(Employee employee) {
        Connection conn = null;
        PreparedStatement ps = null;
        List<Project> projects = employee.getProjects();

        List<String> projectNames = projects.stream()
                .map(Project::getProjectName)
                .collect(Collectors.toList());

        List<Project> foundProjects = new ArrayList<>();

        String sql = String.format(SELECT_ALL_PROJECTS_IN_PROJECT_NAME, preparePlaceHolders(projects.size(), "", ","));

        try {
            conn = jdbcUtil.getConnection();
            ps = conn.prepareStatement(sql);
            setValues(ps, projectNames.toArray());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                var projectId = rs.getLong("p_id");
                var projectName = rs.getString("p_project_name");
                foundProjects.add(new Project(projectId, projectName));
            }

            List<String> foundProjectNames = foundProjects.stream().map(Project::getProjectName).collect(Collectors.toList());
            List<String> nonExistingProjects = ListUtils.subtract(projectNames, foundProjectNames);
            String insert = String.format(INSERT_ALL_PROJECTS, preparePlaceHolders(nonExistingProjects.size(), "(", "),"));


            ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            setValues(ps, nonExistingProjects.toArray());
            ps.executeUpdate();
            List<Long> projectGeneratedKeys = getGeneratedKeys(ps);


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

    private static String preparePlaceHolders(int size, String prefix, String postfix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(prefix);
            sb.append("?");
            sb.append(postfix);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private static void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 1, values[i]);
        }
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

    public <T> long saveEmployeeFullGraph(Employee employee, Insert<T>... inserts) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = jdbcUtil.getConnection();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.rollback(conn);
        } finally {
            JdbcUtil.closeStatement(ps);
            JdbcUtil.closeConnection(conn);
        }
        throw new RuntimeException("Could not execute sql");
    }
}