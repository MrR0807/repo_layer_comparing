package lt.comparing.plainjdbc.repo.employee;

import lt.comparing.config.H2DataSource;
import lt.comparing.fixture.EmployeeBuilder;
import lt.comparing.plainjdbc.entity.Cubicle;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.DoInConnection;
import lt.comparing.utils.H2Launcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeGraphRepoTest {

    private static EmployeeGraphRepo repo;
    private static H2Launcher h2Launcher;

    @BeforeAll
    static void initialize() {
        h2Launcher = H2Launcher.startTcpServer();
        DataSource dataSource = H2DataSource.dataSource();
        DoInConnection doInConnection = new DoInConnection(dataSource);
        SelectAllProjectsInProjectName selectAllProjectsInProjectName = new SelectAllProjectsInProjectName(doInConnection);
        InsertAllProjects insertAllProjects = new InsertAllProjects(doInConnection);
        repo = new EmployeeGraphRepo(dataSource, selectAllProjectsInProjectName, insertAllProjects);
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
    void name() {
        List<Project> addedProjects = List.of(
                new Project(9997, "Super project"),
                new Project(9998, "Super project 9998"),
                new Project(9999, "Average project 9999"));

        Employee saveEmployee = EmployeeBuilder.anEmployee()
                .withFirstName("First5")
                .withLastName("Last5")
                .withSalary(BigDecimal.TEN)
                .withEmployeeType(EmployeeType.EMPLOYEE)
                .withCubicle(new Cubicle(1004, null))
                .withProjects(addedProjects)
                .build();


        Employee employee = repo.saveEmployeeFullGraph(saveEmployee);

        assertThat(employee.getId()).isEqualTo(5L);
        assertThat(employee.getFirstName()).isEqualTo("First5");
        assertThat(employee.getLastName()).isEqualTo("Last5");
        assertThat(employee.getSalary()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(employee.getEmployeeType()).isEqualTo(EmployeeType.EMPLOYEE);
        assertThat(employee.getCubicle().getId()).isEqualTo(1004);
        assertThat(employee.getProjects()).extracting(Project::getProjectName)
                .containsExactlyInAnyOrder("Super project", "Super project 9998", "Average project 9999");

        //Todo
        //        assertThat(employee.getProjects()).extracting(Project::getId)
        //                .containsExactlyInAnyOrder(2000L, 2003L, 2004L);
    }
}