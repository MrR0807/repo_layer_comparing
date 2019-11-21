package lt.comparing.fixture;

import lt.comparing.plainjdbc.entity.Cubicle;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.plainjdbc.entity.Project;

import java.math.BigDecimal;
import java.util.List;

public final class EmployeeBuilder {
    private long id;
    private String firstName;
    private String lastName;
    private BigDecimal salary;
    private EmployeeType employeeType;
    private Cubicle cubicle;
    private List<Project> projects;

    private EmployeeBuilder() {
    }

    public static EmployeeBuilder anEmployee() {
        return new EmployeeBuilder();
    }

    public EmployeeBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public EmployeeBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public EmployeeBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public EmployeeBuilder withSalary(BigDecimal salary) {
        this.salary = salary;
        return this;
    }

    public EmployeeBuilder withEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
        return this;
    }

    public EmployeeBuilder withCubicle(Cubicle cubicle) {
        this.cubicle = cubicle;
        return this;
    }

    public EmployeeBuilder withProjects(List<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Employee build() {
        return new Employee(id, firstName, lastName, salary, employeeType, cubicle, projects);
    }
}
