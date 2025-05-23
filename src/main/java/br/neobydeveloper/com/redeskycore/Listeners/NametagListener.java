package br.neobydeveloper.com.redeskycore.Listeners;

import br.neobydeveloper.com.redeskycore.Rank;
import br.neobydeveloper.com.redeskycore.RedeSkyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NametagListener implements Listener {
    public static void updateNametag(Player player) {
        Rank tag = RedeSkyCore.getInstance().getProfileManager().getProfile(player).getData().getRank();

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            updatePlayerNametagFor(onlinePlayer, player, tag);
        }

    }

    private static void updatePlayerNametagFor(Player onlinePlayer, Player targetPlayer, Rank tag) {
        Scoreboard scoreboard = onlinePlayer.getScoreboard();
        if (scoreboard == null || scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            onlinePlayer.setScoreboard(scoreboard);
        }

        String teamName = tag.name();
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setNameTagVisibility(NameTagVisibility.ALWAYS);
        }

        String formattedPrefix = ChatColor.translateAlternateColorCodes('&', tag.getTabname());
        team.setPrefix(formattedPrefix);
        setPlayerTeam(targetPlayer, team, scoreboard);
    }

    private static void setPlayerTeam(Player player, Team newTeam, Scoreboard scoreboard) {
        for(Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }

        newTeam.addEntry(player.getName());
    }
}
