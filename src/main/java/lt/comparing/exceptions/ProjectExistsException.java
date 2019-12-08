package lt.comparing.exceptions;

import java.util.List;

public class ProjectExistsException extends RuntimeException {
    public ProjectExistsException(List<String> projectNames) {
        super("Projects with names: " + String.join(",", projectNames) + " already exists");
    }
}
