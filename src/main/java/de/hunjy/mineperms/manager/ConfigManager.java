package de.hunjy.mineperms.manager;

import de.hunjy.mineperms.MinePerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigManager {


    private static HashMap<String, String> cache = new HashMap<>();
    private FileConfiguration configuration;

    public ConfigManager(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getString(String path) {
        if (!cache.containsKey(path)) {
            cache.put(path, this.configuration.getString(path));
        }
        return cache.get(path);
    }

    public String getMessage(String key, String... placeholder) {
        String msg = getString("messages." + key);

        msg = msg.replace("{prefix}", getString("messages.PREFIX"));
        for (int i = 0; i < placeholder.length; i++) {
            msg = msg.replace("{" + i + "}", placeholder[i]);
        }

        msg = ChatColor.translateAlternateColorCodes('&', msg);

        return msg;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }
}
