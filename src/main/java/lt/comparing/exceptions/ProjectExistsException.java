package lt.comparing.exceptions;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectExistsException extends RuntimeException {
    public ProjectExistsException(List<String> projectNames) {
        super(String.format("Projects with names: %s already exists", formattedProjectNames(projectNames)));
    }

    private static String formattedProjectNames(List<String> projectNames) {
        return projectNames.stream().collect(Collectors.joining(", ", "'", "'"));
    }
}
