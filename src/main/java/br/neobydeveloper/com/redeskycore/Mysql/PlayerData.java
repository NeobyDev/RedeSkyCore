package br.neobydeveloper.com.redeskycore.Mysql;

import br.neobydeveloper.com.redeskycore.Gold;
import br.neobydeveloper.com.redeskycore.Rank;
import br.neobydeveloper.com.redeskycore.RedeSkyCore;
import br.neobydeveloper.com.redeskycore.Visibility;
import java.sql.SQLException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerData {
    private final RedeSkyCore plugin = RedeSkyCore.getInstance();
    private UUID uuid;
    private String playerName;
    private Rank rank;
    private Gold gold;
    private Visibility visibility;

    public PlayerData(UUID uuid, String name) {
        this.rank = Rank.membro;
        this.gold = new Gold();
        this.visibility = Visibility.ativado;
        this.uuid = uuid;
        this.playerName = name;
    }

    public void load(Player player) {
        this.plugin.getMySQLManager().select("SELECT * FROM player WHERE uuid=?", (resultSet) -> {
            try {
                if (resultSet.next()) {
                    String rankStr = resultSet.getString("rank");
                    int goldValue = resultSet.getInt("gold");
                    String visibilityStr = resultSet.getString("visibility");
                    if (rankStr != null) {
                        this.rank = Rank.valueOf(rankStr);
                    }

                    this.gold.setGold(goldValue);
                    if (visibilityStr != null) {
                        this.visibility = Visibility.valueOf(visibilityStr);
                    }
                } else {
                    this.plugin.getMySQLManager().execute("INSERT INTO player(uuid, gold, name, rank, visibility) VALUES (?,?,?,?,?)", new Object[]{player.getUniqueId().toString(), 0, player.getName(), Rank.membro, Visibility.ativado.toString()});
                }
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("§c[RedeSkyCore] Erro ao carregar dados do jogador " + player.getName() + ": " + e.getMessage());
            }

        }, new Object[]{player.getUniqueId().toString()});
    }

    public void save(Player player) {
        this.plugin.getMySQLManager().execute("UPDATE player SET gold=?, rank=?, visibility=? WHERE uuid=?", new Object[]{this.gold.getGold(), this.rank.toString(), this.visibility.toString(), player.getUniqueId().toString()});
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public Rank getRank() {
        return this.rank;
    }

    public Gold getGold() {
        return this.gold;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setGold(Gold gold) {
        this.gold = gold;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
