package br.neobydeveloper.com.redeskycore.Commands;

import br.neobydeveloper.com.redeskycore.Rank;
import br.neobydeveloper.com.redeskycore.RedeSkyCore;
import br.neobydeveloper.com.redeskycore.Listeners.NametagListener;
import br.neobydeveloper.com.redeskycore.Mysql.Profile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DynamicCommand(
        name = "rank",
        console = true
)
public class RankCommand extends Command {
    RedeSkyCore plugin = RedeSkyCore.getInstance();

    public void execute(CommandSender sender, String... args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            Profile profile = this.plugin.getProfileManager().getProfile(player);
            if (profile == null || !profile.getData().getRank().isHigherOrEqualsTo(player, Rank.master, true)) {
                sender.sendMessage("§cVocê não tem permissão para usar esse comando.");
                return;
            }
        }

        if (args.length < 2) {
            sender.sendMessage("§cUso correto: /rank <Jogador> <Cargo>");
        } else {
            String targetName = args[0];
            String rankName = args[1];
            if (!Rank.contains(rankName)) {
                sender.sendMessage("§cEsse cargo não existe.");
            } else {
                OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
                if (target != null && target.getUniqueId() != null) {
                    try {
                        Rank rank = Rank.valueOf(rankName);
                        this.plugin.getMySQLManager().execute("UPDATE player SET rank=? WHERE UUID=?", new Object[]{rankName, target.getUniqueId().toString()});
                    } catch (Exception e) {
                        sender.sendMessage("§cOcorreu um erro ao atualizar o banco de dados.");
                        e.printStackTrace();
                        return;
                    }

                    Profile targetProfile = this.plugin.getProfileManager().getProfile((Player)target);
                    if (targetProfile != null) {
                        targetProfile.getData().setRank(Rank.valueOf(rankName));
                        targetProfile.getData().setRank(Rank.valueOf(rankName.toLowerCase()));
                        if (target.isOnline() && target.getPlayer() != null) {
                            Player onlinePlayer = target.getPlayer();
                            NametagListener.updateNametag(onlinePlayer);
                            onlinePlayer.sendMessage("§aSeu cargo foi atualizado para " + targetProfile.getData().getRank().getProfilename() + "§a.");
                        }
                    }

                    sender.sendMessage("§aVocê atualizou o cargo do jogador §b" + target.getName() + " §apara " + rankName + "§a.");
                } else {
                    sender.sendMessage("§cJogador não encontrado.");
                }
            }
        }
    }
}
