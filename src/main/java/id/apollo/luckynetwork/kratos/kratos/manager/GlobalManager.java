package id.apollo.luckynetwork.kratos.kratos.manager;

import id.apollo.luckynetwork.kratos.kratos.Kratos;
import id.apollo.luckynetwork.kratos.kratos.events.KratosPlayerUpdateEvent;
import id.apollo.luckynetwork.kratos.kratos.objects.*;
import id.apollo.luckynetwork.kratos.kratos.objects.objects.IUsedPrefix;
import org.bukkit.Bukkit;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class GlobalManager {

    private Kratos kratos;
    private PrefixManager prefixManager;
    private RankManager rankManager;
    private ConcurrentHashMap<UUID, KratosPlayer> players = new ConcurrentHashMap<>();

    public GlobalManager(Kratos kratos) {
        this.kratos = kratos;
        this.prefixManager = new PrefixManager(this);
        this.rankManager = new RankManager(this);
    }

    public boolean isPlayerLoaded(UUID uuid) {
        return players.containsKey(uuid);
    }

    public KratosPlayer loadPlayerandGet(UUID uuid) throws ExecutionException, InterruptedException {

        if (players.containsKey(uuid)) {
            return players.get(uuid);
        }

        CompletableFuture<KratosPlayer> completableFuture = CompletableFuture.supplyAsync(() -> loadPlayer(uuid), kratos.getDataLoaderService());

        return completableFuture.get();
    }

    public KratosPlayer loadPlayer(UUID uuid) {
        long startTime = System.nanoTime();
        PlayerRank playerRank = loadPlayerRank(uuid);
        List<PlayerPrefix> playerPrefixes = loadPlayerPrefixes(uuid);
        IUsedPrefix mainPlayerPrefix = new PlayerPrefix(getPrefixManager().getPrefix(0));
        CustomPrefix customPrefix = loadCustomPrefixes(uuid);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        if (duration > 1) {
            kratos.getLogger().log(Level.WARNING, "Player " + uuid + " took " + duration + "ms to load!");
            kratos.getLogger().log(Level.WARNING, "If you are not using VORD, please switch the database engine to VORD to avoid queueing.");
            NeoLogger.logPanic(kratos, "WARNING", "Player " + uuid + " took " + duration + "ms to load!");
        }
        for (PlayerPrefix playerPrefix : playerPrefixes) {
            if (playerPrefix.isMain()) {
                mainPlayerPrefix = playerPrefix;
            }
        }
        if (customPrefix.getId() != 0)
        {
            if (customPrefix.isMain())
            {
                mainPlayerPrefix = customPrefix;
            }
            KratosPlayer kratosPlayer = new KratosPlayer(uuid, playerRank, mainPlayerPrefix, playerPrefixes, customPrefix);
            players.put(uuid, kratosPlayer);
            return kratosPlayer;
        }

        KratosPlayer kratosPlayer = new KratosPlayer(uuid, playerRank, mainPlayerPrefix, playerPrefixes);


        players.put(uuid, kratosPlayer);
        return kratosPlayer;
    }

    public void reloadPlayer(UUID uuid) {
        kratos.getDataLoaderService().execute(() -> {
            PlayerRank playerRank = loadPlayerRank(uuid);
            List<PlayerPrefix> playerPrefixes = loadPlayerPrefixes(uuid);
            IUsedPrefix mainPlayerPrefix = new PlayerPrefix(getPrefixManager().getPrefix(0));
            CustomPrefix customPrefix = loadCustomPrefixes(uuid);
            for (PlayerPrefix playerPrefix : playerPrefixes) {
                if (playerPrefix.isMain()) {
                    mainPlayerPrefix = playerPrefix;
                }
            }
            if (customPrefix.getId() != 0)
            {
                if (customPrefix.isMain())
                {
                    mainPlayerPrefix = customPrefix;
                }
                KratosPlayer kratosPlayer = new KratosPlayer(uuid, playerRank, mainPlayerPrefix, playerPrefixes, customPrefix);
                players.put(uuid, kratosPlayer);
                Bukkit.getScheduler().runTask(kratos, () -> Bukkit.getPluginManager().callEvent(new KratosPlayerUpdateEvent(kratosPlayer)));
                return;
            }
            KratosPlayer kratosPlayer = new KratosPlayer(uuid, playerRank, mainPlayerPrefix, playerPrefixes);
            players.replace(uuid, kratosPlayer);
            Bukkit.getScheduler().runTask(kratos, () -> Bukkit.getPluginManager().callEvent(new KratosPlayerUpdateEvent(kratosPlayer)));
        });
    }

    public void unloadPlayer(UUID uuid) {
        if (players.containsKey(uuid)) {
            players.remove(uuid);
        }
    }

    public void unloadOfflinePlayers()
    {
        for (UUID uuid : players.keySet())
        {
            if (!Bukkit.getOfflinePlayer(uuid).isOnline())
            {
                unloadPlayer(uuid);
            }
        }
    }

    private List<PlayerPrefix> loadPlayerPrefixes(UUID uuid) {
        List<PlayerPrefix> loadedPrefixes = new ArrayList<>();

        String SQL = "SELECT `relationsId`, `prefixId`, `timelimit`, `locked`, `created`, `main` FROM playerPrefixes WHERE `playerUUID` = ?;";

        try(Connection connection = kratos.getMySQL().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setString(1, uuid.toString());
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                int relationsId = 0;
                int prefixId = 0;
                long timelimit = -1;
                boolean locked = false;
                long created = 1;
                boolean main = false;

                while (resultSet.next())
                {
                    relationsId = resultSet.getInt("relationsId");
                    prefixId = resultSet.getInt("prefixId");
                    timelimit = resultSet.getLong("timelimit");
                    locked = resultSet.getBoolean("locked");
                    created = resultSet.getLong("created");
                    main = resultSet.getBoolean("main");
                    loadedPrefixes.add(new PlayerPrefix(prefixManager.getPrefix(prefixId), timelimit, locked, created, main));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return loadedPrefixes;
    }

    //load CustomPrefix
    private CustomPrefix loadCustomPrefixes(UUID uuid) {

        CustomPrefix customPrefixObject = new CustomPrefix(0, "&7", -1, false);

        String SQL = "SELECT `customPrefixId`, `playerUUID`, `prefixString`, `timeLimit`, `main` FROM customPrefixes WHERE `playerUUID` = ?;";
        try(Connection connection = kratos.getMySQL().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setString(1, uuid.toString());
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                int customPrefixId = 0;
                String prefixString = "&7";
                long timelimit = -1;
                boolean main = false;
                while (resultSet.next())
                {
                    customPrefixId = resultSet.getInt("customPrefixId");
                    prefixString = resultSet.getString("prefixString");
                    timelimit = resultSet.getLong("timeLimit");
                    main = resultSet.getBoolean("main");
                }

                customPrefixObject = new CustomPrefix(customPrefixId, prefixString, timelimit, main);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return customPrefixObject;
    }

    private PlayerRank loadPlayerRank(UUID uuid) {
        PlayerRank playerRank = new PlayerRank(getRankManager().getRank(0));

        String SQL = "SELECT `relationsId`, `rankId`, `timelimit`, `locked`, `created` FROM playerRank WHERE `playerUUID` = ?;";
        try(Connection connection = kratos.getMySQL().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setString(1, uuid.toString());
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                int rankId = 0;
                long timelimit = -1;
                boolean locked = false;
                long created = 1;
                while(resultSet.next())
                {
                    rankId = resultSet.getInt("rankId");
                    timelimit = resultSet.getLong("timelimit");
                    locked = resultSet.getBoolean("locked");
                    created = resultSet.getLong("created");
                }
                Rank rank = getRankManager().getRank(rankId);
                if (rank.isProtected() && locked) {
                    kratos.getLogger().log(Level.WARNING, "[KRATOS] Player " + uuid + " has a locked protected rank");
                    kratos.getLogger().log(Level.WARNING, "[KRATOS] Player " + uuid + " rank will not be loaded");
                    rank = getRankManager().getRank(0);
                }

                playerRank = new PlayerRank(rank, timelimit, locked, created);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return playerRank;
    }

    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public Kratos getKratos() {
        return kratos;
    }

    public void reconnect()
    {
        //REDACTED//
    }

    public void log(String log)
    {
        //redacted//
    }
    public boolean hasPanic()
    {
        File file = new File("plugins/Kratos/panic.txt");
        if (file.length() == 0)
        {
            return false;
        }
        return true;
    }

}

