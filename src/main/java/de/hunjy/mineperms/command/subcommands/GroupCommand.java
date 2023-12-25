package de.hunjy.mineperms.command.subcommands;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.command.SubCommand;
import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.group.PermissionGroup;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GroupCommand implements SubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {

        if (args.length < 1) {
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_GROUP_USAGE"));
            return false;
        }

        PermissionGroup permissionGroup = MinePerms.getInstance().getPermissionGroupManager().getCache().getOrDefault(args[0], null);

        if (permissionGroup == null) {
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_ERROR"));
            return false;
        }

        if (args.length == 1) {
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_HEADER"));
            commandSender.sendMessage("");
            commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + " §7Informationen von Gruppe: §9" + permissionGroup.getName());
            commandSender.sendMessage("");
            commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + " §7  Options:");

            commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + "   §7  - SortID: §9" + permissionGroup.getGroupOptions().getDataMap().getOrDefault("sortid", "§c-"));
            commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + "   §7  - Prefix: §9" + permissionGroup.getGroupOptions().getDataMap().getOrDefault("prefix", "§c-"));
            commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + "   §7  - Color: §9" + permissionGroup.getGroupOptions().getDataMap().getOrDefault("color", "§c-"));

            commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + "§7  Permissions:");
            for (String permission : permissionGroup.getPermissions()) {
                commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + "§7    - " + (permission.startsWith("-") ? "§c" : "§9") + permission);

            }

            commandSender.sendMessage(" ");
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_HEADER"));
        } else {
            String[] arguments = new String[args.length - 2];
            System.arraycopy(args, 2, arguments, 0, args.length - 2);
            handleAction(commandSender, args[1], permissionGroup, arguments);
        }


        return false;
    }

    public void handleAction(CommandSender sender, String action, PermissionGroup permissionGroup, String[] args) {
        if (action.equalsIgnoreCase("permission")) {
            if (args.length == 2) {
                switch (args[0].toLowerCase()) {
                    case "add":
                        permissionGroup.addPermission(args[1]);
                        sender.sendMessage(ConfigManager.getMessage("COMMAND_GROUP_PERMISSION_ADD", args[1], permissionGroup.getName()));
                        break;
                    case "remove":
                        permissionGroup.removePermission(args[1]);
                        sender.sendMessage(ConfigManager.getMessage("COMMAND_GROUP_PERMISSION_REMOVE", args[1], permissionGroup.getName()));
                        break;
                }
            }
        } else if (action.equalsIgnoreCase("option")) {
            if (args.length >= 2) {
                String tmp = args[1];


                switch (args[0].toLowerCase()) {

                    case "prefix": {

                        if (args.length > 2) {
                            for (int i = 2; i < args.length; i++) {
                                tmp += " " + args[i];
                            }
                        }
                        tmp = ChatColor.translateAlternateColorCodes('&', tmp);
                        permissionGroup.setGroupOption("prefix", tmp);
                        sender.sendMessage(ConfigManager.getMessage("COMMAND_GROUP_OPTION_CHANGE", "Prefix", permissionGroup.getName(), tmp));
                    }
                    break;
                    case "color": {
                        try {
                            ChatColor chatColor = ChatColor.valueOf(tmp.toUpperCase());
                            permissionGroup.setGroupOption("color", chatColor.name());
                            sender.sendMessage(ConfigManager.getMessage("COMMAND_GROUP_OPTION_CHANGE", "Color", permissionGroup.getName(), tmp));
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(ConfigManager.getMessage("COMMAND_FORMAT_EXCEPTION"));
                        }
                    }
                    break;
                    case "sortid": {
                        try {
                            int id = Integer.parseInt(tmp);
                            permissionGroup.setGroupOption("sortid", tmp);
                            sender.sendMessage(ConfigManager.getMessage("COMMAND_GROUP_OPTION_CHANGE", "SortID", permissionGroup.getName(), tmp));
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ConfigManager.getMessage("COMMAND_FORMAT_EXCEPTION"));
                        }
                    }
                    break;
                }
            }
        } else {

        }
    }

    @Override
    public String getPermission() {
        return "mineperms.admin.group";
    }

    @Override
    public @NotNull String getAlias() {
        return "group";
    }

    @Override
    public String getDescription() {
        return "Verwalte die Gruppen";
    }
}
