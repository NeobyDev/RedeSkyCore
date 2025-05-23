package br.neobydeveloper.com.redeskycore.Commands.register;

import br.neobydeveloper.com.redeskycore.Commands.Command;
import br.neobydeveloper.com.redeskycore.Commands.CommandLib;
import org.bukkit.plugin.Plugin;

public abstract class RegisterBase {
    private CommandLib commandLib;

    public abstract void setup();

    public CommandLib registerCommand(Command command, Plugin plugin) {
        return this.commandLib;
    }

    public RegisterBase(CommandLib commandLib) {
        this.commandLib = commandLib;
    }

    public CommandLib getCommandLib() {
        return this.commandLib;
    }
}
