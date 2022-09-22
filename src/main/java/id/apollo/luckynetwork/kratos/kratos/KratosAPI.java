package id.apollo.luckynetwork.kratos.kratos;

import id.apollo.luckynetwork.kratos.kratos.datamanager.HighPerformanceManager;
import id.apollo.luckynetwork.kratos.kratos.objects.*;
import id.apollo.luckynetwork.kratos.kratos.objects.objects.IUsedPrefix;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class KratosAPI {

    private Kratos kratos;

    private SimplifiedKratosAPI simplifiedKratosAPI;

    private HighPerformanceManager highPerformanceManager;


    public KratosAPI(Kratos kratos) {
        this.kratos = kratos;
        this.simplifiedKratosAPI = new SimplifiedKratosAPI(this);
        this.highPerformanceManager = new HighPerformanceManager(kratos);
    }

    public KratosPlayer getPlayer(UUID uuid) throws ExecutionException, InterruptedException {
        return kratos.getGlobalManager().loadPlayerandGet(uuid);
    }

    public void loadPlayer(UUID uuid)
    {
        kratos.getDataLoaderService().execute(() -> {
            kratos.getGlobalManager().loadPlayer(uuid);
        });
    }

    public Rank getRank(int id)
    {
        if (kratos.getGlobalManager().getRankManager().getRank(id) != null)
        {
            return kratos.getGlobalManager().getRankManager().getRank(id);
        }
        return null;
    }

    public Rank getRank(String name)
    {
        if (kratos.getGlobalManager().getRankManager().getRank(name) != null)
        {
            return kratos.getGlobalManager().getRankManager().getRank(name);
        }
        return null;
    }

    public Prefix getPrefix(int id)
    {
        if (kratos.getGlobalManager().getPrefixManager().getPrefix(id) != null)
        {
            return kratos.getGlobalManager().getPrefixManager().getPrefix(id);
        }
        return null;
    }

    public Prefix getPrefix(String name)
    {
        if (kratos.getGlobalManager().getPrefixManager().getPrefixByString(name) != null)
        {
            return kratos.getGlobalManager().getPrefixManager().getPrefixByString(name);
        }
        return null;
    }

    public PlayerRank getPlayerRank(KratosPlayer kratosPlayer)
    {
        return kratosPlayer.getPlayerRank();
    }

    public IUsedPrefix getPlayerMainPrefix(KratosPlayer kratosPlayer)
    {
        return kratosPlayer.getUsedPrefix();
    }

    public List<PlayerPrefix> getPlayerOwnedPrefixes(KratosPlayer kratosPlayer)
    {
        return kratosPlayer.getOwnedPrefixes();
    }

    public void setRank(KratosPlayer kratosPlayer, Rank rank, long timeLimit) throws ExecutionException, InterruptedException {
        highPerformanceManager.setRank(kratosPlayer.getUuid(), rank.getId(), timeLimit);
    }

    public void removeRank(KratosPlayer kratosPlayer) throws ExecutionException, InterruptedException {
        this.setRank(kratosPlayer, getRank(0), -1);
    }

    public void addPrefix(KratosPlayer kratosPlayer, Prefix prefix, long timeLimit) {
        try {
            highPerformanceManager.addPrefix(kratosPlayer.getUuid(), prefix.getId(), timeLimit);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePrefix(KratosPlayer kratosPlayer, Prefix prefix)
    {
        try {
            highPerformanceManager.removePrefix(kratosPlayer.getUuid(), prefix.getId());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMainPrefix(KratosPlayer kratosPlayer, Prefix prefix) throws ExecutionException, InterruptedException {
        highPerformanceManager.setPrefixAsMain(kratosPlayer.getUuid(), prefix.getId());
    }

    public boolean isPlayerRankLocked(KratosPlayer kratosPlayer) {
        return kratosPlayer.getPlayerRank().isLocked();
    }

    public void lockPlayerRank(KratosPlayer kratosPlayer) throws ExecutionException, InterruptedException {
        highPerformanceManager.lockRank(kratosPlayer.getUuid());
    }

    public void unlockPlayerRank(KratosPlayer kratosPlayer) throws ExecutionException, InterruptedException {
        highPerformanceManager.unlockRank(kratosPlayer.getUuid());
    }

    public void lockPlayerPrefix(KratosPlayer kratosPlayer, Prefix prefix) throws ExecutionException, InterruptedException {
        highPerformanceManager.lockPrefix(kratosPlayer.getUuid(), prefix.getId());
    }

    public void unlockPlayerPrefix(KratosPlayer kratosPlayer, Prefix prefix) throws ExecutionException, InterruptedException {
        highPerformanceManager.unlockPrefix(kratosPlayer.getUuid(), prefix.getId());
    }

    public void setCustomPrefix(KratosPlayer kratosPlayer) throws ExecutionException, InterruptedException {
        highPerformanceManager.setCustomPrefixAsMain(kratosPlayer.getUuid());
    }

    public void addCustomPrefix(KratosPlayer kratosPlayer, String prefix, long timelimit) throws ExecutionException, InterruptedException {
        highPerformanceManager.addCustomPrefix(kratosPlayer.getUuid(), prefix, timelimit);
    }

    public void removeCustomPrefix(KratosPlayer kratosPlayer) throws ExecutionException, InterruptedException {
        highPerformanceManager.removeCustomPrefix(kratosPlayer.getUuid());
    }

    public void setMainCustomPrefix(KratosPlayer kratosPlayer) throws ExecutionException, InterruptedException {
        highPerformanceManager.setCustomPrefixAsMain(kratosPlayer.getUuid());
    }

    public void modifyRankTimelimit(KratosPlayer kratosPlayer, long timeLimit) throws ExecutionException, InterruptedException {
        highPerformanceManager.modifyTimeLimitOfRank(kratosPlayer.getUuid(), timeLimit);
    }

    public void modifyPrefixTimelimit(KratosPlayer kratosPlayer, Prefix prefix, long timeLimit) throws ExecutionException, InterruptedException {
        highPerformanceManager.modifyTimeLimitOfPrefix(kratosPlayer.getUuid(), prefix.getId(), timeLimit);
    }

    public void modifyCustomPrefixtimelimit(KratosPlayer kratosPlayer, String prefix, long timeLimit) throws ExecutionException, InterruptedException {
        highPerformanceManager.modifyTimeLimitOfCustomPrefix(kratosPlayer.getUuid(), timeLimit);
    }

    public List<Prefix> getPrefixes()
    {
        return kratos.getGlobalManager().getPrefixManager().getPrefixList();
    }

    public SimplifiedKratosAPI getSimplifiedKratosAPI() {
        return simplifiedKratosAPI;
    }

    public HighPerformanceManager getHighPerformanceAPI() {
        return highPerformanceManager;
    }
}
