package lt.comparing.plainjdbc.repo;

import lt.comparing.config.H2DataSource;
import lt.comparing.exceptions.ProjectExistsException;
import lt.comparing.fixture.ProjectFixture;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.service.JdbcProjectService;
import lt.comparing.utils.H2Launcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JdbcProjectServiceTest {

    private static H2Launcher h2Launcher;
    private static JdbcProjectService projectService;

    @BeforeAll
    static void initialize() {
        h2Launcher = H2Launcher.startTcpServer();
        DataSource dataSource = H2DataSource.dataSource();
        projectService = new JdbcProjectService(new JdbcProjectRepo(dataSource));
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
        List<Project> result = projectService.selectIn(List.of(2000L, 2001L, 2002L));
        List<Project> expected = ProjectFixture.projects();

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void selectIn__whenNothingIsFound__thenReturnEmptyArray() {
        List<Project> result = projectService.selectIn(List.of(998L, 999L));

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void selectInProjectNames() {
        List<Project> expected = List.of(ProjectFixture.project2000(), ProjectFixture.project2001());
        List<String> projectNames = expected.stream().map(Project::getProjectName).collect(Collectors.toList());
        List<Project> result = projectService.selectInProjectNames(projectNames);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void selectInProjectNames__whenNothingIsFound__thenReturnEmptyArray() {
        List<Project> result = projectService.selectInProjectNames(List.of("Test1", "Test2"));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
    }

    @Test
    void saveProjects() {
        //given
        List<Project> projectsToPersist = ProjectFixture.nonExistingProjects();
        String[] expectedName = projectsToPersist.stream().map(Project::getProjectName).toArray(String[]::new);
        //when
        List<Project> result = projectService.saveProjects(projectsToPersist);
        //then
        assertThat(result).hasSize(3);
        assertThat(result).extracting("id").containsExactly(2003L, 2004L, 2005L);
        assertThat(result).extracting("projectName").containsExactly(expectedName);
    }

    @Test
    void saveProjects__whenAlreadyExists__throwProjectExistsException() {
        List<Project> projectsToPersist = List.of(ProjectFixture.project2000(), ProjectFixture.project2001());

        assertThatThrownBy(() -> projectService.saveProjects(projectsToPersist))
                .isInstanceOf(ProjectExistsException.class)
                .hasMessage("Projects with names: 'Super project, Terrible project' already exists");
    }
}