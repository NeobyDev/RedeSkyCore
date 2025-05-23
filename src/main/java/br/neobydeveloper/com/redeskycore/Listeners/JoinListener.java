package br.neobydeveloper.com.redeskycore.Listeners;

import br.neobydeveloper.com.redeskycore.Rank;
import br.neobydeveloper.com.redeskycore.RedeSkyCore;
import br.neobydeveloper.com.redeskycore.Visibility;
import br.neobydeveloper.com.redeskycore.Mysql.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class JoinListener implements Listener {
    private final RedeSkyCore plugin;
    private final Scoreboard scoreboard;

    public JoinListener(RedeSkyCore plugin, Scoreboard scoreboard) {
        this.plugin = plugin;
        this.scoreboard = scoreboard;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage((String)null);
        Player player = e.getPlayer();
        this.updateNametagAndPosition(player);
        this.updateAllNametagsForPlayer(player);
        Profile profile = this.plugin.getProfileManager().getProfile(player);
        Visibility currentVisibility = profile.getData().getVisibility();
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            if (currentVisibility == Visibility.desativado) {
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (this.plugin.getProfileManager().getProfile(onlinePlayer).getData().getRank() != Rank.master) {
                        player.hidePlayer(onlinePlayer);
                    }
                }
            } else if (currentVisibility == Visibility.ativado) {
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    player.showPlayer(onlinePlayer);
                }
            }

            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Profile onlineProfile = this.plugin.getProfileManager().getProfile(onlinePlayer);
                if (onlineProfile.getData().getVisibility() == Visibility.desativado) {
                    onlinePlayer.hidePlayer(player);
                } else if (onlineProfile.getData().getVisibility() == Visibility.ativado) {
                    onlinePlayer.showPlayer(player);
                }
            }

        }, 3L);
    }

    private void updateNametagAndPosition(Player player) {
        Profile profile = RedeSkyCore.getInstance().getProfileManager().getProfile(player);
        if (profile != null) {
            Rank tag = profile.getData().getRank();
            if (tag != null) {
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    this.updatePlayerNametagAndPositionFor(onlinePlayer, player, tag);
                }

                if (!tag.toString().equalsIgnoreCase("membro")) {
                    player.teleport(player.getLocation().add((double)0.0F, (double)0.5F, (double)0.0F));
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    Bukkit.broadcastMessage(tag.getChatname() + player.getName() + " §6entrou neste lobby!");
                }

                this.updateTagInDatabase(player, tag);
            }
        }

    }

    private void updatePlayerNametagAndPositionFor(Player onlinePlayer, Player targetPlayer, Rank tag) {
        Scoreboard scoreboard = onlinePlayer.getScoreboard();
        if (scoreboard == null || scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            onlinePlayer.setScoreboard(scoreboard);
        }

        String teamName = tag.getTabname();
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setNameTagVisibility(NameTagVisibility.ALWAYS);
        }

        String formattedPrefix = ChatColor.translateAlternateColorCodes('&', tag.getTabname());
        team.setPrefix(formattedPrefix);
        removePlayerFromTeams(targetPlayer, scoreboard);
        team.addEntry(targetPlayer.getName());
        onlinePlayer.setScoreboard(scoreboard);
    }

    private static void removePlayerFromTeams(Player player, Scoreboard scoreboard) {
        for(Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }

    }

    private void updateAllNametagsForPlayer(Player player) {
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile profile = RedeSkyCore.getInstance().getProfileManager().getProfile(onlinePlayer);
            if (profile != null) {
                Rank tag = profile.getData().getRank();
                if (tag != null) {
                    this.updatePlayerNametagAndPositionFor(player, onlinePlayer, tag);
                }
            }
        }

    }

    private void updateTagInDatabase(Player player, Rank tag) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
                this.plugin.getMySQLManager().execute("UPDATE player SET rank=? WHERE uuid=?", new Object[]{tag.name(), player.getUniqueId().toString()});
            } catch (Exception e) {
                player.sendMessage("§cErro ao atualizar a tag no banco de dados.");
                e.printStackTrace();
            }

        });
    }

    private void updateVisibilityInDatabase(Player player, Visibility visibility) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
                this.plugin.getMySQLManager().execute("UPDATE player SET visibility=? WHERE uuid=?", new Object[]{visibility.name(), player.getUniqueId().toString()});
            } catch (Exception e) {
                player.sendMessage("§cErro ao atualizar a visibilidade no banco de dados.");
                e.printStackTrace();
            }

        });
    }
}
