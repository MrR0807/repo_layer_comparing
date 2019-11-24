package lt.comparing;

import lt.comparing.plainjdbc.entity.Project;

public interface ProjectRepo {

    Project getProject(long projectId);
}
