package lt.comparing.plainjdbc.repo.employee;

import lt.comparing.plainjdbc.repo.sqlfunction.Insert;

import java.util.List;

public class InsertEmployeeProject {

    private static final String INSERT_INTO_PROJECTS_EMPLOYEES = """
            INSERT INTO company.employee_project (employee_id, project_id)
            VALUES (?, ?)""";

    public static String preparedStatementSQL() {
        return INSERT_INTO_PROJECTS_EMPLOYEES;
    }

    public static Insert insert(Long employeeId, List<Long> projectGeneratedKeys) {
        return ps -> {
            for (Long projectId : projectGeneratedKeys) {
                ps.setLong(1, employeeId);
                ps.setLong(2, projectId);
            }
            ps.executeUpdate();
        };
    }
}
