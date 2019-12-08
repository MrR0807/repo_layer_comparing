package lt.comparing.plainjdbc.service;

import lt.comparing.exceptions.ProjectExistsException;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.JdbcProjectRepo;
import org.apache.commons.collections4.ListUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcProjectService {

    private final JdbcProjectRepo projectRepo;

    public JdbcProjectService(JdbcProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    public List<Project> selectIn(Collection<Long> projectIds) {
        return projectRepo.selectIn(projectIds);
    }

    public List<Project> selectInProjectNames(Collection<String> projectNames) {
        return projectRepo.selectInProjectNames(projectNames);
    }

    public List<Project> saveProjects(List<Project> projects) {
        ifFoundThrowProjectExistsException(projects);

        return projectRepo.save(projects);
    }

    private void ifFoundThrowProjectExistsException(List<Project> projects) {
        List<Project> foundProject = selectInProjectNames(getProjectNames(projects));
        if (!foundProject.isEmpty()) {
            List<Project> alreadyExistsProjects = ListUtils.intersection(projects, foundProject);
            throw new ProjectExistsException(getProjectNames(alreadyExistsProjects));
        }
    }

    private List<String> getProjectNames(List<Project> projects) {
        return projects.stream()
                    .map(Project::getProjectName)
                    .collect(Collectors.toList());
    }
}
