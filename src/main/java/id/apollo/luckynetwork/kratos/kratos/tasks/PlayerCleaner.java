package id.apollo.luckynetwork.kratos.kratos.tasks;

import id.apollo.luckynetwork.kratos.kratos.Kratos;

/**
 * Used for KratosWebAPI to clean cache
 */
public class PlayerCleaner implements Runnable {

    private Kratos kratos;

    public PlayerCleaner(Kratos kratos) {
        this.kratos = kratos;
    }

    @Override
    public void run() {
        kratos.getDataLoaderService().execute(() -> {
            kratos.getGlobalManager().unloadOfflinePlayers();
        });
    }
}
