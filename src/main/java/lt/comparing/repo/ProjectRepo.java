package lt.comparing.repo;

import lt.comparing.plainjdbc.entity.Project;

public interface ProjectRepo {

    Project getProject(long projectId);
}
