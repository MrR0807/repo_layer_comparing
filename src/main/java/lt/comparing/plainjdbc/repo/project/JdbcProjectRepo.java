package lt.comparing.plainjdbc.repo.project;

import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.JdbcHelper;
import lt.comparing.plainjdbc.repo.nonemployee.InsertAllProjects;
import lt.comparing.plainjdbc.repo.nonemployee.SelectAllProjectsInProjectId;
import lt.comparing.plainjdbc.repo.nonemployee.SelectAllProjectsInProjectName;
import lt.comparing.plainjdbc.repo.sqlfunction.InsertReturning;
import lt.comparing.plainjdbc.repo.sqlfunction.Select;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcProjectRepo {

    private final JdbcHelper jdbcHelper;
    private final SelectAllProjectsInProjectName selectAllProjectsInProjectName;
    private final SelectAllProjectsInProjectId selectAllProjectsInProjectId;
    private final InsertAllProjects insertAllProjects;

    public JdbcProjectRepo(DataSource dataSource, SelectAllProjectsInProjectName selectAllProjectsInProjectName,
                           SelectAllProjectsInProjectId selectAllProjectsInProjectId,
                           InsertAllProjects insertAllProjects) {
        this.jdbcHelper = new JdbcHelper(dataSource);
        this.selectAllProjectsInProjectName = selectAllProjectsInProjectName;
        this.selectAllProjectsInProjectId = selectAllProjectsInProjectId;
        this.insertAllProjects = insertAllProjects;
    }

    public List<Project> selectIn(Collection<Long> projectIds) {
        String sql = selectAllProjectsInProjectId.preparedStatementSQL(projectIds);
        Select<List<Project>> select = selectAllProjectsInProjectId.select(projectIds);

        return jdbcHelper.select(sql, select);
    }

    public List<Project> selectInProjectNames(Collection<String> projectNames) {
        String sql = selectAllProjectsInProjectName.preparedStatementSQL(projectNames);
        Select<List<Project>> select = selectAllProjectsInProjectName.select(projectNames);

        return jdbcHelper.select(sql, select);
    }

    public List<Project> save(List<Project> projects) {
        if (projects.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> projectNames = projects.stream()
                .map(Project::getProjectName)
                .collect(Collectors.toList());

        String sql = insertAllProjects.sql(projectNames);
        InsertReturning<List<Long>> insert1 = insertAllProjects.insert(projectNames);

        List<Long> generatedKeys = jdbcHelper.insertReturnGeneratedKeys(sql, insert1);

        return selectIn(generatedKeys);
    }
}