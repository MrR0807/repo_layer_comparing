package lt.comparing.plainjdbc.repo.project;

public class ProjectSQLStatements {

    private ProjectSQLStatements() {
    }

    static final String SELECT_ALL_PROJECTS_IN = """
            SELECT p.id p_id, p.project_name p_project_name
            FROM company.project p
            WHERE p.id IN (%s)""";

    static final String SELECT_ALL_PROJECTS_IN_PROJECT_NAME = """
            SELECT p.id p_id, p.project_name p_project_name
            FROM company.project p
            WHERE p.project_name IN (%s)""";

    static final String INSERT_ALL_PROJECTS = """
            INSERT INTO company.project (project_name)
            VALUES %s""";
}
