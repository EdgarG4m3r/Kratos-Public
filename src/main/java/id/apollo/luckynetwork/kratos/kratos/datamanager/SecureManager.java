package id.apollo.luckynetwork.kratos.kratos.datamanager;

import id.apollo.luckynetwork.kratos.kratos.Kratos;
import id.apollo.luckynetwork.kratos.kratos.redis.JsonBuilder;
import id.apollo.luckynetwork.kratos.kratos.redis.sync.PlayerUpdate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * Secure Manager for Knox Mode. Transaction per second is rated for 300 TPS / thread
 */
public class SecureManager {

    private final Kratos kratos;
    private final ExecutorService crypto = Executors.newCachedThreadPool();


    public SecureManager(Kratos kratos) {
        this.kratos = kratos;
    }

    /*
    Ranks
     */

    /**
     * Set rank by rank name
     * @param uuid player's UUID
     * @param rankId ID of preffered rank, input 0 for default rank
     * @param timelimit time limit for this rank, input -1 for no time limit
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void setRank(UUID uuid, int rankId, long timelimit) throws ExecutionException, InterruptedException {
        call("setRank('" + uuid.toString() + "', " + rankId + ", " + timelimit + ")");
        postCall(uuid);
    }

    /**
     * Lock rank by UUID
     * @param uuid
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void lockRank(UUID uuid) throws ExecutionException, InterruptedException {
        call("lockRank('" + uuid.toString() + "')");
        postCall(uuid);
    }

    /**
     * Unlock rank by UUID
     * @param uuid
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void unlockRank(UUID uuid) throws ExecutionException, InterruptedException {
        call("unlockRank('" + uuid.toString() + "')");
        postCall(uuid);
    }

    /**
     * Modify time limit of a rank
     * @param uuid  player UUID
     * @param timelimit new time limit
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void modifyTimeLimitOfRank(UUID uuid, long timelimit) throws ExecutionException, InterruptedException {
        call("modifyRankTimeLimit('" + uuid.toString() + "', " + timelimit + ")");
        postCall(uuid);
    }

    /*
    Custom Prefixes
     */

    /**
     * Give player a custom prefix
     * @param uuid player's UUID
     * @param prefixString prefix string
     * @param Timelimit time limit for this prefix, input -1 for no time limit
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void addCustomPrefix(UUID uuid, String prefixString, long Timelimit) throws ExecutionException, InterruptedException {
        call("addCustomPrefix('" + uuid.toString() + "', '" + prefixString + "', " + Timelimit + ");");
        postCall(uuid);
    }

    /**
     * Remove player's custom prefix
     * @param uuid player's UUID
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void removeCustomPrefix(UUID uuid) throws ExecutionException, InterruptedException {
        call("removeCustomPrefix('" + uuid.toString() + "');");
        postCall(uuid);
    }

    /**
     * Get player's custom prefix as the main prefix. Will override other main prefixes.
     * @param uuid player's UUID
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void setCustomPrefixAsMain(UUID uuid) throws ExecutionException, InterruptedException {
        call("setMainCustomPrefix('" + uuid.toString() + "');");
        postCall(uuid);
    }

    /**
     * modify time limit of a custom prefix
     * @param uuid player UUID
     * @param timelimit time limit for this prefix, input -1 for no time limit
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void modifyTimeLimitOfCustomPrefix(UUID uuid, long timelimit) throws ExecutionException, InterruptedException {
        call("modifyCustomPrefixtimelimit('" + uuid.toString() + "', " + timelimit + ");");
        postCall(uuid);
    }

    /*
    Buyable Prefixes
     */
    public void addPrefix(UUID uuid, int prefixId, long timelimit) throws ExecutionException, InterruptedException {
        call("addPrefix('" + uuid.toString() + "', " + prefixId + ", " + timelimit + ");");
        postCall(uuid);
    }

    public void removePrefix(UUID uuid, int prefixId) throws ExecutionException, InterruptedException {
        call("removePrefix('" + uuid.toString() + "', " + prefixId + ");");
        postCall(uuid);
    }

    public void setPrefixAsMain(UUID uuid, int prefixId) throws ExecutionException, InterruptedException {
        call("setMainPrefix('" + uuid.toString() + "', " + prefixId + ");");
        postCall(uuid);
    }

    public void lockPrefix(UUID uuid, int prefixId) throws ExecutionException, InterruptedException {
        call("lockPrefix('" + uuid.toString() + "', " + prefixId + ");");
        postCall(uuid);
    }

    public void unlockPrefix(UUID uuid, int prefixId) throws ExecutionException, InterruptedException {
        call("unlockPrefix('" + uuid.toString() + "', " + prefixId + ");");
        postCall(uuid);
    }

    public void modifyTimeLimitOfPrefix(UUID uuid, int prefixId, long timelimit) throws ExecutionException, InterruptedException {
        call("modifyPrefixTimelimit('" + uuid.toString() + "', " + prefixId + ", " + timelimit + ");");
        postCall(uuid);
    }

    public void removeMainPrefix(UUID uuid) throws ExecutionException, InterruptedException {
        call("removeMainPrefix('" + uuid.toString() + "');");
        postCall(uuid);
    }

    public void buyRank(UUID uuid, int rankId, int price, long timelimit) throws ExecutionException, InterruptedException {
        call("buyRank('" + uuid.toString() + "', " + rankId + ", " + price + ", " + timelimit + ");");
        postCall(uuid);
    }

    public void buyPrefix(UUID uuid, int prefixId, int price, long timelimit) throws ExecutionException, InterruptedException {
        call("buyPrefix('" + uuid.toString() + "', " + prefixId + ", " + price + ", " + timelimit + ");");
        postCall(uuid);
    }

    private void call(String args) {
        crypto.execute(() -> {
            //redacted//
        });

    }

    private void postCall(UUID uuid)
    {
        //redacted//
    }

    public String signAndEncrypt(char[] key, String message) {
        //redacted//
    }

    public char[] contactMasterForKey() throws ExecutionException
    {
        //redacted//
    }


    private void sendRedisUpdate(UUID uuid)
    {
        //redacted//

    }

}
