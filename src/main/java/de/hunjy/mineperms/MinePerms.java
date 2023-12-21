package de.hunjy.mineperms;

import de.hunjy.mineperms.command.MinePermsCommand;
import de.hunjy.mineperms.manager.ConfigManager;
import de.hunjy.mineperms.sql.SQLConnection;
import de.hunjy.mineperms.sql.table.SQLTableBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;

public final class MinePerms extends JavaPlugin {


    private static MinePerms instance;

    private ConfigManager configManager;
    private SQLConnection sqlConnection;

    @Override
    public void onEnable() {
        instance = this;
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        init();
    }

    private void init() {
        /**
         *  init managers
         */
        configManager = new ConfigManager(getConfig());
        sqlConnection = new SQLConnection(configManager);


        /**
         *  init commands
         */

        getCommand("mineperms").setExecutor(new MinePermsCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MinePerms getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public SQLConnection getSqlConnection() {
        return sqlConnection;
    }
}
