package de.hunjy.mineperms.listener;

import de.hunjy.mineperms.player.PermsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnectionListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PermsPlayer permsPlayer = PermsPlayer.get(player);
    }

}
