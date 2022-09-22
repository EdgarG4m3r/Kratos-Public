package id.apollo.luckynetwork.kratos.kratos.objects;

import id.apollo.luckynetwork.kratos.kratos.objects.objects.IUsedPrefix;

import java.util.ArrayList;
import java.util.List;

public class PlayerPrefix implements IUsedPrefix {

    private Prefix prefix;
    private long timeLimit;
    private boolean locked;
    private long created;

    private boolean main;

    public PlayerPrefix(Prefix defaultPrefix)
    {
        this(defaultPrefix, -1, false, 1, true);
    }

    public PlayerPrefix(Prefix prefix, long timeLimit, boolean locked, long created, boolean main) {
        this.prefix = prefix;
        this.timeLimit = timeLimit;
        this.locked = locked;
        this.created = created;
        this.main = main;
    }

    public Prefix getPrefix() {
        return prefix;
    }

    @Override
    public long getTimelimit() {
        return timeLimit;
    }

    @Override
    public int getId() {
        return prefix.getId();
    }

    @Override
    public String getPrefixString() { return prefix.getString(); }

    @Override
    public boolean isLocked() {
        return locked;
    }

    public long getCreated() {
        return created;
    }

    @Override
    public boolean isMain() {
        return main;
    }

    @Override
    public String toString() {
        return "PlayerPrefix{" +
                "prefix=" + prefix +
                ", timeLimit=" + timeLimit +
                ", locked=" + locked +
                ", created=" + created +
                ", main=" + main +
                '}';
    }

}
