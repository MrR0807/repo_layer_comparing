package lt.comparing.plainjdbc.entity;

import java.util.Objects;

public class Building {

    private final long id;
    private final String name;
    private final String address;

    public Building(long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return id == building.id &&
                Objects.equals(name, building.name) &&
                Objects.equals(address, building.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address);
    }

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}