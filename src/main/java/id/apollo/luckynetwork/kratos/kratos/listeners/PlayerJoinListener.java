package id.apollo.luckynetwork.kratos.kratos.listeners;

import id.apollo.luckynetwork.kratos.kratos.Kratos;
import id.apollo.luckynetwork.kratos.kratos.objects.KratosPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private Kratos kratos;

    public PlayerJoinListener(Kratos kratos) {
        this.kratos = kratos;
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {

        UUID uuid = event.getUniqueId();

        KratosPlayer kratosPlayer;
        try
        {
            kratosPlayer = kratos.getGlobalManager().loadPlayer(uuid);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        if (kratos.getKratosConfiguration().getServerName().contains("hub")) {
            if (kratosPlayer.getPlayerRank().getTimeLimit() != -1)
            {
                Bukkit.getScheduler().runTaskLater(kratos, () -> {
                    long days = (kratosPlayer.getPlayerRank().getTimeLimit() - System.currentTimeMillis()) / 86400000;
                    if (days <= 30)
                    {
                        Bukkit.getPlayer(kratosPlayer.getUuid()).sendMessage("");
                        Bukkit.getPlayer(kratosPlayer.getUuid()).sendMessage("&cYour rank will expire in &e" + days + " &cdays.");
                        Bukkit.getPlayer(kratosPlayer.getUuid()).sendMessage("");
                    }
                }, 50);
            }
        }

        if (kratos.getKratosConfiguration().isSelfSufficient())
        {
            if (kratos != null)
            {
                //redacted//
            }
        }
    }

}

