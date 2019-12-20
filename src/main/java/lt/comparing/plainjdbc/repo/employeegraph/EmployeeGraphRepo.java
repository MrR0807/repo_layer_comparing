package lt.comparing.plainjdbc.repo.employeegraph;

import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.JdbcUtil;
import lt.comparing.plainjdbc.repo.employee.InsertEmployee;
import lt.comparing.plainjdbc.repo.employee.InsertEmployeeProject;
import lt.comparing.plainjdbc.repo.nonemployee.InsertAllProjects;
import lt.comparing.plainjdbc.repo.nonemployee.SelectAllProjectsInProjectName;
import org.apache.commons.collections4.ListUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeGraphRepo {

    private final JdbcUtil jdbcUtil;
    private final InsertEmployee insertEmployee;
    private final SelectAllProjectsInProjectName selectAllProjectsInProjectName;
    private final InsertAllProjects insertAllProjects;

    public EmployeeGraphRepo(DataSource dataSource, InsertEmployee insertEmployee,
                             SelectAllProjectsInProjectName selectAllProjectsInProjectName,
                             InsertAllProjects insertAllProjects) {
        this.jdbcUtil = new JdbcUtil(dataSource);
        this.insertEmployee = insertEmployee;
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

            ps = conn.prepareStatement(selectAllProjectsInProjectName.preparedStatementSQL(projectNames));
            List<Project> foundProjects = selectAllProjectsInProjectName.select(projectNames).doInConnection(ps);

            List<String> foundProjectNames = foundProjects.stream().map(Project::getProjectName).collect(Collectors.toList());
            List<String> nonExistingProjects = ListUtils.subtract(projectNames, foundProjectNames);

            ps = conn.prepareStatement(insertAllProjects.sql(nonExistingProjects), Statement.RETURN_GENERATED_KEYS);
            List<Long> projectGeneratedKeys = insertAllProjects.insert(nonExistingProjects).doInConnection(ps);

            ps = conn.prepareStatement(insertEmployee.preparedStatementSQL(), Statement.RETURN_GENERATED_KEYS);
            List<Long> employeeGeneratedKey = insertEmployee.insert(employee).doInConnection(ps);

            ps = conn.prepareStatement(InsertEmployeeProject.preparedStatementSQL(), Statement.RETURN_GENERATED_KEYS);
            InsertEmployeeProject.insert(employeeGeneratedKey.get(0), projectGeneratedKeys).doInConnection(ps);

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
}