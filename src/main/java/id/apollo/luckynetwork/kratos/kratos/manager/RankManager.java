package id.apollo.luckynetwork.kratos.kratos.manager;

import id.apollo.luckynetwork.kratos.kratos.objects.PlayerRank;
import id.apollo.luckynetwork.kratos.kratos.objects.Rank;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class RankManager {

    private GlobalManager globalManager;
    private ConcurrentHashMap<Integer, Rank> ranks = new ConcurrentHashMap<>();

    public RankManager(GlobalManager globalManager) {
        this.globalManager = globalManager;
        addDefaultRank();
        loadRanks();
    }

    public Rank getRank(int id) {
        return ranks.get(id);
    }

    public Rank getRank(String name) {
        for (Rank rank : ranks.values()) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }


    private void loadRanks() {

        globalManager.getKratos().getDataLoaderService().execute(() -> {
            ranks.clear();
            addDefaultRank();

            try(Connection connection = globalManager.getKratos().getMySQL().getConnection(); Statement statement = connection.createStatement())
            {

                String SQL = "SELECT `rankId`, `rankName`, `rankPrefix`, `protected` from ranks;";

                try(ResultSet resultSet = connection.createStatement().executeQuery(SQL))
                {
                    int rankId;
                    String rankName;
                    String rankPrefix;
                    boolean protectedRank;

                    while (resultSet.next())
                    {
                        rankId = resultSet.getInt("rankId");
                        rankName = resultSet.getString("rankName");
                        rankPrefix = resultSet.getString("rankPrefix");
                        protectedRank = resultSet.getBoolean("protected");

                        ranks.put(rankId, new Rank(rankId, rankName, rankPrefix, protectedRank));
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                globalManager.getKratos().getLogger().log(Level.SEVERE, "[KRATOS PANIC] Error loading ranks");
            }
        });
    }

    public void addDefaultRank() {
        addRank(new Rank(0, "DEFAULT", "&7", false));
    }

    public void addRank(Rank rank) {
        ranks.put(rank.getId(), rank);
    }

    public void removeRank(int id) {
        ranks.remove(id);
    }

    public void removeRank(Rank rank) {
        ranks.remove(rank.getId());
    }

    public boolean isExpired(PlayerRank playerRank)
    {
        if (playerRank.getTimeLimit() == -1) {
            return false;
        }
        return playerRank.getTimeLimit() < System.currentTimeMillis();
    }

    public boolean isLocked(PlayerRank playerRank)
    {
        return playerRank.isLocked();
    }

    public boolean isProtected(PlayerRank playerRank)
    {
        return playerRank.getRank().isProtected();
    }

    public boolean isValid(PlayerRank playerRank)
    {
        if (isExpired(playerRank)) { return false; }
        if (isLocked(playerRank)) { return false; }
        return true;
    }


}
