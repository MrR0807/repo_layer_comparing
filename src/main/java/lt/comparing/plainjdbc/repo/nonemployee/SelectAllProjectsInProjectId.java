package lt.comparing.plainjdbc.repo.nonemployee;

import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.sqlfunction.Select;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SelectAllProjectsInProjectId {

    private static final String SELECT_ALL_PROJECTS_IN = """
            SELECT p.id p_id, p.project_name p_project_name
            FROM company.project p
            WHERE p.id IN (%s)""";

    public String preparedStatementSQL(Collection<Long> projectIds) {
        String placeHolders = preparePlaceHolders(projectIds.size(), "", ",");
        return String.format(SELECT_ALL_PROJECTS_IN, placeHolders);
    }

    public Select<List<Project>> select(Collection<Long> projectIds) {
        return ps -> getProjects(projectIds, ps);
    }

    private static List<Project> getProjects(Collection<Long> projectIds, PreparedStatement ps) throws SQLException {
        setValues(ps, projectIds.toArray());
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
