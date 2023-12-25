package de.hunjy.mineperms.command.subcommands;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.command.SubCommand;
import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.group.PermissionGroup;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class RemoveCommand implements SubCommand {


    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {

        if (args.length != 1) {
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_REMOVE_USAGE"));
        }


        if(args[0].equalsIgnoreCase(ConfigManager.getString("default_group"))) {
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_REMOVE_DENY"));
            return false;
        }
        PermissionGroup permissionGroup = MinePerms.getInstance().getPermissionGroupManager().getCache().get(args[0]);

        if (permissionGroup == null) {
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_GROUP_NOT_FOUND", args[0]));
            return false;
        }

        MinePerms.getInstance().getPermissionGroupManager().removeGroup(permissionGroup);
        commandSender.sendMessage(ConfigManager.getMessage("COMMAND_REMOVE_SUCCESS", args[0]));
        return false;
    }

    @Override
    public String getPermission() {
        return "mineperms.admin.remove";
    }

    @Override
    public @NotNull String getAlias() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Lösche eine Gruppe";
    }
}
