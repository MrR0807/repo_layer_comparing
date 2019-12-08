package lt.comparing.plainjdbc.repo;

import lt.comparing.plainjdbc.entity.Project;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JdbcProjectRepo {

    private final DataSource dataSource;

    public JdbcProjectRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static final String SELECT_ALL_PROJECTS_IN = """
            SELECT p.id p_id, p.project_name p_project_name
            FROM company.project p
            WHERE p.id IN (%s)""";

    public List<Project> selectIn(Collection<Long> projectIds) {
        String sql = String.format(SELECT_ALL_PROJECTS_IN, preparePlaceHolders(projectIds.size()));
        List<Project> projects = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setValues(ps, projectIds.toArray());
            ResultSet results = ps.executeQuery();

            while (results.next()) {
                var projectId = results.getLong("p_id");
                var projectName = results.getString("p_project_name");
                projects.add(new Project(projectId, projectName));
            }
            return projects;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not execute statement");
        }
    }

    private static String preparePlaceHolders(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 1, values[i]);
        }
    }

    public List<Long> save(List<Project> projects) {
        return List.of();
    }


    //
//    jdbcHelper.insert(INSERT_INTO_EMPLOYEE_PROJECT, insertEmployeeProject, employee);
//
//
//    Insert<Employee> insertEmployeeProject = (ps, e) -> {
//        for (Project project : e.getProjects()) {
//            ps.setLong(1, e.getId());
//            ps.setLong(2, project.getId());
//            ps.executeUpdate();
//        }
//    };
}