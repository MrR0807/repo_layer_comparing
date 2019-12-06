package lt.comparing.plainjdbc.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Project {

    private final long id;
    private final String projectName;
    private List<Employee> employees = new ArrayList<>();

    public Project(long id, String projectName) {
        this.id = id;
        this.projectName = projectName;
    }

    public long getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id && Objects.equals(projectName, project.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectName);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}