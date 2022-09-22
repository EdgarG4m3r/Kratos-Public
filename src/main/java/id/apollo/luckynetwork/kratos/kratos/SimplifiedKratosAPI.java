package id.apollo.luckynetwork.kratos.kratos;

import id.apollo.luckynetwork.kratos.kratos.objects.KratosPlayer;
import id.apollo.luckynetwork.kratos.kratos.objects.PlayerPrefix;
import id.apollo.luckynetwork.kratos.kratos.objects.Prefix;
import id.luckynetwork.dev.luckycorev5.commons.api.IPermission;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * A Simplified API for older plugins
 */
public class SimplifiedKratosAPI {

    private KratosAPI kratosAPI;

    public SimplifiedKratosAPI(KratosAPI kratosAPI) {
        this.kratosAPI = kratosAPI;
    }

    public void init() {
        System.out.println("\n" +
                "\u001B[31m" + "██╗  ██╗██████╗  █████╗ ████████╗ ██████╗ ███████╗\n" +
                "\u001B[31m" + "██║ ██╔╝██╔══██╗██╔══██╗╚══██╔══╝██╔═══██╗██╔════╝\n" +
                "\u001B[31m" + "█████╔╝ ██████╔╝███████║   ██║   ██║   ██║███████╗\n" +
                "\u001B[31m" + "██╔═██╗ ██╔══██╗██╔══██║   ██║   ██║   ██║╚════██║\n" +
                "\u001B[31m" + "██║  ██╗██║  ██║██║  ██║   ██║   ╚██████╔╝███████║\n" +
                "\u001B[31m" + "╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚══════╝\n" +
                "\u001B[31m" + "                                                  \n" + "\u001B[0m");
    }

    public List<Prefix> getPrefixList(){
        return kratosAPI.getPrefixes();
    }

    public void setRank(UUID uuid, String s, long l) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().setRank(uuid, kratosAPI.getRank(s).getId(), l);
    }

    public void removeMainPrefix(UUID uuid) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().removeMainPrefix(uuid);
    }

    public String getRankPrefix(String Rank) throws ExecutionException, InterruptedException {
        return kratosAPI.getRank(Rank).getPrefix();
    }

    public void addPrefix(UUID uuid, String s, long l) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().addPrefix(uuid, kratosAPI.getPrefix(s).getId(), l);
    }

    public void removePrefix(UUID uuid, String s) throws ExecutionException, InterruptedException  {
        kratosAPI.getHighPerformanceAPI().removePrefix(uuid, kratosAPI.getPrefix(s).getId());
    }

    public void selectMainPrefix(UUID uuid, String s) throws ExecutionException, InterruptedException {
        if (s.equalsIgnoreCase("DEFAULT") || s.equalsIgnoreCase("&7"))
        {
            kratosAPI.getHighPerformanceAPI().setPrefixAsMain(uuid, 0);
        }
        if (kratosAPI.getPrefix(s) != null) {
            kratosAPI.getHighPerformanceAPI().setPrefixAsMain(uuid, kratosAPI.getPrefix(s).getId());
        }
        else {
            kratosAPI.getHighPerformanceAPI().setCustomPrefixAsMain(uuid);
        }

    }

    public void setCustomPrefix(UUID uuid, String s, long l) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().addCustomPrefix(uuid, s, l);
    }

    public void removeCustomPrefix(UUID uuid) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().removeCustomPrefix(uuid);
    }

    public void modifyTimeLimitPrefix(UUID uuid, String s, long l) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().modifyTimeLimitOfPrefix(uuid, kratosAPI.getPrefix(s).getId(), l);
    }

    public void modifyTimeLimitRank(UUID uuid, long l) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().modifyTimeLimitOfRank(uuid, l);
    }

    public void modifyTimeLimitCustomPrefix(UUID uuid, long l) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().modifyTimeLimitOfCustomPrefix(uuid, l);
    }

    public void lockRank(UUID uuid) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().lockRank(uuid);
    }

    public void unlockRank(UUID uuid) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().unlockRank(uuid);
    }

    public void lockPrefix(UUID uuid, String s) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().lockPrefix(uuid, kratosAPI.getPrefix(s).getId());
    }

    public void unlockPrefix(UUID uuid, String s) throws ExecutionException, InterruptedException {
        kratosAPI.getHighPerformanceAPI().unlockPrefix(uuid, kratosAPI.getPrefix(s).getId());
    }

    public String getRank(UUID uuid) throws ExecutionException, InterruptedException {
        return kratosAPI.getPlayer(uuid).getPlayerRank().getRank().getName();
    }

    public String getPrefix(UUID uuid) throws ExecutionException, InterruptedException {
        if (kratosAPI.getPlayer(uuid).getUsedPrefix().getId() == 0) {
            return kratosAPI.getPlayerRank(kratosAPI.getPlayer(uuid)).getRank().getPrefix();
        }
        return kratosAPI.getPlayerMainPrefix(kratosAPI.getPlayer(uuid)).getPrefixString();
    }

    public List<String> getOwnedPrefixes(UUID uuid) throws ExecutionException, InterruptedException {
        List<String> prefixes = new ArrayList<>();
        for (PlayerPrefix prefix : kratosAPI.getPlayer(uuid).getOwnedPrefixes()) {
            prefixes.add(prefix.getPrefixString());
        }
        return prefixes;
    }

    public String getCustomPrefix(UUID uuid) throws ExecutionException, InterruptedException {
        if (kratosAPI.getPlayer(uuid).getCustomPrefix() != null)
        {
            return kratosAPI.getPlayer(uuid).getCustomPrefix().getPrefixString();
        }
        else {
            return kratosAPI.getPlayerRank(kratosAPI.getPlayer(uuid)).getRank().getPrefix();
        }
    }

}
