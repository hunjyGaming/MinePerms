package de.hunjy.mineperms.command;

import de.hunjy.mineperms.MinePerms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MinePermsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length < 1) {
            commandSender.sendMessage(MinePerms.getInstance().getConfigManager().getMessage("COMMAND_DEFAULT_USAGE"));
            return false;
        }
        return false;
    }
}
