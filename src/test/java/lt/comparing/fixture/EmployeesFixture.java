package lt.comparing.fixture;

import lt.comparing.plainjdbc.entity.Building;
import lt.comparing.plainjdbc.entity.Cubicle;
import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.entity.EmployeeType;
import lt.comparing.plainjdbc.entity.Project;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

public class EmployeesFixture {

    private EmployeesFixture() {
    }

    public static Set<Employee> employeesWithFullGraph() {
        Building building = new Building(1, "Big Building", "Address 1");
        Project project2000 = ProjectFixture.project2000();
        Project project2001 = ProjectFixture.project2001();
        Project project2002 = ProjectFixture.project2002();

        return Set.of(
                new Employee(1, "First1", "Last1", BigDecimal.valueOf(1000), EmployeeType.EMPLOYEE, new Cubicle(1000, building), List.of(project2000, project2002)),
                new Employee(2, "First2", "Last2", BigDecimal.valueOf(1000), EmployeeType.EMPLOYEE, new Cubicle(1001, building), List.of(project2000)),
                new Employee(3, "First3", "Last3", BigDecimal.valueOf(1000), EmployeeType.EMPLOYEE, new Cubicle(1002, building), List.of(project2001)),
                new Employee(4, "First4", "Last4", BigDecimal.valueOf(1000), EmployeeType.MANAGER, new Cubicle(1003, building), List.of(project2001)));
    }

    private static Employee createEmployee(long id, EmployeeType employeeType, Cubicle cubicle, List<Project> projects) {
        return new Employee(id, "First" + id, "Last" + id, BigDecimal.valueOf(1_000), employeeType, cubicle, projects);
    }

    public static EmployeeBuilder defaultEmployeeWithCubicle() {
        return EmployeeBuilder.anEmployee()
                .withId(1)
                .withFirstName("First1")
                .withLastName("Last1")
                .withSalary(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP))
                .withEmployeeType(EmployeeType.EMPLOYEE)
                .withCubicle(new Cubicle(1000, new Building(1, "Big Building", "Address 1")))
                .withProjects(List.of(ProjectFixture.project2000(), ProjectFixture.project2002()));
    }
}