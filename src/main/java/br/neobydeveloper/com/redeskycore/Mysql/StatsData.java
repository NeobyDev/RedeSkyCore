package br.neobydeveloper.com.redeskycore.Mysql;

import br.neobydeveloper.com.redeskycore.RedeSkyCore;
import java.sql.SQLException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StatsData {
    private final RedeSkyCore plugin = RedeSkyCore.getInstance();
    private final UUID UUID;
    private String playerName;
    private int kills = 0;
    private int wins = 0;
    private int mortes = 0;

    public StatsData(UUID uuid, String name) {
        this.UUID = uuid;
        this.playerName = name;
    }

    public void load(Player player) {
        this.plugin.getMySQLManager().select("SELECT * FROM statsSW WHERE uuid=?", (resultSet) -> {
            try {
                if (resultSet.next()) {
                    this.kills = resultSet.getInt("kills");
                    this.wins = resultSet.getInt("wins");
                    this.mortes = resultSet.getInt("mortes");
                } else {
                    this.plugin.getMySQLManager().execute("INSERT INTO statsSW(uuid, name, kills, wins, mortes) VALUES (?,?,?,?,?);", new Object[]{player.getUniqueId().toString(), player.getName(), this.kills, this.wins, this.mortes});
                }
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("§c[Erro] Não foi possível carregar os dados de " + player.getName() + ": " + e.getMessage());
            }

        }, new Object[]{player.getUniqueId().toString()});
    }

    public void save(Player player) {
        Profile profile = RedeSkyCore.getInstance().getProfileManager().getProfile(player);
        this.plugin.getMySQLManager().execute("UPDATE statsSW SET kills=?, wins=?, mortes=? WHERE uuid=?;", new Object[]{profile.getStatsData().getKills(), profile.getStatsData().getWins(), profile.getStatsData().getMortes(), player.getUniqueId().toString()});
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setMortes(int mortes) {
        this.mortes = mortes;
    }

    public UUID getUUID() {
        return this.UUID;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getKills() {
        return this.kills;
    }

    public int getWins() {
        return this.wins;
    }

    public int getMortes() {
        return this.mortes;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
