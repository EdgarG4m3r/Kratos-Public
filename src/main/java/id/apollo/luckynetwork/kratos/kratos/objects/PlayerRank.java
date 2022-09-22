package id.apollo.luckynetwork.kratos.kratos.objects;

public class PlayerRank {

    private Rank rank;
    private long timeLimit;
    private boolean locked;
    private long created;

    public PlayerRank(Rank defaultRank) {
        this(defaultRank, -1, false, 1);
    }

    public PlayerRank(Rank rank, long timeLimit, boolean locked, long created) {
        this.rank = rank;
        this.timeLimit = timeLimit;
        this.locked = locked;
        this.created = created;
    }

    public Rank getRank() {
        return rank;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    /**
     * Added for compatibility reasons. HighPerformanceManager won't load locked rank.
     */
    public boolean isLocked() {
        return locked;
    }

    public long getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "PlayerRank{" +
                "rank=" + rank +
                ", timeLimit=" + timeLimit +
                ", locked=" + locked +
                ", created=" + created +
                '}';
    }
}
