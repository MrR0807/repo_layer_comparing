package lt.comparing.plainjdbc.repo.employee;

import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.DoInConnection;
import lt.comparing.plainjdbc.repo.sqlfunction.InsertReturning;
import lt.comparing.plainjdbc.repo.sqlfunction.InsertReturning2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InsertAllProjects {

    private final DoInConnection doInConnection;

    public InsertAllProjects(DoInConnection doInConnection) {
        this.doInConnection = doInConnection;
    }

    private static final String INSERT_ALL_PROJECTS = """
            INSERT INTO company.project (project_name)
            VALUES %s""";

    public List<Long> insert(List<String> projects) {
        String placeHolders = preparePlaceHolders(projects.size(), "(", "),");
        String insertSql = String.format(INSERT_ALL_PROJECTS, placeHolders);
        InsertReturning2<List<Long>> insertReturning = ps -> insertProjects(projects, ps);

        return doInConnection.insert(insertSql, insertReturning);
    }

    private List<Long> insertProjects(List<String> projectNames, PreparedStatement ps) throws SQLException {
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
