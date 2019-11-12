package lt.comparing.plainjdbc.entity;

import java.util.Objects;

public class Cubicle {

    private final long id;
    private final Building building;

    public Cubicle(long id, Building building) {
        this.id = id;
        this.building = building;
    }

    public long getId() {
        return id;
    }

    public Building getBuilding() {
        return building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cubicle cubicle = (Cubicle) o;
        return id == cubicle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cubicle{" +
                "id=" + id +
                ", building=" + building +
                '}';
    }
}