package id.apollo.luckynetwork.kratos.kratos;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import id.apollo.luckynetwork.kratos.kratos.listeners.PlayerJoinListener;
import id.apollo.luckynetwork.kratos.kratos.listeners.PlayerLeaveListener;
import id.apollo.luckynetwork.kratos.kratos.manager.GlobalManager;
import id.apollo.luckynetwork.kratos.kratos.mysql.MySQL;
import id.apollo.luckynetwork.kratos.kratos.objects.KratosConfiguration;
import id.apollo.luckynetwork.kratos.kratos.redis.RedisHandler;
import id.apollo.luckynetwork.kratos.kratos.redis.RedisPackets;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class Kratos extends JavaPlugin {

    private ExecutorService dataSaverService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Kratos DataSaver - %d").build());
    private ExecutorService dataLoaderService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 5, new ThreadFactoryBuilder().setNameFormat("Kratos DataLoader - %d").build());

    private GlobalManager globalManager;

    private MySQL mySQL;

    private static KratosAPI kratosAPI;

    private KratosConfiguration kratosConfiguration;

    private RedisHandler redisHandler;
    @Override
    public void onEnable() {
        initializeConfig();
        initializeMySQL();
        initializeManager();
        initializeApi();
        initializeListeners();
        initializeRedis();


        if (globalManager.hasPanic()) {
            System.out.println("\n" +
                    "\u001B[31m" + "██╗  ██╗██████╗  █████╗ ████████╗ ██████╗ ███████╗\n" +
                    "\u001B[31m" + "██║ ██╔╝██╔══██╗██╔══██╗╚══██╔══╝██╔═══██╗██╔════╝\n" +
                    "\u001B[31m" + "█████╔╝ ██████╔╝███████║   ██║   ██║   ██║███████╗\n" +
                    "\u001B[31m" + "██╔═██╗ ██╔══██╗██╔══██║   ██║   ██║   ██║╚════██║\n" +
                    "\u001B[31m" + "██║  ██╗██║  ██║██║  ██║   ██║   ╚██████╔╝███████║\n" +
                    "\u001B[31m" + "╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚══════╝\n" +
                    "\u001B[31m" + "                                                  \n" +
                    "\u001B[31m" + "██████╗  █████╗ ███╗   ██╗██╗ ██████╗██╗          \n" +
                    "\u001B[31m" + "██╔══██╗██╔══██╗████╗  ██║██║██╔════╝██║          \n" +
                    "\u001B[31m" + "██████╔╝███████║██╔██╗ ██║██║██║     ██║          \n" +
                    "\u001B[31m" + "██╔═══╝ ██╔══██║██║╚██╗██║██║██║     ╚═╝          \n" +
                    "\u001B[31m" + "██║     ██║  ██║██║ ╚████║██║╚██████╗██╗          \n" +
                    "\u001B[31m" + "╚═╝     ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝ ╚═════╝╚═╝          \n" +
                    "\u001B[31m" + "                                                  \n" + "\u001B[0m");
            getLogger().log(Level.SEVERE, "Kratos had entered panic mode before. Please check /plugins/Kratos/panic.txt to intervene");
            getLogger().log(Level.SEVERE, "Please empty the panic.txt file after resolving the issue");
            getLogger().log(Level.SEVERE, "Server will be paused for 10 seconds");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        getLogger().log(Level.INFO, "Kratos has been enabled");

        System.out.println("\n" +
                "\u001B[31m" + "██╗  ██╗██████╗  █████╗ ████████╗ ██████╗ ███████╗\n" +
                "\u001B[31m" + "██║ ██╔╝██╔══██╗██╔══██╗╚══██╔══╝██╔═══██╗██╔════╝\n" +
                "\u001B[31m" + "█████╔╝ ██████╔╝███████║   ██║   ██║   ██║███████╗\n" +
                "\u001B[31m" + "██╔═██╗ ██╔══██╗██╔══██║   ██║   ██║   ██║╚════██║\n" +
                "\u001B[31m" + "██║  ██╗██║  ██║██║  ██║   ██║   ╚██████╔╝███████║\n" +
                "\u001B[31m" + "╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚══════╝\n" +
                "\u001B[31m" + "                                                  \n" +"\u001B[0m");

        getLogger().log(Level.INFO, "Kratos has been booted successfully!");


        //new KratosDestroyer(this).oblirate();
        //MongoDBToMySQL luckycoreMigrator = new MongoDBToMySQL(this);
        //luckycoreMigrator.migrate();
    }

    @Override
    public void onDisable() {
        try {
            getLogger().log(Level.INFO, "Shutting down executors");

            dataSaverService.awaitTermination(3, java.util.concurrent.TimeUnit.SECONDS);
            dataLoaderService.awaitTermination(3, java.util.concurrent.TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            getLogger().log(Level.SEVERE, "Executors interrupted... Shutting down now...");
            throw new RuntimeException(e);
        }
        redisHandler.close();
        this.mySQL.disconnect();
        getLogger().log(Level.INFO, "Kratos has been disabled");
    }

    private void initializeMySQL()
    {

        getLogger().log(Level.INFO, "Initializing MySQL");

        this.mySQL = new MySQL(kratosConfiguration.getMysqlHost(), String.valueOf(kratosConfiguration.getMysqlPort()), kratosConfiguration.getMysqlDatabase(), kratosConfiguration.getMysqlUser(), kratosConfiguration.getMysqlPassword(), kratosConfiguration.getMysqlSSL());

        this.mySQL.connect();

        getLogger().log(Level.INFO, "MySQL initialized");
    }

    private void initializeConfig()
    {
        getLogger().log(Level.INFO, "Kratos is loading configuration...");
        this.kratosConfiguration = new KratosConfiguration(this);
        getLogger().log(Level.INFO, "Kratos configuration loaded!");
    }

    private void initializeManager() {
        getLogger().log(Level.INFO, "Kratos is loading manager...");
        globalManager = new GlobalManager(this);
        getLogger().log(Level.INFO, "Kratos manager loaded!");
    }

    private void initializeApi() {
        getLogger().log(Level.INFO, "Kratos is loading API...");
        kratosAPI = new KratosAPI(this);
        getLogger().log(Level.INFO, "Kratos API loaded!");
    }

    private void initializeListeners()
    {
        getLogger().log(Level.INFO, "Kratos is loading listeners...");
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
        getLogger().log(Level.INFO, "Kratos listeners loaded!");
    }

    private void initializeRedis()
    {
        getLogger().log(Level.INFO, "Kratos is loading Redis...");
        this.redisHandler = new RedisHandler(this);
        String host = kratosConfiguration.getRedisHost();
        int port = kratosConfiguration.getRedisPort();
        String password = kratosConfiguration.getRedisPassword();
        String channel = kratosConfiguration.getRedisChannel();
        int database = Integer.parseInt(kratosConfiguration.getRedisDatabase());
        boolean connect = this.redisHandler.connect(host, port, password, channel, database);
        if (connect)
        {
            try {
                redisHandler.setupPackets(new RedisPackets());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("\n" +
                    "\u001B[31m" + "██╗  ██╗██████╗  █████╗ ████████╗ ██████╗ ███████╗\n" +
                    "\u001B[31m" + "██║ ██╔╝██╔══██╗██╔══██╗╚══██╔══╝██╔═══██╗██╔════╝\n" +
                    "\u001B[31m" + "█████╔╝ ██████╔╝███████║   ██║   ██║   ██║███████╗\n" +
                    "\u001B[31m" + "██╔═██╗ ██╔══██╗██╔══██║   ██║   ██║   ██║╚════██║\n" +
                    "\u001B[31m" + "██║  ██╗██║  ██║██║  ██║   ██║   ╚██████╔╝███████║\n" +
                    "\u001B[31m" + "╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚══════╝\n" +
                    "\u001B[31m" + "                                                  \n" +
                    "\u001B[31m" + "██████╗  █████╗ ███╗   ██╗██╗ ██████╗██╗          \n" +
                    "\u001B[31m" + "██╔══██╗██╔══██╗████╗  ██║██║██╔════╝██║          \n" +
                    "\u001B[31m" + "██████╔╝███████║██╔██╗ ██║██║██║     ██║          \n" +
                    "\u001B[31m" + "██╔═══╝ ██╔══██║██║╚██╗██║██║██║     ╚═╝          \n" +
                    "\u001B[31m" + "██║     ██║  ██║██║ ╚████║██║╚██████╗██╗          \n" +
                    "\u001B[31m" + "╚═╝     ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝ ╚═════╝╚═╝          \n" +
                    "\u001B[31m" + "                                                  \n" + "\u001B[0m");
            getLogger().log(Level.SEVERE, "[KRATOS PANIC] Failed to connect to Redis database!");
            getLogger().log(Level.SEVERE, "[KRATOS PANIC] Please check your Redis configuration!");
            getLogger().log(Level.SEVERE, "[KRATOS PANIC] KRATOS WILL STILL FUNCTION WITHOUT REDIS ALTOUGHT LESS EFFICIENT!");
        }
        if (connect)
        {
            getLogger().log(Level.INFO, "Kratos Redis loaded!");
        }
        else {
            getLogger().log(Level.SEVERE, "Kratos Redis failed to load!");
        }
    }

    public void initializeSelfSufficiency()
    {
        // redacted //
    }

    public ExecutorService getDataLoaderService() {
        return dataLoaderService;
    }

    public ExecutorService getDataSaverService() {
        return dataSaverService;
    }

    public GlobalManager getGlobalManager() {
        return globalManager;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public KratosConfiguration getKratosConfiguration() {
        return kratosConfiguration;
    }

    public RedisHandler getRedisHandler() {
        return redisHandler;
    }

    public static KratosAPI getAPI() {
        return kratosAPI;
    }

    public void reconnectRedis()
    {
        this.initializeRedis();
    }
}
