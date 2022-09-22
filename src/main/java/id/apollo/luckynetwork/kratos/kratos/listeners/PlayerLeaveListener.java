package id.apollo.luckynetwork.kratos.kratos.listeners;

import id.apollo.luckynetwork.kratos.kratos.Kratos;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerLeaveListener implements Listener {

    private Kratos kratos;

    public PlayerLeaveListener(Kratos kratos) {
        this.kratos = kratos;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeave(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        kratos.getDataLoaderService().execute(() -> {
            kratos.getGlobalManager().unloadPlayer(uuid);
        });
    }

}
