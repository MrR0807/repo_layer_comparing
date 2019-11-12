INSERT INTO company.building (id, name, address)
VALUES (1, 'Big Building', 'Address 1');

INSERT INTO company.cubicle (id, building_id)
VALUES
(1001, 1),
(1002, 1),
(1003, 1),
(1004, 1);

INSERT INTO company.employee (id, first_name, last_name, salary, employee_type, cubicle_id)
VALUES
(1, 'First1', 'Last1', 1000, 'EMPLOYEE', 1001),
(2, 'First2', 'Last2', 1000, 'EMPLOYEE', 1002),
(3, 'First3', 'Last3', 1000, 'EMPLOYEE', 1003),
(4, 'First4', 'Last4', 1000, 'MANAGER', 1004);

INSERT INTO company.project (id, project_name)
VALUES
(2001, 'Super project'),
(2002, 'Terrible project');

INSERT INTO company.employee_project (employee_id, project_id)
VALUES
(1, 2001),
(2, 2001),
(3, 2002),
(4, 2002);