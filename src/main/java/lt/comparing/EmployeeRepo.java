package lt.comparing;

import lt.comparing.plainjdbc.entity.Employee;

import java.util.List;

public interface EmployeeRepo {

    Employee getEmployee(long employeeId);

    List<Employee> getEmployees();

    Employee getEmployeeFullGraph(long employeeId);

    List<Employee> getEmployeesFullGraph();
}
