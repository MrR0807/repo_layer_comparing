package lt.comparing.plainjdbc.service;

import lt.comparing.exceptions.EmployeeNotFoundException;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.Project;
import lt.comparing.plainjdbc.repo.JdbcEmployeeRepo;
import lt.comparing.plainjdbc.repo.JdbcProjectRepo;
import lt.comparing.service.EmployeeService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcEmployeeService implements EmployeeService {

    private final JdbcEmployeeRepo employeeRepoe;
    private final JdbcProjectRepo projectRepo;

    public JdbcEmployeeService(JdbcEmployeeRepo employeeRepoe, JdbcProjectRepo projectRepo) {
        this.employeeRepoe = employeeRepoe;
        this.projectRepo = projectRepo;
    }

    @Override
    public Employee getEmployee(long employeeId) {
        return employeeRepoe.getEmployee(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    @Override
    public Set<Employee> getEmployees() {
        return employeeRepoe.getEmployees();
    }

    @Override
    public Employee getEmployeeFullGraph(long employeeId) {
        return employeeRepoe.getEmployeeFullGraph(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    @Override
    public Set<Employee> getEmployeesFullGraph() {
        return employeeRepoe.getEmployeesFullGraph();
    }

    @Override
    public long saveEmployeeFullGraph(Employee employee) {
        List<Long> projectIds = employee.getProjects().stream()
                .map(Project::getId)
                .collect(Collectors.toList());
        List<Project> projects = projectRepo.selectIn(projectIds);



        return employeeRepoe.saveEmployee(employee);
    }

    @Override
    public void updateEmployeeFullGraph(Employee employee) {

    }
}
