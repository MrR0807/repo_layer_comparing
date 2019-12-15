package lt.comparing.plainjdbc.repo.employee;

import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.DoInConnection;
import lt.comparing.plainjdbc.repo.sqlfunction.Select;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectAllProjectsInProjectName {

    private final DoInConnection doInConnection;

    private static final String SELECT_ALL_PROJECTS_IN_PROJECT_NAME = """
            SELECT p.id p_id, p.project_name p_project_name
            FROM company.project p
            WHERE p.project_name IN (%s)""";

    public SelectAllProjectsInProjectName(DoInConnection doInConnection) {
        this.doInConnection = doInConnection;
    }

    public List<Project> select(List<String> projectNames) {
        String placeHolders = preparePlaceHolders(projectNames.size(), "", ",");
        String sql = String.format(SELECT_ALL_PROJECTS_IN_PROJECT_NAME, placeHolders);

        Select<List<Project>> extractProjects = ps -> getProjects(projectNames, ps);

        return doInConnection.select(sql, extractProjects);
    }

    private List<Project> getProjects(List<String> projectNames, PreparedStatement ps) throws SQLException {
        setValues(ps, projectNames.toArray());
        ResultSet rs = ps.executeQuery();
        List<Project> foundProjects = new ArrayList<>();

        while (rs.next()) {
            var projectId = rs.getLong("p_id");
            var projectName = rs.getString("p_project_name");
            foundProjects.add(new Project(projectId, projectName));
        }
        return foundProjects;
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
}