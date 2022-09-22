package id.apollo.luckynetwork.kratos.kratos.manager;

import id.apollo.luckynetwork.kratos.kratos.objects.PlayerPrefix;
import id.apollo.luckynetwork.kratos.kratos.objects.Prefix;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class PrefixManager {

    private GlobalManager globalManager;
    private ConcurrentHashMap<Integer, Prefix> prefixes = new ConcurrentHashMap<>();

    public PrefixManager(GlobalManager globalManager) {
        this.globalManager = globalManager;
        loadPrefixes();
    }

    public Prefix getPrefix(int id) {
        return prefixes.get(id);
    }

    public List<Prefix> getPrefixList()
    {
        return new ArrayList<>(prefixes.values());
    }

    public Prefix getPrefix(String name) {
        for (Prefix prefix : prefixes.values()) {
            if (prefix.getName().equalsIgnoreCase(name)) {
                return prefix;
            }
        }
        return null;
    }

    public Prefix getPrefixByString(String prefixString)
    {
        for(Prefix prefix : prefixes.values())
        {
            if(prefix.getString().equalsIgnoreCase(prefixString))
            {
                return prefix;
            }
        }
        return null;
    }

    public void loadPrefixes()
    {
        globalManager.getKratos().getDataLoaderService().execute(() -> {
            prefixes.clear();
            addDefaultPrefix();

            try(Connection connection = globalManager.getKratos().getMySQL().getConnection(); Statement statement = connection.createStatement())
            {

                String SQL = "SELECT `prefixId`, `prefixName`, `prefixString`, `prefixPermission` from prefixes;";

                try (ResultSet resultSet = statement.executeQuery(SQL))
                {
                    int prefixId;
                    String prefixName;
                    String prefixString;
                    String prefixPermission;
                    while(resultSet.next()) {
                        prefixId = resultSet.getInt("prefixId");
                        prefixName = resultSet.getString("prefixName");
                        prefixString = resultSet.getString("prefixString");
                        prefixPermission = resultSet.getString("prefixPermission");

                        Prefix prefix = new Prefix(prefixId, prefixName, prefixString, prefixPermission);
                        prefixes.put(prefixId, prefix);
                    }
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                globalManager.getKratos().getLogger().log(Level.SEVERE, "[KRATOS PANIC] Error loading Prefixes");
            }

        });
    }

    public void addDefaultPrefix()
    {
        addPrefix(new Prefix(0, "DEFAULT", "&7", "NONE"));
    }

    public void addPrefix(Prefix prefix) {
        prefixes.put(prefix.getId(), prefix);
    }

    public boolean isExpired(PlayerPrefix playerPrefix) {
        if (playerPrefix.getTimelimit() == -1) {
            return false;
        }
        return playerPrefix.getTimelimit() < System.currentTimeMillis();
    }

    public boolean isLocked(PlayerPrefix playerPrefix) {
        return playerPrefix.isLocked();
    }
    
}
