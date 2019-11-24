package lt.comparing.plainjdbc;

public class EmployeeSQLStatements {

    private EmployeeSQLStatements() {
    }

    public static final String SELECT_EMPLOYEE = "" +
            "SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary, " +
            "e.employee_type e_employee_type " +
            "FROM company.employee e WHERE e.id = ?";

    public static final String SELECT_EMPLOYEES = "" +
            "SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary, " +
            "e.employee_type e_employee_type " +
            "FROM company.employee e";

    public static final String SELECT_EMPLOYEE_AND_PROJECTS = "" +
            "SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary, e.employee_type e_employee_type, " +
            "p.id p_id, p.project_name p_project_name " +
            "FROM company.employee e " +
            "INNER JOIN company.employee_project ep ON e.id = ep.employee_id " +
            "INNER JOIN company.project p ON ep.project_id = p.id " +
            "WHERE e.id = ?";

    public static final String SELECT_ALL_EMPLOYEES_AND_PROJECTS = "" +
            "SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary, e.employee_type e_employee_type, " +
            "p.id p_id, p.project_name p_project_name " +
            "FROM company.employee e " +
            "INNER JOIN company.employee_project ep ON e.id = ep.employee_id " +
            "INNER JOIN company.project p ON ep.project_id = p.id";
}
