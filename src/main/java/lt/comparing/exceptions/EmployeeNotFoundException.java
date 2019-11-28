package lt.comparing.exceptions;

import static java.lang.String.format;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(long employeeId) {
        super(format("Employee with id %d not found", employeeId));
    }
}
