package lt.comparing;

import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.Project;

import java.util.List;

public interface Repo {

    Employee getEmployee(long employeeId);

    List<Employee> getEmployees();

    Employee getEmployeeFullGraph(long employeeId);

    List<Employee> getEmployeesFullGraph();

    Project getProject(long projectId);
}