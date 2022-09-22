package id.apollo.luckynetwork.kratos.kratos.objects;

import id.apollo.luckynetwork.kratos.kratos.objects.objects.IUsedPrefix;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KratosPlayer {

    private UUID uuid;
    private PlayerRank playerRank;
    private IUsedPrefix usedPrefix;
    private List<PlayerPrefix> ownedPrefixes = new ArrayList<>();
    private CustomPrefix customPrefix;


    public KratosPlayer(UUID uuid, PlayerRank playerRank, IUsedPrefix usedPrefix, List<PlayerPrefix> ownedPrefixes) {
        this.uuid = uuid;
        this.playerRank = playerRank;
        this.usedPrefix = usedPrefix;
        this.ownedPrefixes = ownedPrefixes;
    }

    public KratosPlayer(UUID uuid, PlayerRank playerRank, IUsedPrefix usedPrefix, List<PlayerPrefix> ownedPrefixes, CustomPrefix customPrefix) {
        this.uuid = uuid;
        this.playerRank = playerRank;
        this.usedPrefix = usedPrefix;
        this.ownedPrefixes = ownedPrefixes;
        this.customPrefix = customPrefix;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<PlayerPrefix> getOwnedPrefixes() {
        return ownedPrefixes;
    }


    public PlayerRank getPlayerRank() {
        return playerRank;
    }

    public IUsedPrefix getUsedPrefix() {
        return usedPrefix;
    }

    @Override
    public String toString() {
        return "KratosPlayer{" +
                "uuid=" + uuid +
                ", playerRank=" + playerRank +
                ", usedPrefix=" + usedPrefix +
                ", ownedPrefixes=" + ownedPrefixes +
                '}';
    }

    public CustomPrefix getCustomPrefix() {
        return customPrefix;
    }

    public void setCustomPrefix(CustomPrefix customPrefix) {
        this.customPrefix = customPrefix;
    }
}
