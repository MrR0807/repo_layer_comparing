package lt.comparing;

import lt.comparing.config.H2DataSource;
import lt.comparing.fixture.EmployeeBuilder;
import lt.comparing.plainjdbc.RepoJDBC;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.utils.H2Launcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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

    private Employee expected() {
        return defaultEmployee().build();
    }

    private EmployeeBuilder defaultEmployee() {
        return EmployeeBuilder.anEmployee()
                .withId(1)
                .withFirstName("First1")
                .withLastName("Last1")
                .withSalary(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP))
                .withEmployeeType(EmployeeType.EMPLOYEE)
                .withCubicle(null)
                .withProjects(new ArrayList<>());
    }
}