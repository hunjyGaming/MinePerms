package de.hunjy.mineperms.listener;

import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.player.PermsPlayer;
import de.hunjy.mineperms.scoreboard.ScoreManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {


    @EventHandler
    public void on(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PermsPlayer permsPlayer = PermsPlayer.get(player.getUniqueId());
        permsPlayer.updatePlayerGroup();
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PermsPlayer permsPlayer = PermsPlayer.get(player.getUniqueId());


        event.setJoinMessage(ConfigManager.getMessage("PLAYER_JOIN_MESSAGE", (permsPlayer.getDisplayPermissionGroup().getGroupOptions().get("prefix") == null ? "" : permsPlayer.getDisplayPermissionGroup().getGroupOptions().get("prefix")) + player.getName()));
        ScoreManager.updateAll();
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PermsPlayer permsPlayer = PermsPlayer.get(player.getUniqueId());
        event.setQuitMessage(ConfigManager.getMessage("PLAYER_QUIT_MESSAGE", (permsPlayer.getDisplayPermissionGroup().getGroupOptions().get("prefix") == null ? "" : permsPlayer.getDisplayPermissionGroup().getGroupOptions().get("prefix")) + player.getName()));
        ScoreManager.updateAll();
    }
}
