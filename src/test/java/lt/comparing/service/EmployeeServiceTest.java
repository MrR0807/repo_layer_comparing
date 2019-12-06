package lt.comparing.service;

import lt.comparing.config.H2DataSource;
import lt.comparing.exceptions.EmployeeNotFoundException;
import lt.comparing.fixture.EmployeeBuilder;
import lt.comparing.plainjdbc.entity.Building;
import lt.comparing.plainjdbc.entity.Cubicle;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.JdbcEmployeeRepo;
import lt.comparing.plainjdbc.repo.JdbcProjectRepo;
import lt.comparing.plainjdbc.service.JdbcEmployeeService;
import lt.comparing.utils.H2Launcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
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
        DataSource dataSource = H2DataSource.dataSource();
        service = new JdbcEmployeeService(new JdbcEmployeeRepo(dataSource), new JdbcProjectRepo(dataSource));
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
        //given employee does not have projects assign
        //when getEmployeeGraph
        //then return employee with empty project list
    }

    @Test
    void getEmployees() {
        Set<Employee> expected = employeesWithFullGraph();
        Set<Employee> result = service.getEmployees();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);
        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
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
    void saveEmployeeFullGraph__whenEmployeeDoesNotExistsButProjectsExists__thenCreateOnlyEmployee() {
        List<Project> addedProjects = List.of(
                new Project(2001, "Super project"),
                new Project(2003, "Average project"));

        Employee saveEmployee = EmployeeBuilder.anEmployee()
                .withFirstName("First5")
                .withLastName("Last5")
                .withSalary(BigDecimal.TEN)
                .withEmployeeType(EmployeeType.EMPLOYEE)
                .withCubicle(new Cubicle(1005, null))
                .withProjects(addedProjects)
                .build();

        long employeeId = service.saveEmployeeFullGraph(saveEmployee);
        Employee result = service.getEmployeeFullGraph(employeeId);

        assertThat(employeeId).isEqualTo(5L);
        assertThat(result).isEqualToIgnoringGivenFields(saveEmployee, "id", "cubicle", "projects", "salary");
        assertThat(result.getSalary()).isEqualByComparingTo(saveEmployee.getSalary());
        assertThat(result.getCubicle()).isEqualTo(new Cubicle(1005, new Building(1, "Big Building", "Address 1")));
        assertThat(result.getProjects()).hasSize(2);
        assertThat(result.getProjects()).containsExactlyInAnyOrderElementsOf(addedProjects);
    }

    @Test
    void saveEmployeeFullGraph__whenEmployeeAndProjectDoesNotExist__thenCreateEmployeeAndProject() {
        List<Project> addedProjects = List.of(
                new Project(9998, "Super project 9998"),
                new Project(9999, "Average project 9999"));

        Employee saveEmployee = EmployeeBuilder.anEmployee()
                .withFirstName("First5")
                .withLastName("Last5")
                .withSalary(BigDecimal.TEN)
                .withEmployeeType(EmployeeType.EMPLOYEE)
                .withCubicle(new Cubicle(1005, null))
                .withProjects(addedProjects)
                .build();

        long employeeId = service.saveEmployeeFullGraph(saveEmployee);
        Employee result = service.getEmployeeFullGraph(employeeId);

        assertThat(employeeId).isEqualTo(5L);
        assertThat(result).isEqualToIgnoringGivenFields(saveEmployee, "id", "cubicle", "projects", "salary");
        assertThat(result.getSalary()).isEqualByComparingTo(saveEmployee.getSalary());
        assertThat(result.getCubicle()).isEqualTo(new Cubicle(1005, new Building(1, "Big Building", "Address 1")));
        assertThat(result.getProjects()).hasSize(2);
    }

    @Test
    void saveEmployeeFullGraph__whenCubicleDoesNotExists__thenThrowException() {
        Employee saveEmployee = EmployeeBuilder.anEmployee()
                .withFirstName("First5")
                .withLastName("Last5")
                .withSalary(BigDecimal.TEN)
                .withEmployeeType(EmployeeType.EMPLOYEE)
                .withCubicle(new Cubicle(9999, null))
                .withProjects(List.of())
                .build();

        assertThatThrownBy(() -> service.saveEmployeeFullGraph(saveEmployee)).isInstanceOf(RuntimeException.class)
                .hasMessage("Could not execute statement");
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