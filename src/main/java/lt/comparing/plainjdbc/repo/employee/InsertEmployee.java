package lt.comparing.plainjdbc.repo.employee;

import lt.comparing.plainjdbc.entity.Employee;
import lt.comparing.plainjdbc.repo.sqlfunction.InsertReturning;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertEmployee {

    private static final String INSERT_EMPLOYEE = """
            INSERT INTO company.employee (first_name, last_name, salary, employee_type, cubicle_id)
            VALUES (?, ?, ?, ?, ?)""";

    public static String preparedStatementSQL() {
        return INSERT_EMPLOYEE;
    }

    public static InsertReturning<List<Long>> insert(Employee employee) {
        return ps -> insertEmployee(employee, ps);
    }

    private static List<Long> insertEmployee(Employee employee, PreparedStatement ps) throws SQLException {
        setValues(employee, ps);
        ps.executeUpdate();
        return getGeneratedKeys(ps);
    }

    private static void setValues(Employee employee, PreparedStatement ps) throws SQLException {
        ps.setString(1, employee.getFirstName());
        ps.setString(2, employee.getLastName());
        ps.setBigDecimal(3, employee.getSalary());
        ps.setString(4, employee.getEmployeeType().toString());
        ps.setLong(5, employee.getCubicle().getId());
    }

    private static List<Long> getGeneratedKeys(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            List<Long> generatedKeys = new ArrayList<>();
            while (rs.next()) {
                generatedKeys.add(rs.getLong(1));
            }
            return generatedKeys;
        }
    }
}
