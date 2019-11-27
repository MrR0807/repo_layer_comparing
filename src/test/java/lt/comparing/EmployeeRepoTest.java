package lt.comparing;

import lt.comparing.config.H2DataSource;
import lt.comparing.plainjdbc.JdbcEmployeeRepo;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.utils.H2Launcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static lt.comparing.fixture.EmployeesFixture.defaultEmployeeWithCubicle;
import static lt.comparing.fixture.EmployeesFixture.employees;
import static org.assertj.core.api.Assertions.assertThat;

class EmployeeRepoTest {

    private static EmployeeRepo repo;
    private static H2Launcher h2Launcher;

    @BeforeAll
    static void initialize() {
        h2Launcher = H2Launcher.startTcpServer();
        repo = new JdbcEmployeeRepo(H2DataSource.dataSource());
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
        Employee result = repo.getEmployee(1).orElseThrow();
        Employee expected = defaultEmployeeWithCubicle().build();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getEmployee__whenEmployeeDoesNotExists__thenReturnEmptyOptional() {
        Optional<Employee> result = repo.getEmployee(999);

        assertThat(result).isEmpty();
    }

    @Test
    void getEmployeeFullGraph() {
        Employee expected = defaultEmployeeWithCubicle()
                .withProjects(List.of(
                        new Project(2001, "Super project"),
                        new Project(2003, "Average project")))
                .build();

        Employee result = repo.getEmployeeFullGraph(1).orElseThrow();

        assertThat(result).isNotNull();
        assertThat(result).isEqualToIgnoringGivenFields(expected, "salary", "projects");
        assertThat(result.getSalary()).isEqualTo(expected.getSalary());
        assertThat(result.getProjects()).containsOnlyElementsOf(expected.getProjects());
        assertThat(result.getCubicle()).isEqualToComparingFieldByField(expected.getCubicle());
    }

    @Test
    void getEmployeeFullGraph__whenEmployeeDoesNotExists__thenThrowException() {
    }

    @Test
    void getEmployeeFullGraph__whenProjectDoesNotExists__thenReturnEmptyProjectList() {
    }

    @Test
    void getEmployees() {
        List<Employee> expected = employees();

        List<Employee> result = repo.getEmployees();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);
        assertThat(result).containsExactlyElementsOf(expected);
    }

    @Test
    void getEmployees__whenThereIsNoEmployees__thenReturnEmptyList() {

    }

    @Test
    void saveEmployeeFullGraph() {
    }

    @Test
    void saveEmployeeFullGraph__whenOnlyEmployeeDoesNotExists__thenCreateOnlyEmployee() {
    }

    @Test
    void saveEmployeeFullGraph__whenEmployeeAndProjectDoesNotExist__thenCreateEmployeeAndProject() {
    }

    @Test
    void saveEmployeeFullGraph__whenCubicleDoesNotExists__thenThrowException() {
    }

    @Test
    void saveEmployeeFullGraph__whenBuildingDoesNotExists__thenThrowException() {
    }

    @Test
    void updateEmployeeFullGraph__whenUpdatingEmployeeName__thenUpdateOnlyName() {
    }

    @Test
    void updateEmployeeFullGraph__whenChangeEmployeeProject__thenCreateNewProjectAndRemoveProject() {
    }

    @Test
    void updateEmployeeFullGraph__whenChangeEmployeeProjectAndProjectHasMoreEmployees__thenCreateNewProjectAndDONTRemoveProject() {
    }

    @Test
    void updateEmployeeFullGraph__whenChangingToOccupiedCubicle__thenThrowException() {
    }
}