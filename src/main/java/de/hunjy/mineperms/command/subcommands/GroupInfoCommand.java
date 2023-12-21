package de.hunjy.mineperms.command.subcommands;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.MinePermsLogger;
import de.hunjy.mineperms.command.SubCommand;
import de.hunjy.mineperms.group.PermissionGroup;
import de.hunjy.mineperms.sql.query.GroupResultQueryListener;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GroupInfoCommand implements SubCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {

        if(args.length != 1) {

        }

        PermissionGroup permissionGroup = MinePerms.getInstance().getPermissionGroupManager().getCache().getOrDefault(args[0], null);

        if(permissionGroup == null) {
            // ERROR
            return false;
        }

        commandSender.sendMessage(permissionGroup.getName() + ":");
        commandSender.sendMessage("    Options:");
        for(String option : permissionGroup.getGroupOptions().getDataMap().keySet()) {
            commandSender.sendMessage("        " + option + ": " + permissionGroup.getGroupOptions().getDataMap().get(option));
        }
        commandSender.sendMessage("    Permission:");


        return false;
    }

    @Override
    public String getPermission() {
        return "mineperms.admin.groupinfo";
    }

    @Override
    public @NotNull String getAlias() {
        return "info";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
