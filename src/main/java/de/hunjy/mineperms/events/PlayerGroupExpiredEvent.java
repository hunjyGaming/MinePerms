package de.hunjy.mineperms.events;

import de.hunjy.mineperms.group.PermissionGroup;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerGroupExpiredEvent extends Event {


    private static final HandlerList handlers = new HandlerList();


    private final Player player;
    private final PermissionGroup expiredGroup;
    private final PermissionGroup currentGroup;

    public PlayerGroupExpiredEvent(Player player, PermissionGroup expiredGroup, PermissionGroup currentGroup) {
        this.player = player;
        this.expiredGroup = expiredGroup;
        this.currentGroup = currentGroup;
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

    public PermissionGroup getExpiredGroup() {
        return expiredGroup;
    }

    public PermissionGroup getCurrentGroup() {
        return currentGroup;
    }
}
