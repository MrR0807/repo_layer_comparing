package lt.comparing.plainjdbc.repo.project;

import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.JdbcHelper;
import lt.comparing.plainjdbc.repo.sqlfunction.Insert;
import lt.comparing.plainjdbc.repo.sqlfunction.Select;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static lt.comparing.plainjdbc.repo.project.ProjectSQLStatements.*;

public class JdbcProjectRepo {

    private final JdbcHelper jdbcHelper;

    public JdbcProjectRepo(DataSource dataSource) {
        jdbcHelper = new JdbcHelper(dataSource);
    }

    public List<Project> selectIn(Collection<Long> projectIds) {
        return selectIn(projectIds, SELECT_ALL_PROJECTS_IN);
    }

    public List<Project> selectInProjectNames(Collection<String> projectNames) {
        return selectIn(projectNames, SELECT_ALL_PROJECTS_IN_PROJECT_NAME);
    }

    private List<Project> selectIn(Collection<?> valuesIn, String selectSql) {
        if (valuesIn.isEmpty()) {
            return new ArrayList<>();
        }

        String sql = String.format(selectSql, preparePlaceHolders(valuesIn.size()));

        Select<List<Project>> selectProject = ps -> {
            setValues(ps, valuesIn.toArray());
            ResultSet rs = ps.executeQuery();
            List<Project> projects = new ArrayList<>();

            while (rs.next()) {
                var projectId = rs.getLong("p_id");
                var projectName = rs.getString("p_project_name");
                projects.add(new Project(projectId, projectName));
            }
            return projects;
        };

        return jdbcHelper.select(sql, selectProject);
    }

    private static String preparePlaceHolders(int size) {
        return preparePlaceHolders(size, "", ",");
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

    public List<Project> save(List<Project> projects) {
        if (projects.isEmpty()) {
            return new ArrayList<>();
        }

        String formattedInsert = String.format(INSERT_ALL_PROJECTS, preparePlaceHolders(projects.size(), "(", "),"));

        Insert<List<Project>> insert = (ps, projectList) -> {
            Object[] projectNames = projectList.stream()
                    .map(Project::getProjectName).toArray();
            setValues(ps, projectNames);

            int insertCount = ps.executeUpdate();

            if (projects.size() != insertCount) {
                throw new RuntimeException("Something went wrong inserting projects");
            }
        };

        List<Long> generatedKeys = jdbcHelper.insertReturnGeneratedKeys(formattedInsert, insert, projects);

        return selectIn(generatedKeys);
    }
}