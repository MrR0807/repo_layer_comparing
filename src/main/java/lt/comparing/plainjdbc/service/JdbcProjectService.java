package lt.comparing.plainjdbc.service;

import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.JdbcProjectRepo;

import java.util.Collection;
import java.util.List;

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
        return null;
    }
}
