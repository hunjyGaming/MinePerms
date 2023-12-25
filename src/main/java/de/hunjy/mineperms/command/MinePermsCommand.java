package de.hunjy.mineperms.command;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.group.PermissionGroup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinePermsCommand implements CommandExecutor, TabCompleter {

    private static final Map<String, SubCommand> commands = new HashMap<>();

    public static Map<String, SubCommand> getCommands() {
        return commands;
    }

    public static void registerSubCommand(SubCommand subCommand) {
        commands.put(subCommand.getAlias(), subCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length < 1) {
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_DEFAULT_USAGE"));
            return false;
        }

        String rawSubCommand = args[0].toLowerCase();

        if (!commands.containsKey(rawSubCommand)) {
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_DEFAULT_USAGE"));
            return false;
        }
        SubCommand subCommand = commands.get(rawSubCommand);

        if (subCommand.getPermission() != null) {
            if (!commandSender.hasPermission(subCommand.getPermission())) {
                commandSender.sendMessage(ConfigManager.getMessage("COMMAND_NO_PERMISSIONS"));
                return false;
            }
        }

        String[] arguments = new String[args.length - 1];
        System.arraycopy(args, 1, arguments, 0, args.length - 1);

        return subCommand.onCommand(commandSender, arguments);
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> list = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("mineperms")) {

            if (args.length == 1) {
                for (String cmd : commands.keySet()) {
                    if (commandSender.hasPermission(commands.get(cmd).getPermission())) {
                        list.add(cmd);
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("group")) {
                    for (PermissionGroup permissionGroup : MinePerms.getInstance().getPermissionGroupManager().getCache().values()) {
                        list.add(permissionGroup.getName());
                    }
                }
                if (args[0].equalsIgnoreCase("user")) {
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        list.add(player.getName());
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("group")) {
                    list.add("permission");
                    list.add("option");
                }
                if (args[0].equalsIgnoreCase("user")) {
                    list.add("group");
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("group")) {
                    if (args[2].equalsIgnoreCase("permission")) {
                        list.add("add");
                        list.add("remove");
                    }
                    if (args[2].equalsIgnoreCase("option")) {
                        list.add("prefix");
                        list.add("color");
                        list.add("sortid");
                    }
                }
                if (args[0].equalsIgnoreCase("user")) {
                    list.add("add");
                    list.add("set");
                    list.add("remove");
                }
            } else if (args.length == 5) {
                if (args[0].equalsIgnoreCase("group")) {
                    if (args[2].equalsIgnoreCase("option")) {
                        if (args[3].equalsIgnoreCase("color")) {
                            for (ChatColor chatColor : ChatColor.values()) {
                                list.add(chatColor.name());
                            }
                        }
                    }
                }

                if (args[0].equalsIgnoreCase("user")) {
                    for (PermissionGroup permissionGroup : MinePerms.getInstance().getPermissionGroupManager().getCache().values()) {
                        list.add(permissionGroup.getName());
                    }
                }
            }
        }
        return list;
    }
}
