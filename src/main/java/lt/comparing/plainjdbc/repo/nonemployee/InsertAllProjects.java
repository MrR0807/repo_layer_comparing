package lt.comparing.plainjdbc.repo.nonemployee;

import lt.comparing.plainjdbc.repo.sqlfunction.InsertReturning;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertAllProjects {

    private static final String INSERT_ALL_PROJECTS = """
            INSERT INTO company.project (project_name)
            VALUES %s""";

    public String sql(List<String> projectNames) {
        String placeHolders = preparePlaceHolders(projectNames.size(), "(", "),");
        return String.format(INSERT_ALL_PROJECTS, placeHolders);
    }

    public InsertReturning<List<Long>> insert(List<String> projects) {
        return ps -> insertProjects(projects, ps);
    }

    private static List<Long> insertProjects(List<String> projectNames, PreparedStatement ps) throws SQLException {
        setValues(ps, projectNames.toArray());
        ps.executeUpdate();
        return getGeneratedKeys(ps);
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
