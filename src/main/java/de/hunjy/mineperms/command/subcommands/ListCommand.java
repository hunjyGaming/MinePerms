package de.hunjy.mineperms.command.subcommands;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.MinePermsLogger;
import de.hunjy.mineperms.command.SubCommand;
import de.hunjy.mineperms.group.PermissionGroup;
import de.hunjy.mineperms.sql.query.GroupResultQueryListener;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListCommand implements SubCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        for (PermissionGroup group : MinePerms.getInstance().getPermissionGroupManager().getCache().values()) {
            MinePermsLogger.log(" - " + group.getName() + " [" + group.getGroupOptions().get("sortID") + "]");
        }
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
        return null;
    }
}
