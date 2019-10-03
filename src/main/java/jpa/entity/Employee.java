package jpa.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "manager")
    private Set<Employee> employees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToOne
    private WorkStation workStation;

    @ManyToMany(mappedBy = "employees")
    private Set<Project> projects = new HashSet<>();

    public void addEmployee(Employee employee) {
        employee.manager = this;
        this.employees.add(employee);
    }

    public void fireEmployee(Employee employee) {
        employee.manager = null;
        this.employees.remove(employee);
    }

    public void joinProject(Project project) {
        project.getEmployees().add(this);
        this.projects.add(project);
    }

    public void leaveProject(Project project) {
        project.getEmployees().remove(this);
        this.projects.remove(project);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Employee getManager() {
        return manager;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.nonNull(id) && Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}