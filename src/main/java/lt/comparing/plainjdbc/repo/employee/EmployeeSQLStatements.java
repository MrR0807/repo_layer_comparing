package lt.comparing.plainjdbc.repo.employee;

public class EmployeeSQLStatements {

    private EmployeeSQLStatements() {
    }

    static final String SELECT_EMPLOYEE = """
            SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary,
            e.employee_type e_employee_type
            FROM company.employee e WHERE e.id = ?""";

    static final String SELECT_EMPLOYEES = """
            SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary,
            e.employee_type e_employee_type
            FROM company.employee e""";

    static final String SELECT_EMPLOYEE_FULL_GRAPH = """
            SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary, e.employee_type e_employee_type,
            p.id p_id, p.project_name p_project_name,
            c.id c_id,
            b.id b_id, b.name b_name, b.address b_address
            FROM company.employee e
            INNER JOIN company.employee_project ep ON e.id = ep.employee_id
            INNER JOIN company.project p ON ep.project_id = p.id
            INNER JOIN company.cubicle c ON e.cubicle_id = c.id
            INNER JOIN company.building b ON c.building_id = b.id
            WHERE e.id = ?""";

    static final String SELECT_ALL_EMPLOYEES_FULL_GRAPH = """
            SELECT e.id e_id, e.first_name e_first_name, e.last_name e_last_name, e.salary e_salary, e.employee_type e_employee_type,
            p.id p_id, p.project_name p_project_name,
            c.id c_id,
            b.id b_id, b.name b_name, b.address b_address
            FROM company.employee e
            INNER JOIN company.employee_project ep ON e.id = ep.employee_id
            INNER JOIN company.project p ON ep.project_id = p.id
            INNER JOIN company.cubicle c ON e.cubicle_id = c.id
            INNER JOIN company.building b ON c.building_id = b.id""";

    static final String INSERT_EMPLOYEE = """
            INSERT INTO company.employee (first_name, last_name, salary, employee_type, cubicle_id)
            VALUES (?, ?, ?, ?, ?)""";
}