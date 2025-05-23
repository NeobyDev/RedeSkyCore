package br.neobydeveloper.com.redeskycore.Mysql;

import br.neobydeveloper.com.redeskycore.RedeSkyCore;
import java.util.UUID;
import org.bukkit.entity.Player;

public class Profile {
    private RedeSkyCore plugin = RedeSkyCore.getInstance();
    private PlayerData data;
    private UUID UUID;
    private String playerName;
    private final PlayerData playerData;
    private final StatsData statsData;

    public Profile(UUID uuid, String name) {
        this.UUID = uuid;
        this.playerName = name;
        this.data = new PlayerData(uuid, name);
        this.playerData = new PlayerData(uuid, name);
        this.statsData = new StatsData(uuid, name);
    }

    public void load(Player player) {
        this.playerData.load(player);
        this.statsData.load(player);
    }

    public void save(Player player) {
        this.playerData.save(player);
        this.statsData.save(player);
    }

    public RedeSkyCore getPlugin() {
        return this.plugin;
    }

    public PlayerData getData() {
        return this.data;
    }

    public StatsData getStatsData() {
        return this.statsData;
    }

    public UUID getUUID() {
        return this.UUID;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlugin(RedeSkyCore plugin) {
        this.plugin = plugin;
    }

    public void setData(PlayerData data) {
        this.data = data;
    }

    public void setUUID(UUID UUID2) {
        this.UUID = UUID2;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
