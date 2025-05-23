package br.neobydeveloper.com.redeskycore;

import br.neobydeveloper.com.redeskycore.Commands.CommandLib;
import br.neobydeveloper.com.redeskycore.Commands.RankCommand;
import br.neobydeveloper.com.redeskycore.Listeners.JoinListener;
import br.neobydeveloper.com.redeskycore.Listeners.NametagListener;
import br.neobydeveloper.com.redeskycore.Mysql.MySQLManager;
import br.neobydeveloper.com.redeskycore.Mysql.Profile;
import br.neobydeveloper.com.redeskycore.Mysql.ProfileManager;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public final class RedeSkyCore extends JavaPlugin implements Listener {
    public static RedeSkyCore instance;
    public static ArrayList<Player> build;
    public MySQLManager MySQLManager;
    public ProfileManager profileManager;

    public void onEnable() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.saveDefaultConfig();
        instance = this;
        this.profileManager = new ProfileManager(this);
        this.MySQLManager = new MySQLManager(this);
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new NametagListener(), this);
        (new CommandLib()).setupBukkit(this).register(new RankCommand());
        this.getServer().getPluginManager().registerEvents(new JoinListener(this, scoreboard), this);
        new MySQLManager(this);
        this.getLogger().warning("chiken jockei ativo!!!");
    }

    public void onDisable() {
        this.getLogger().warning("chiken jockei desligado :C");
        this.getMySQLManager().shutdown();
    }

    private void updateVisibilityInDatabase(Player player, Visibility visibility) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                this.MySQLManager.execute("UPDATE player SET visibility=? WHERE uuid=?", new Object[]{visibility.name(), player.getUniqueId().toString()});
            } catch (Exception e) {
                player.sendMessage("§cErro ao atualizar a visibilidade no banco de dados.");
                e.printStackTrace();
            }

        });
    }

    @EventHandler
    public void onProfileLoad(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String playerName = event.getName();

        try {
            this.getProfileManager().handleProfileCreation(uuid, playerName);
            Profile profile = this.getProfileManager().getProfile(uuid);
            if (profile == null) {
                Bukkit.getLogger().severe("❌ Erro: O perfil de " + playerName + " não foi encontrado!");
                event.disallow(Result.KICK_OTHER, "§cO servidor não conseguiu carregar seu perfil.");
                return;
            }

            Player onlinePlayer = Bukkit.getPlayer(uuid);
            if (onlinePlayer != null) {
                profile.getStatsData().load(onlinePlayer);
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("❌ Erro ao carregar perfil do jogador " + playerName + ":");
            e.printStackTrace();
            event.disallow(Result.KICK_OTHER, "§cErro ao carregar seus dados. Tente novamente.");
        }

    }

    @EventHandler
    public void saveData(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.getProfileManager().getProfile(player);
        if (profile == null) {
            player.kickPlayer("§cO servidor não conseguiu carregar seu perfil.");
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                profile.getData().save(player);
                profile.getStatsData().save(player);
            });
        }
    }

    private void setupScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        if (scoreboardManager == null) {
            this.getLogger().severe("❌ ScoreboardManager não foi inicializado!");
        } else {
            Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileManager.getProfile(player);
        if (profile == null) {
            player.kickPlayer("§cErro ao carregar seu perfil!");
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                profile.getData().load(player);
                profile.getStatsData().load(player);
            });

            try {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                Team team = scoreboard.getTeam("damn");
                if (team == null) {
                    team = scoreboard.registerNewTeam("damn");
                }

                if (!team.hasEntry(player.getName())) {
                    team.addEntry(player.getName());
                }
            } catch (Exception e) {
                this.getLogger().severe("❌ Erro ao adicionar " + player.getName() + " à scoreboard: " + e.getMessage());
            }

        }
    }

    public MySQLManager getMySQLManager() {
        return this.MySQLManager;
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }

    public static RedeSkyCore getInstance() {
        return instance;
    }
}
