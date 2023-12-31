package de.hunjy.mineperms.command.subcommands;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.command.SubCommand;
import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.group.PermissionGroup;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;


public class CreateCommand implements SubCommand {


    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {

        if (args.length != 2) {
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_CREATE_USAGE"));
            return false;
        }

        String name = args[0];
        String sortID = args[1];

        String rawOptions = "createAt::" + System.currentTimeMillis() + "//sortid::" + sortID;

        String options = Base64.getEncoder().encodeToString(rawOptions.getBytes());
        MinePerms.getInstance().getSQLConnection().query(
                "INSERT INTO " + ConfigManager.getString("mysql.table_prefix") + "groups (group_name, group_options) VALUES" +
                        "('" + name + "', '" + options + "')"
        );

        MinePerms.getInstance().getPermissionGroupManager().getCache().put(name, new PermissionGroup(name, options, null));
        commandSender.sendMessage(ConfigManager.getMessage("COMMAND_GROUP_CREATE_SUCCESS", name));


        return false;
    }

    @Override
    public String getPermission() {
        return "mineperms.admin.create";
    }

    @Override
    public @NotNull String getAlias() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Erstelle eine Gruppe";
    }
}
