package br.neobydeveloper.com.redeskycore;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public enum Rank {
    master("Master", "§6Master", "§6[Master] ", "§6[Master] ", "§6Master", ChatColor.GOLD, false, false, 9),
    gerente("Gerente", "§4Gerente", "§4[Gerente] ", "§4[Gerente] ", "§4Gerente", ChatColor.DARK_RED, false, false, 8),
    moderador("Moderador", "§2Moderador", "§2[Moderador] ", "§2[Moderador] ", "§2Moderador", ChatColor.DARK_GREEN, false, false, 7),
    ajudante("Ajudante", "§eAjudante", "§e[Ajudante] ", "§e[Ajudante] ", "§eAjudante", ChatColor.YELLOW, false, false, 6),
    youtuber("Youtuber", "§cYoutuber", "§c[Youtuber] ", "§c[Youtuber] ", "§cYoutuber", ChatColor.RED, false, false, 5),
    mvpplus("MVP+", "§bMVP§6+§b", "§b[MVP§6+§b] ", "§b[MVP§6+§b] ", "§bMVP§6+§b", ChatColor.AQUA, false, false, 4),
    mvp("MVP", "§6MVP", "§6[MVP] ", "§6[MVP] ", "§6MVP", ChatColor.GOLD, false, false, 3),
    vip("VIP", "§aVIP", "§a[VIP] ", "§a[VIP] ", "§aVIP", ChatColor.GREEN, false, false, 2),
    membro("Membro", "§7Membro", ChatColor.GRAY + "", ChatColor.GRAY + "", "§7Membro", ChatColor.GRAY, false, false, 1);

    private String name;
    private String profilename;
    private String chatname;
    private String tabname;
    private String scorename;
    private ChatColor color;
    private boolean bolded;
    private boolean italicized;
    private int level;

    private Rank(String name, String profilename, String chatname, String tabname, String scorename, ChatColor color, boolean bolded, boolean italicized, int level) {
        this.name = name;
        this.color = color;
        this.bolded = bolded;
        this.italicized = italicized;
        this.level = level;
        this.profilename = profilename;
        this.chatname = chatname;
        this.tabname = tabname;
        this.scorename = scorename;
    }

    public boolean isHigherOrEqualsTo(Player player, Rank rank, boolean callback) {
        if (callback && this.level < rank.level) {
            player.sendMessage("§cVocê não possui permissão para usar este comando.");
            return false;
        } else {
            return this.level >= rank.level;
        }
    }

    public static boolean contains(String rank) {
        for(Rank ranks : values()) {
            if (ranks.name().equalsIgnoreCase(rank)) {
                return true;
            }
        }

        return false;
    }

    public String getNameHigher() {
        return this.name.toUpperCase();
    }

    public static String translate(String source) {
        return ChatColor.translateAlternateColorCodes('&', source);
    }

    public String getPrefix() {
        String nameFormatted = this.name.toUpperCase();
        if (this.bolded && this.italicized) {
            return translate(this.color + "&o&l" + nameFormatted);
        } else if (this.bolded) {
            return translate(this.color + "&l" + nameFormatted);
        } else {
            return this.italicized ? translate(this.color + "&o" + nameFormatted) : translate(this.color + nameFormatted);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getProfilename() {
        return this.profilename;
    }

    public String getScorename() {
        return this.scorename;
    }

    public String getChatname() {
        return this.chatname;
    }

    public String getTabname() {
        return translate(this.tabname);
    }

    public ChatColor getColor() {
        return this.color;
    }

    public boolean isBolded() {
        return this.bolded;
    }

    public boolean isItalicized() {
        return this.italicized;
    }

    public int getLevel() {
        return this.level;
    }
}
