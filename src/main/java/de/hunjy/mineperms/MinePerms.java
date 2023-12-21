package de.hunjy.mineperms;

import de.hunjy.mineperms.command.MinePermsCommand;
import de.hunjy.mineperms.command.subcommands.CreateCommand;
import de.hunjy.mineperms.command.subcommands.EditCommand;
import de.hunjy.mineperms.command.subcommands.GroupInfoCommand;
import de.hunjy.mineperms.command.subcommands.ListCommand;
import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.group.PermissionGroupManager;
import de.hunjy.mineperms.listener.PlayerConnectionListener;
import de.hunjy.mineperms.sql.SQLConnection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.plaf.basic.BasicButtonUI;

public final class MinePerms extends JavaPlugin {


    private static MinePerms instance;

    private ConfigManager configManager;
    private SQLConnection sqlConnection;
    private PermissionGroupManager permissionGroupManager;

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
        permissionGroupManager = new PermissionGroupManager();

        /**
         *  init commands
         */
        getCommand("mineperms").setExecutor(new MinePermsCommand());
        getCommand("mineperms").setTabCompleter(new MinePermsCommand());

        /**
         *  register subcommands
         */
        MinePermsCommand.registerSubCommand(new CreateCommand());
        MinePermsCommand.registerSubCommand(new ListCommand());
        MinePermsCommand.registerSubCommand(new GroupInfoCommand());
        MinePermsCommand.registerSubCommand(new EditCommand());

        /**
         *  register listner
         */

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerConnectionListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static MinePerms getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public SQLConnection getSQLConnection() {
        return sqlConnection;
    }

    public PermissionGroupManager getPermissionGroupManager() {
        return permissionGroupManager;
    }
}
