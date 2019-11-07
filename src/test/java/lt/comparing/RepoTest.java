package lt.comparing;

import lt.comparing.plainjdbc.RepoJDBC;
import lt.comparing.utils.H2Launcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RepoTest {

    private static Repo repo;
    private static H2Launcher h2Launcher;

    @BeforeAll
    static void initialize() {
        h2Launcher = new H2Launcher();
        repo = new RepoJDBC();
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
        String result = repo.get();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("goodbye");
    }

    @Test
    void testAgain() {
        String result = repo.get();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("goodbye");
    }
}