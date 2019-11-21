package lt.comparing.plainjdbc.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Employee {

    private final long id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final EmployeeType employeeType;
    private final Cubicle cubicle;
    private final List<Project> projects;

    public Employee(long id, String firstName, String lastName, BigDecimal salary, EmployeeType employeeType, Cubicle cubicle, List<Project> projects) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.employeeType = employeeType;
        this.cubicle = cubicle;
        this.projects = Objects.requireNonNullElse(projects, new ArrayList<>());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public Cubicle getCubicle() {
        return cubicle;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void addProject(Project project) {
        this.projects.add(project);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", employeeType=" + employeeType +
                ", cubicle=" + cubicle +
                '}';
    }
}