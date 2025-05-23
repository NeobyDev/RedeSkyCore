package br.neobydeveloper.com.redeskycore;

public class Manager {
    protected RedeSkyCore plugin;

    public Manager(RedeSkyCore plugin) {
        this.plugin = plugin;
    }

    public RedeSkyCore getPlugin() {
        return this.plugin;
    }
}
