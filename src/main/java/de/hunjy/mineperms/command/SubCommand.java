package de.hunjy.mineperms.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SubCommand {
    boolean onCommand(CommandSender commandSender, String[] args);

    String getPermission();

    @NotNull
    String getAlias();

    String getDescription();
}
