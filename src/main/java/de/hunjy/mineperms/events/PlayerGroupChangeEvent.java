package de.hunjy.mineperms.events;

import de.hunjy.mineperms.group.PermissionGroup;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerGroupChangeEvent extends Event {


    private static final HandlerList handlers = new HandlerList();


    private Player player;
    private List<PermissionGroup> permissionGroups;

    private PlayerGroupChangeType type;

    public PlayerGroupChangeEvent(Player player, List<PermissionGroup> permissionGroups, PlayerGroupChangeType type) {
        this.player = player;
        this.permissionGroups = permissionGroups;
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

    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public PlayerGroupChangeType getType() {
        return type;
    }
}
