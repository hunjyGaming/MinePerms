package de.hunjy.mineperms.command.subcommands;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.command.SubCommand;
import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.group.PermissionGroup;
import de.hunjy.mineperms.player.PermsPlayer;
import de.hunjy.mineperms.utils.TimeValue;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserCommand implements SubCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_USER_USAGE"));
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_USER_NOT_FOUND"));
            return false;
        }
        PermsPlayer permsPlayer = PermsPlayer.get(player.getUniqueId());

        if (args.length == 1) {

            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_HEADER"));
            commandSender.sendMessage("");
            commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + " §7Informationen von Spieler: §9" + player.getName());
            commandSender.sendMessage("");
            commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + " §7  Permanente Gruppen:");
            for (PermissionGroup permissionGroup : permsPlayer.getPermanentGroups()) {
                commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + " §7    - §9" + permissionGroup.getName());
            }
            commandSender.sendMessage("");
            commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + " §7  Temporäre Gruppen:");
            for (PermissionGroup permissionGroup : permsPlayer.getTempGroups().keySet()) {

                commandSender.sendMessage(ConfigManager.getMessage("PREFIX") + " §7    - §9" + permissionGroup.getName() + " §7[" + TimeValue.calcDate(permsPlayer.getTempGroups().get(permissionGroup)).toLocaleString() + "]");
            }
            commandSender.sendMessage("");
            commandSender.sendMessage(ConfigManager.getMessage("COMMAND_HEADER"));
        } else {
            String[] arguments = new String[args.length - 2];
            System.arraycopy(args, 2, arguments, 0, args.length - 2);
            handleAction(commandSender, args[1], permsPlayer, arguments);
        }
        return false;
    }

    public void handleAction(CommandSender sender, String action, PermsPlayer player, String[] args) {
        if (action.equalsIgnoreCase("group")) {
            if (args.length == 2) {
                PermissionGroup permissionGroup = MinePerms.getInstance().getPermissionGroupManager().getCache().getOrDefault(args[1], null);
                if (permissionGroup == null) {
                    sender.sendMessage(ConfigManager.getMessage("COMMAND_GROUP_NOT_FOUND", args[1]));
                    return;
                }
                switch (args[0]) {
                    case "add": {
                        player.addGroup(permissionGroup);
                        sender.sendMessage(ConfigManager.getMessage("COMMAND_USER_GROUP_ADD", permissionGroup.getName(), player.getPlayer().getName()));
                    }
                    break;
                    case "set": {
                        player.setGroup(permissionGroup);
                        sender.sendMessage(ConfigManager.getMessage("COMMAND_USER_GROUP_SET", permissionGroup.getName(), player.getPlayer().getName()));
                    }
                    break;
                    case "remove":
                        player.removeGroup(permissionGroup);
                        sender.sendMessage(ConfigManager.getMessage("COMMAND_USER_GROUP_REMOVE", permissionGroup.getName(), player.getPlayer().getName()));
                        break;
                }
                return;
            }

            if (args.length > 2) {
                PermissionGroup permissionGroup = MinePerms.getInstance().getPermissionGroupManager().getCache().getOrDefault(args[1], null);
                if (permissionGroup == null) {
                    sender.sendMessage(ConfigManager.getMessage("COMMAND_GROUP_NOT_FOUND", args[1]));
                    return;
                }
                switch (args[0]) {
                    case "add":
                    case "set": {
                        long durationMillis = 0;
                        for (int i = 2; i < args.length; i++) {
                            String definition = args[i].substring(args[i].length() - 1);
                            TimeValue timeValue = TimeValue.getTimeValue(definition);

                            try {
                                int rawDuration = Integer.parseInt(args[i].replace(definition, ""));
                                durationMillis += (rawDuration * timeValue.getMultiplicator());
                            } catch (NumberFormatException e) {
                                sender.sendMessage(ConfigManager.getMessage("PREFIX") + "§cDer Angegebne Wert ist Falsch! Benutze nur Zahlen (0-9)");
                                return;
                            }
                        }

                        long currentMillis = System.currentTimeMillis();
                        player.addGroup(permissionGroup, (currentMillis + durationMillis));
                        sender.sendMessage(ConfigManager.getMessage("COMMAND_USER_GROUP_ADD_TEMP", permissionGroup.getName(), player.getPlayer().getName(), TimeValue.calcDate(currentMillis + durationMillis).toLocaleString()));
                    }
                    break;
                }
            }

        }
    }


    @Override
    public String getPermission() {
        return "mineperms.admin.user";
    }

    @Override
    public @NotNull String getAlias() {
        return "user";
    }

    @Override
    public String getDescription() {
        return "Verwalte die Spieler";
    }
}
