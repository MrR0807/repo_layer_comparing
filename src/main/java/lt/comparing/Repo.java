package lt.comparing;

import lt.comparing.plainjdbc.entity.Employee;

public interface Repo {

    Employee getEmployeesFullGraph(long employeeId);

    Employee getEmployee(long employeeId);
}