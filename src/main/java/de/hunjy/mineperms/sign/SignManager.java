package de.hunjy.mineperms.sign;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.player.PermsPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SignManager {

    public static final NamespacedKey USER_SIGN_KEY = new NamespacedKey(MinePerms.getInstance(), "user_sign");
    private final List<Sign> signBlocks;
    public BukkitTask task;

    public SignManager() {
        signBlocks = new ArrayList<>();
        loadSings();
    }

    public void run() {
        task = Bukkit.getScheduler().runTaskTimer(MinePerms.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Sign sign : signBlocks) {
                    updateText(sign);
                }
            }
        }, 0, 20);
    }

    public void stop() {
        task.cancel();
    }

    public void loadSings() {
        World world = Bukkit.getServer().getWorld("world");
        for (Chunk chunk : world.getLoadedChunks())
        {
            for (BlockState blockState : chunk.getTileEntities())
            {
                if (blockState instanceof Sign)
                {
                    Sign sign = (Sign) blockState;
                    if(sign.getPersistentDataContainer().has(USER_SIGN_KEY)) {
                        signBlocks.add(sign);
                    }
                }
            }
        }
    }

    public void updateText(Sign sign) {

        String playerUUID = sign.getPersistentDataContainer().get(USER_SIGN_KEY, PersistentDataType.STRING);
        PermsPlayer permsPlayer = PermsPlayer.get(UUID.fromString(playerUUID));

        for (int i = 0; i < 4; i++) {
            String line = ConfigManager.getString("sign.line_" + (i + 1));
            line = ChatColor.translateAlternateColorCodes('&', line);
            line = line.replaceAll("%player%", permsPlayer.getName());
            line = line.replaceAll("%rank%", permsPlayer.getDisplayPermissionGroup().getName());
            sign.setLine(i, line);
        }
        sign.update();
    }

    public void registerSign(Block block, String playerUUID) {
        Sign sign = (Sign) block.getState();
        sign.getPersistentDataContainer().set(USER_SIGN_KEY, PersistentDataType.STRING, playerUUID);
        signBlocks.add(sign);
    }

    public void unregisterSign(Block block) {
        Sign sign = (Sign) block.getState();
        if (signBlocks.contains(sign)) {
            if (sign.getPersistentDataContainer().has(USER_SIGN_KEY)) {
                signBlocks.remove(sign);
                sign.getPersistentDataContainer().remove(USER_SIGN_KEY);
                block.setType(Material.AIR);
            }
        }
    }
}
