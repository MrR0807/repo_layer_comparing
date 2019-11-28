package lt.comparing.fixture;

import lt.comparing.plainjdbc.entity.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EmployeesFixture {

    private EmployeesFixture() {
    }

    public static Set<Employee> employees() {
        Set<Employee> employees = new LinkedHashSet<>();
        employees.add(defaultEmployee().build());

        for (int i = 2; i < 5; i++) {
            EmployeeBuilder employeeBuilder = defaultEmployeeWithCubicle()
                    .withId(i)
                    .withFirstName("First" + i)
                    .withLastName("Last" + i);

            if (i == 4) {
                employeeBuilder.withEmployeeType(EmployeeType.MANAGER);
            }
            employees.add(employeeBuilder.build());
        }
        return employees;
    }

    public static Set<Employee> employeesWithFullGraph() {
        Building building = new Building(1, "Big Building", "Address 1");
        Project project2001 = new Project(2001, "Super project");
        Project project2002 = new Project(2002, "Terrible project");
        Project project2003 = new Project(2003, "Average project");

        return Set.of(
                new Employee(1, "First1", "Last1", BigDecimal.valueOf(1000), EmployeeType.EMPLOYEE, new Cubicle(1001, building), List.of(project2001, project2003)),
                new Employee(2, "First2", "Last2", BigDecimal.valueOf(1000), EmployeeType.EMPLOYEE, new Cubicle(1002, building), List.of(project2001)),
                new Employee(3, "First3", "Last3", BigDecimal.valueOf(1000), EmployeeType.EMPLOYEE, new Cubicle(1003, building), List.of(project2002)),
                new Employee(4, "First4", "Last4", BigDecimal.valueOf(1000), EmployeeType.MANAGER, new Cubicle(1004, building), List.of(project2002)));
    }

    public static EmployeeBuilder defaultEmployee() {
        return EmployeeBuilder.anEmployee()
                .withId(1)
                .withFirstName("First1")
                .withLastName("Last1")
                .withSalary(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP))
                .withEmployeeType(EmployeeType.EMPLOYEE)
                .withCubicle(null)
                .withProjects(new ArrayList<>());
    }

    public static EmployeeBuilder defaultEmployeeWithCubicle() {
        return EmployeeBuilder.anEmployee()
                .withId(1)
                .withFirstName("First1")
                .withLastName("Last1")
                .withSalary(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP))
                .withEmployeeType(EmployeeType.EMPLOYEE)
                .withCubicle(new Cubicle(1001, new Building(1, "Big Building", "Address 1")))
                .withProjects(new ArrayList<>());
    }
}