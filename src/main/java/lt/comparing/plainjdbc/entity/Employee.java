package lt.comparing.plainjdbc.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Employee {

    private final long id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final EmployeeType employeeType;
    private Cubicle cubicle;

    public Employee(long id, String firstName, String lastName, BigDecimal salary, EmployeeType employeeType, Cubicle cubicle) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.employeeType = employeeType;
        this.cubicle = cubicle;
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