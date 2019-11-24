package lt.comparing.fixture;

import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class EmployeesFixture {

    private EmployeesFixture() {
    }

    public static List<Employee> employees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(defaultEmployee().build());

        for (int i = 2; i < 5; i++) {
            EmployeeBuilder employeeBuilder = defaultEmployee()
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
}