package de.hunjy.mineperms.command.subcommands;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.command.SubCommand;
import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.group.PermissionGroup;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ListCommand implements SubCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {

        commandSender.sendMessage(ConfigManager.getMessage("COMMAND_HEADER"));
        commandSender.sendMessage("");
        for (PermissionGroup group : MinePerms.getInstance().getPermissionGroupManager().getCache().values()) {
            commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + "§7 - " + group.getName() + " [" + group.getGroupOptions().get("sortid") + "]");
        }
        commandSender.sendMessage("");
        commandSender.sendMessage(ConfigManager.getMessage("COMMAND_HEADER"));
        return false;
    }

    @Override
    public String getPermission() {
        return "mineperms.admin.list";
    }

    @Override
    public @NotNull String getAlias() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Liste alle Gruppen auf";
    }
}
