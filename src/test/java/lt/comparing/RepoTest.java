package lt.comparing;

import lt.comparing.config.H2DataSource;
import lt.comparing.plainjdbc.RepoJDBC;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.utils.H2Launcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class RepoTest {

    private static Repo repo;
    private static H2Launcher h2Launcher;

    @BeforeAll
    static void initialize() {
        h2Launcher = new H2Launcher();
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
    void get() {
        Employee result = repo.getEmployee(1);
        Employee expected = expected();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testAgain() {
        Employee result = repo.getEmployee(1);
        Employee expected = expected();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);
    }

    private Employee expected() {
        return new Employee(1, "First1", "Last1", BigDecimal.valueOf(1000), EmployeeType.EMPLOYEE, null);
    }
}