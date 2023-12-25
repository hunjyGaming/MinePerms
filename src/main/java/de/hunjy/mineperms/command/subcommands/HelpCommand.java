package de.hunjy.mineperms.command.subcommands;

import de.hunjy.mineperms.command.MinePermsCommand;
import de.hunjy.mineperms.command.SubCommand;
import de.hunjy.mineperms.config.ConfigManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements SubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        for(SubCommand subCommand : MinePermsCommand.getCommands().values()) {
            commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + " §7- §9/" + subCommand.getAlias() + " §8| §7" + subCommand.getDescription());
        }
        return false;
    }

    @Override
    public String getPermission() {
        return "mineperms.admin.help";
    }

    @Override
    public @NotNull String getAlias() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Eine Liste aller Befehle";
    }
}
