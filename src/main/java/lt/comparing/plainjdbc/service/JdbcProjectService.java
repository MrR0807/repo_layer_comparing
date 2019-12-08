package lt.comparing.plainjdbc.service;

import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.JdbcProjectRepo;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcProjectService {

    private final JdbcProjectRepo projectRepo;

    public JdbcProjectService(JdbcProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    public List<Project> selectIn(Collection<Long> projectIds) {
        return projectRepo.selectIn(projectIds);
    }

    public List<Project> saveProjects(List<Project> projects) {
        return null;
    }
}
