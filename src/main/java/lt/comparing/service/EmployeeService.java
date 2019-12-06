package lt.comparing.service;

import lt.comparing.plainjdbc.entity.Employee;

import java.util.Set;

public interface EmployeeService {

    Employee getEmployee(long employeeId);

    Set<Employee> getEmployees();

    Employee getEmployeeFullGraph(long employeeId);

    Set<Employee> getEmployeesFullGraph();

    long saveEmployeeFullGraph(Employee employee);

    void updateEmployeeFullGraph(Employee employee);
}