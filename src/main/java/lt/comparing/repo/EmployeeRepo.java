package lt.comparing.repo;

import lt.comparing.plainjdbc.entity.Employee;

import java.util.Optional;
import java.util.Set;

public interface EmployeeRepo {

    Optional<Employee> getEmployee(long employeeId);

    Set<Employee> getEmployees();

    Optional<Employee> getEmployeeFullGraph(long employeeId);

    Set<Employee> getEmployeesFullGraph();

    void saveEmployeeFullGraph(Employee employee);

    void updateEmployeeFullGraph(Employee employee);
}
