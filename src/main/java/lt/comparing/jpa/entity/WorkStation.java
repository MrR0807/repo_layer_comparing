package lt.comparing.jpa.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class WorkStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "employee")
    private Employee employee;

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkStation that = (WorkStation) o;
        return Objects.nonNull(id) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WorkStation{" +
                "id=" + id +
                '}';
    }
}