package id.apollo.luckynetwork.kratos.kratos.objects;

import id.apollo.luckynetwork.kratos.kratos.Kratos;
import org.bukkit.configuration.file.FileConfiguration;

public class KratosConfiguration {

    private String serverName;

    private String redisHost;
    private int redisPort;
    private String redisPassword;
    private String redisDatabase;
    private String redisChannel;
    private String redisUser;

    private String mysqlHost;
    private int mysqlPort;
    private String mysqlDatabase;
    private String mysqlUser;
    private String mysqlPassword;
    private boolean mysqlUseSSL;

    private boolean selfSufficient;

    public KratosConfiguration(Kratos kratos) {
        this.serverName = "Kratos";

        this.redisHost = "localhost";
        this.redisPort = 6379;
        this.redisPassword = "";
        this.redisDatabase = "0";
        this.redisChannel = "kratos";
        this.redisUser = "kratos";

        this.mysqlHost = "localhost";
        this.mysqlPort = 3306;
        this.mysqlDatabase = "kratos";
        this.mysqlUser = "kratos";
        this.mysqlPassword = "kratos";
        this.mysqlUseSSL = false;

        //Load spigot configuration
        kratos.saveDefaultConfig();
        FileConfiguration config = kratos.getConfig();

        if (config.getString("server-name") != null) {
            this.serverName = config.getString("server-name");
        }

        if (config.getString("redis.host") != null) {
            this.redisHost = config.getString("redis.host");
        }

        this.redisPort = config.getInt("redis.port");

        if (config.getString("redis.password") != null) {
            this.redisPassword = config.getString("redis.password");
        }

        if (config.getString("redis.database") != null) {
            this.redisDatabase = config.getString("redis.database");
        }

        if (config.getString("redis.channel") != null) {
            this.redisChannel = config.getString("redis.channel");
        }

        if (config.getString("redis.user") != null) {
            this.redisUser = config.getString("redis.user");
        }

        if (config.getString("mysql.host") != null) {
            this.mysqlHost = config.getString("mysql.host");
        }

        this.mysqlPort = config.getInt("mysql.port");

        if (config.getString("mysql.database") != null) {
            this.mysqlDatabase = config.getString("mysql.database");
        }

        if (config.getString("mysql.user") != null) {
            this.mysqlUser = config.getString("mysql.user");
        }

        if (config.getString("mysql.password") != null) {
            this.mysqlPassword = config.getString("mysql.password");
        }

        if (config.getBoolean("mysql.ssl") != false) {
            this.mysqlUseSSL = config.getBoolean("mysql.ssl");
        }

        if (config.getString("self-sufficient") != null) {
            this.selfSufficient = config.getBoolean("self-sufficient");
        }

    }

    public String getServerName() {
        return serverName;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public String getRedisDatabase() {
        return redisDatabase;
    }

    public String getRedisChannel() {
        return redisChannel;
    }

    public String getMysqlHost() {
        return mysqlHost;
    }

    public int getMysqlPort() {
        return mysqlPort;
    }

    public String getMysqlDatabase() {
        return mysqlDatabase;
    }

    public String getMysqlUser() {
        return mysqlUser;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public boolean isSelfSufficient() {
        return selfSufficient;
    }

    public boolean getMysqlSSL()
    {
        return mysqlUseSSL;
    }

    @Override
    public String toString() {
        return "KratosConfiguration{" +
                "serverName='" + serverName + '\'' +
                ", redisHost='" + redisHost + '\'' +
                ", redisPort=" + redisPort +
                ", redisDatabase='" + redisDatabase + '\'' +
                ", redisChannel='" + redisChannel + '\'' +
                ", redisUser='" + redisUser + '\'' +
                ", mysqlHost='" + mysqlHost + '\'' +
                ", mysqlPort=" + mysqlPort +
                ", mysqlDatabase='" + mysqlDatabase + '\'' +
                ", mysqlUser='" + mysqlUser + '\'' +
                '}';
    }
}
