package de.hunjy.mineperms.listener;

import de.hunjy.mineperms.player.PermsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PermsPlayer permsPlayer = PermsPlayer.get(player.getUniqueId());
        String msg = event.getMessage();
        event.setFormat((permsPlayer.getDisplayPermissionGroup().getGroupOptions().get("prefix") == null ? "" : permsPlayer.getDisplayPermissionGroup().getGroupOptions().get("prefix")) + player.getName() + " §8» §7" + msg);
    }

}
