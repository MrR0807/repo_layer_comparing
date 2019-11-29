package lt.comparing.service;

import lt.comparing.config.H2DataSource;
import lt.comparing.exceptions.EmployeeNotFoundException;
import lt.comparing.plainjdbc.entity.Building;
import lt.comparing.plainjdbc.entity.Cubicle;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.repo.JdbcEmployeeRepo;
import lt.comparing.plainjdbc.service.JdbcEmployeeService;
import lt.comparing.utils.H2Launcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static lt.comparing.fixture.EmployeesFixture.defaultEmployeeWithCubicle;
import static lt.comparing.fixture.EmployeesFixture.employeesWithFullGraph;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmployeeServiceTest {

    private static EmployeeService service;
    private static H2Launcher h2Launcher;

    @BeforeAll
    static void initialize() {
        h2Launcher = H2Launcher.startTcpServer();
        service = new JdbcEmployeeService(new JdbcEmployeeRepo(H2DataSource.dataSource()));
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
        Employee result = service.getEmployee(1);
        Employee expected = defaultEmployeeWithCubicle().build();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getEmployee__whenEmployeeDoesNotExists__thenThrowException() {
        assertThatThrownBy(() -> service.getEmployee(999))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee with id 999 not found");
    }

    @Test
    void getEmployeeFullGraph() {
        Employee expected = defaultEmployeeWithCubicle().build();

        Employee result = service.getEmployeeFullGraph(1);

        assertThat(result).isNotNull();
        assertThat(result).isEqualToIgnoringGivenFields(expected, "salary", "projects");
        assertThat(result.getSalary()).isEqualTo(expected.getSalary());
        assertThat(result.getProjects()).containsExactlyElementsOf(expected.getProjects());
        assertThat(result.getCubicle()).isEqualToComparingFieldByField(expected.getCubicle());
    }

    @Test
    void getEmployeeFullGraph__whenEmployeeDoesNotExists__thenThrowException() {
        assertThatThrownBy(() -> service.getEmployeeFullGraph(999))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee with id 999 not found");
    }

    @Test
    void getEmployeeFullGraph__whenProjectDoesNotExists__thenReturnEmptyProjectList() {

    }

    @Test
    void getEmployees() {
        Set<Employee> expected = employeesWithFullGraph();
        Set<Employee> result = service.getEmployees();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);
        assertThat(result).containsExactlyElementsOf(expected);
    }

    @Test
    void getEmployees__whenThereIsNoEmployees__thenReturnEmptyList() {
        //given delete all employess
        //when getEmployees
        //then return emptyList
    }

    @Test
    void getEmployeesFullGraph() {
        Set<Employee> expected = employeesWithFullGraph();
        Set<Employee> result = service.getEmployeesFullGraph();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);
        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void getEmployeesFullGraph__thenCubiclesAreTheSame() {
        Set<Cubicle> expectedCubicles = extractCubicles(employeesWithFullGraph());
        Set<Cubicle> resultCubicles = extractCubicles(service.getEmployeesFullGraph());

        assertThat(resultCubicles).hasSize(4);
        assertThat(expectedCubicles).containsExactlyInAnyOrderElementsOf(resultCubicles);
    }

    private Set<Cubicle> extractCubicles(Set<Employee> employees) {
        return employees.stream()
                .map(Employee::getCubicle)
                .collect(Collectors.toSet());
    }

    @Test
    void getEmployeesFullGraph__thenBuildingIsTheSame() {
        Building expectedBuilding = extractBuilding(employeesWithFullGraph());
        Building resultBuilding = extractBuilding(service.getEmployeesFullGraph());

        assertThat(expectedBuilding).isEqualToComparingFieldByField(resultBuilding);
    }

    private Building extractBuilding(Set<Employee> employees) {
        return employees.stream()
                .map(Employee::getCubicle)
                .map(Cubicle::getBuilding)
                .findAny().orElseThrow();
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