package id.apollo.luckynetwork.kratos.kratos.objects;

import id.apollo.luckynetwork.kratos.kratos.objects.objects.IUsedPrefix;

public class CustomPrefix implements IUsedPrefix {
    private int id;
    private String prefixString;
    private long timelimit;
    private boolean main;

    public CustomPrefix(int id, String prefixString, long timelimit, boolean main) {
        this.id = id;
        this.prefixString = prefixString;
        this.timelimit = timelimit;
        this.main = main;
    }

    public int getId() {
        return id;
    }

    public String getPrefixString() {
        return prefixString;
    }

    public long getTimelimit() {
        return timelimit;
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public boolean isMain() {
        return main;
    }
}
