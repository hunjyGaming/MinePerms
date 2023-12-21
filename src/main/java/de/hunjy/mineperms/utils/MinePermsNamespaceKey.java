package de.hunjy.mineperms.utils;

import de.hunjy.mineperms.MinePerms;
import org.bukkit.NamespacedKey;

public enum MinePermsNamespaceKey {

    MP_USER_INFORMATION_GROUP("MP_USER_INFORMATION_GROUP");

    private String key;

    MinePermsNamespaceKey(String key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return new NamespacedKey(MinePerms.getInstance(), key);
    }
}
