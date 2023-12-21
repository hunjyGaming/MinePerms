package de.hunjy.mineperms.command;

import de.hunjy.mineperms.MinePerms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinePermsCommand implements CommandExecutor, TabCompleter {

    private static Map<String, SubCommand> commands = new HashMap<>();

    public static Map<String, SubCommand> getCommands() {
        return commands;
    }

    public static void registerSubCommand(SubCommand subCommand) {
        commands.put(subCommand.getAlias(), subCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length < 1) {
            commandSender.sendMessage(MinePerms.getInstance().getConfigManager().getMessage("COMMAND_DEFAULT_USAGE"));
            return false;
        }

        String rawSubCommand = args[0].toLowerCase();

        if (!commands.containsKey(rawSubCommand)) {
            commandSender.sendMessage(MinePerms.getInstance().getConfigManager().getMessage("COMMAND_DEFAULT_USAGE"));
            return false;
        }
        SubCommand subCommand = commands.get(rawSubCommand);

        if (subCommand.getPermission() != null) {
            if (!commandSender.hasPermission(subCommand.getPermission())) {
                commandSender.sendMessage(MinePerms.getInstance().getConfigManager().getMessage("COMMAND_NO_PERMISSIONS"));
                return false;
            }
        }

        String[] arguments = new String[args.length - 1];
        System.arraycopy(args,1,arguments,0,args.length - 1);

        return subCommand.onCommand(commandSender, arguments);
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> list = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("mineperms") && args.length >= 0) {

                if (args.length == 1) {
                    for (String cmd : commands.keySet()) {
                        if (commandSender.hasPermission(commands.get(cmd).getPermission())) {
                            list.add(cmd);
                        }
                    }
                }
        }
        return list;
    }
}
