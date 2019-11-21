package lt.comparing;

import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.Project;

public interface Repo {

    Employee getEmployee(long employeeId);

    Employee getEmployeeFullGraph(long employeeId);

    Project getProject(long projectId);
}