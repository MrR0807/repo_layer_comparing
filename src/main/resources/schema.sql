CREATE SCHEMA company;

CREATE TABLE company.employee (
id                  BIGINT          NOT NULL auto_increment,
first_name          VARCHAR (255)   NOT NULL,
last_name           VARCHAR (255)   NOT NULL,
salary              DECIMAL (19, 2) NOT NULL,
employee_type       VARCHAR (255)   NOT NULL,
cubicle_id          BIGINT          NOT NULL,

CONSTRAINT PK__employee_id PRIMARY KEY (id)
);

CREATE TABLE company.project (
id              BIGINT        NOT NULL auto_increment,
project_name    VARCHAR (255) NOT NULL,

CONSTRAINT PK__project_id PRIMARY KEY (id)
);

CREATE TABLE company.employee_project (
employee_id BIGINT NOT NULL,
project_id  BIGINT NOT NULL,

CONSTRAINT PK__employee_id__project_id PRIMARY KEY (employee_id, project_id)
);

CREATE TABLE company.cubicle (
id          BIGINT NOT NULL auto_increment,
building_id BIGINT NOT NULL,

CONSTRAINT PK__cubicle_id PRIMARY KEY (id)
);

CREATE TABLE company.building(
id          BIGINT NOT NULL auto_increment,
name        VARCHAR (255) NOT NULL,
address     VARCHAR (255) NOT NULL,

CONSTRAINT PK__building_id PRIMARY KEY (id)
);

ALTER TABLE company.employee_project
    ADD CONSTRAINT FK__employee_project__employee FOREIGN KEY (employee_id) REFERENCES company.employee (id);

ALTER TABLE company.employee_project
    ADD CONSTRAINT FK__employee_project__project FOREIGN KEY (project_id) REFERENCES company.project (id);

ALTER TABLE company.employee
    ADD CONSTRAINT FK__employee__cubicle FOREIGN KEY (cubicle_id) REFERENCES company.cubicle (id);

ALTER TABLE company.cubicle
    ADD CONSTRAINT FK__cubicle__building FOREIGN KEY (building_id) REFERENCES company.building (id);