package de.hunjy.mineperms.events;

import de.hunjy.mineperms.group.PermissionGroup;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerGroupChangeEvent extends Event {


    private static final HandlerList handlers = new HandlerList();


    private final Player player;
    private final PermissionGroup permissionGroup;

    private final PlayerGroupChangeType type;

    public PlayerGroupChangeEvent(Player player, PermissionGroup permissionGroup, PlayerGroupChangeType type) {
        this.player = player;
        this.permissionGroup = permissionGroup;
        this.type = type;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public PlayerGroupChangeType getType() {
        return type;
    }
}
