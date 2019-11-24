package lt.comparing;

import lt.comparing.config.H2DataSource;
import lt.comparing.plainjdbc.RepoJDBC;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.utils.H2Launcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static lt.comparing.fixture.EmployeesFixture.defaultEmployee;
import static lt.comparing.fixture.EmployeesFixture.employees;
import static org.assertj.core.api.Assertions.assertThat;

class RepoTest {

    private static Repo repo;
    private static H2Launcher h2Launcher;

    @BeforeAll
    static void initialize() {
        h2Launcher = H2Launcher.startTcpServer();
        repo = new RepoJDBC(H2DataSource.dataSource());
    }

    @BeforeEach
    void setUp() {
        h2Launcher.initDatabase();
    }

    @AfterEach
    void tearDown() {
        h2Launcher.restart();
    }

    @Test
    void getEmployee() {
        Employee result = repo.getEmployee(1);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected());
    }

    @Test
    void getEmployeeFullGraph() {
        Employee expected = defaultEmployee()
                .withProjects(List.of(new Project(2001, "Super project"), new Project(2003, "Average project")))
                .build();

        Employee result = repo.getEmployeeFullGraph(1);

        assertThat(result).isNotNull();
        assertThat(result).isEqualToIgnoringGivenFields(expected, "salary", "projects");
        assertThat(result.getSalary()).isEqualTo(expected.getSalary());
        assertThat(result.getProjects()).containsOnlyElementsOf(expected.getProjects());
    }

    @Test
    void getEmployees() {
        List<Employee> expected = employees();

        List<Employee> result = repo.getEmployees();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);
        assertThat(result).containsExactlyElementsOf(expected);
    }

    private Employee expected() {
        return defaultEmployee().build();
    }


}