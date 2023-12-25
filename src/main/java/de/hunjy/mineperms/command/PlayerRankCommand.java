package de.hunjy.mineperms.command;

import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.player.PermsPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerRankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;

        PermsPlayer permsPlayer = PermsPlayer.get(player.getUniqueId());

        player.sendMessage(ConfigManager.getMessage("COMMAND_USER_SELF_GROUP", permsPlayer.getDisplayPermissionGroup().getName()));


        return false;
    }
}
