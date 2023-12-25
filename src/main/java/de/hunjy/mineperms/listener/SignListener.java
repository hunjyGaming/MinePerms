package de.hunjy.mineperms.listener;

import de.hunjy.mineperms.sign.SignManager;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SignListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (!(block.getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) block.getState();
        if(sign.getPersistentDataContainer().has(SignManager.USER_SIGN_KEY)) {
            event.setCancelled(true);
        }
    }

}
