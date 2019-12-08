package lt.comparing.fixture;

import lt.comparing.plainjdbc.entity.Project;

import java.util.List;

public class ProjectFixture {

    private ProjectFixture() {
    }

    public static List<Project> projects() {
        return List.of(project2000(), project2001(), project2002());
    }

    public static Project project2000() {
        return new Project(2000, "Super project");
    }

    public static Project project2001() {
        return new Project(2001, "Terrible project");
    }

    public static Project project2002() {
        return new Project(2002, "Average project");
    }

    public static Project nonExistingProject() {
        return new Project(999, "New project one");
    }

    public static List<Project> nonExistingProjects() {
        return List.of(
                new Project(999, "New project one"),
                new Project(999, "New project two"),
                new Project(999, "New project three"));
    }
}
