INSERT INTO company.building (name, address)
VALUES ('Big Building', 'Address 1');

INSERT INTO company.cubicle (id, building_id)
VALUES
(1000, 1),
(1001, 1),
(1002, 1),
(1003, 1),
(1004, 1);

INSERT INTO company.employee (first_name, last_name, salary, employee_type, cubicle_id)
VALUES
('First1', 'Last1', 1000, 'EMPLOYEE', 1000),
('First2', 'Last2', 1000, 'EMPLOYEE', 1001),
('First3', 'Last3', 1000, 'EMPLOYEE', 1002),
('First4', 'Last4', 1000, 'MANAGER', 1003);

INSERT INTO company.project (project_name)
VALUES
('Super project'),
('Terrible project'),
('Average project');

INSERT INTO company.employee_project (employee_id, project_id)
VALUES
(1, 2000),
(1, 2002),
(2, 2000),
(3, 2001),
(4, 2001);