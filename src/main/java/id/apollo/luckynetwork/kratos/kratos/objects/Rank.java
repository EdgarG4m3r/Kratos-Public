package id.apollo.luckynetwork.kratos.kratos.objects;

public class Rank {

    private int id;
    private String name;
    private String prefix;
    private boolean isProtected;

    public Rank(int id, String name, String prefix, boolean isProtected) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.isProtected = isProtected;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isProtected() {
        return isProtected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rank rank = (Rank) o;
        return id == rank.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", isProtected=" + isProtected +
                '}';
    }
}
