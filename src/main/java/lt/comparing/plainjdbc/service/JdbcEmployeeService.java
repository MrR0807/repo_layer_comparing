package lt.comparing.plainjdbc.service;

import lt.comparing.exceptions.EmployeeNotFoundException;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.repo.JdbcEmployeeRepo;
import lt.comparing.service.EmployeeService;

import java.util.Set;

public class JdbcEmployeeService implements EmployeeService {

    private JdbcEmployeeRepo repo;

    public JdbcEmployeeService(JdbcEmployeeRepo repo) {
        this.repo = repo;
    }

    @Override
    public Employee getEmployee(long employeeId) {
        return repo.getEmployee(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    @Override
    public Set<Employee> getEmployees() {
        return repo.getEmployees();
    }

    @Override
    public Employee getEmployeeFullGraph(long employeeId) {
        return repo.getEmployeeFullGraph(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    @Override
    public Set<Employee> getEmployeesFullGraph() {
        return repo.getEmployeesFullGraph();
    }

    @Override
    public void saveEmployeeFullGraph(Employee employee) {

    }

    @Override
    public void updateEmployeeFullGraph(Employee employee) {

    }
}
