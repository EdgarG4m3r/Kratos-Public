package id.apollo.luckynetwork.kratos.kratos.events;

import id.apollo.luckynetwork.kratos.kratos.objects.KratosPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KratosPlayerUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private KratosPlayer kratosPlayer;

    public KratosPlayerUpdateEvent(KratosPlayer kratosPlayer) {
        this.kratosPlayer = kratosPlayer;
    }

    public KratosPlayer getKratosPlayer() {
        return kratosPlayer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
