package id.apollo.luckynetwork.kratos.kratos.objects;

import java.util.Objects;

public class Prefix {

    private int id;
    private String name;
    private String string;
    private String permission;

    public Prefix(int id, String name, String string, String permission) {
        this.id = id;
        this.name = name;
        this.string = string;
        this.permission = permission;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getString() {
        return string;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prefix prefix = (Prefix) o;
        return id == prefix.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Prefix{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", string='" + string + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }
}
