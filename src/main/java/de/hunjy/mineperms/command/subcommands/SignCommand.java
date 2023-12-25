package de.hunjy.mineperms.command.subcommands;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.command.SubCommand;
import de.hunjy.mineperms.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SignCommand implements SubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        Player player = (Player) commandSender;

        Block block = player.getTargetBlock(null, 5);

        if (!block.getType().toString().contains("SIGN")) {
            return false;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                OfflinePlayer offlinePlayer = Bukkit.getPlayer(args[1]);
                if (offlinePlayer == null) {
                    player.sendMessage(ConfigManager.getMessage("COMMAND_USER_NOT_FOUND"));
                    return false;
                }
                player.sendMessage(ConfigManager.getMessage("COMMAND_SIGN_CREATE", args[1]));
                MinePerms.getInstance().getSignManager().registerSign(block, offlinePlayer.getUniqueId().toString());
                return true;
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("remove")) {
                player.sendMessage(ConfigManager.getMessage("COMMAND_SIGN_REMOVE"));
                MinePerms.getInstance().getSignManager().unregisterSign(block);
                return true;
            }
        }

        return false;
    }

    @Override
    public String getPermission() {
        return "mineperms.admin.sign";
    }

    @Override
    public @NotNull String getAlias() {
        return "sign";
    }

    @Override
    public String getDescription() {
        return "Ertslle ein Schild";
    }
}
