package br.neobydeveloper.com.redeskycore;

import org.bukkit.ChatColor;

public enum Visibility {
    desativado(0),
    ativado(1);

    private final int level;

    Visibility(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public String getDisplayName() {
        return this == ativado ? ChatColor.GREEN + "Ativado" : ChatColor.RED + "Desativado";
    }

    public static Visibility fromLevel(int level) {
        for (Visibility visibility : values()) {
            if (visibility.level == level) {
                return visibility;
            }
        }
        return desativado;
    }
}
