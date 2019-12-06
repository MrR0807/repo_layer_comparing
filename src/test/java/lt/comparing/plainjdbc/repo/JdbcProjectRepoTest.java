package lt.comparing.plainjdbc.repo;

import lt.comparing.config.H2DataSource;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.utils.H2Launcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcProjectRepoTest {

    private static H2Launcher h2Launcher;
    private static JdbcProjectRepo projectRepo;

    @BeforeAll
    static void initialize() {
        h2Launcher = H2Launcher.startTcpServer();
        DataSource dataSource = H2DataSource.dataSource();
        projectRepo = new JdbcProjectRepo(dataSource);
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
    void selectIn() {
        List<Project> projects = projectRepo.selectIn(List.of(2001L, 2002L, 2003L));

        assertThat(projects).hasSize(3);
    }
}