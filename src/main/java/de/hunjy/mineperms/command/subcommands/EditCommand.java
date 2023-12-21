package de.hunjy.mineperms.command.subcommands;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.command.SubCommand;
import de.hunjy.mineperms.group.PermissionGroup;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class EditCommand implements SubCommand {

    private List<String> AVAILABLE_OPTIONS = new ArrayList<>();

    public EditCommand() {
        AVAILABLE_OPTIONS.add("sortid");
        AVAILABLE_OPTIONS.add("prefix");
        AVAILABLE_OPTIONS.add("suffix");
        AVAILABLE_OPTIONS.add("color");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {

        if (args.length < 3) {
            // ERROR
        }

        PermissionGroup permissionGroup = MinePerms.getInstance().getPermissionGroupManager().getCache().getOrDefault(args[0], null);

        if (permissionGroup == null) {
            // ERROR
            return false;
        }

        String option = args[1];

        if(!AVAILABLE_OPTIONS.contains(option.toLowerCase())) {
            // ERROR
            return false;
        }

        String value = "";

        for (int i = 2; i < args.length; i++) {
            value += args[i] + " ";
        }

        value = value.substring(0, value.length() - 1);

        permissionGroup.getGroupOptions().getDataMap().put(option, value);

        String stmt = "UPDATE " + MinePerms.getInstance().getConfigManager().getString("mysql.table_prefix") + "groups SET group_options='" + Base64.getEncoder().encodeToString(permissionGroup.getGroupOptions().getRawOptionString().getBytes()) + "' WHERE group_name='" + permissionGroup.getName() + "'";
        MinePerms.getInstance().getSQLConnection().query(stmt);

        return true;
    }

    @Override
    public String getPermission() {
        return "mineperms.admin.edit";
    }

    @Override
    public @NotNull String getAlias() {
        return "edit";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
