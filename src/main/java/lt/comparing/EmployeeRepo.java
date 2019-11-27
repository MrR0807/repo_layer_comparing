package lt.comparing;

import lt.comparing.plainjdbc.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepo {

    Optional<Employee> getEmployee(long employeeId);

    List<Employee> getEmployees();

    Optional<Employee> getEmployeeFullGraph(long employeeId);

    List<Employee> getEmployeesFullGraph();

    void saveEmployeeFullGraph(Employee employee);

    void updateEmployeeFullGraph(Employee employee);
}
