package de.hunjy.mineperms;

import de.hunjy.mineperms.command.MinePermsCommand;
import de.hunjy.mineperms.command.PlayerRankCommand;
import de.hunjy.mineperms.command.subcommands.*;
import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.group.PermissionGroupManager;
import de.hunjy.mineperms.listener.PlayerChatEvent;
import de.hunjy.mineperms.listener.PlayerConnectionListener;
import de.hunjy.mineperms.listener.SignListener;
import de.hunjy.mineperms.player.PermsPlayer;
import de.hunjy.mineperms.sign.SignManager;
import de.hunjy.mineperms.sql.SQLConnection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinePerms extends JavaPlugin {


    private static MinePerms instance;

    private ConfigManager configManager;
    private SQLConnection sqlConnection;
    private PermissionGroupManager permissionGroupManager;
    private SignManager signManager;

    public boolean enabled = true;

    @Override
    public void onEnable() {
        instance = this;
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        init();

        if(enabled) {
            loadOnlineUser();
        }
    }

    public void loadOnlineUser() {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PermsPlayer.get(player.getUniqueId());
            }
        }
    }

    private void init() {
        configManager = new ConfigManager(getConfig());

        if(ConfigManager.getString("mysql.password").equalsIgnoreCase("password")) {
            enabled = false;
            MinePermsLogger.warning("You need to change the MySQL credentials in the config.yml");
            return;
        }


        sqlConnection = new SQLConnection();

        if(!sqlConnection.isConnected()) {
            enabled = false;
            MinePermsLogger.warning("Can't connect to MySQL!");
            return;
        }

        permissionGroupManager = new PermissionGroupManager();

        getCommand("rank").setExecutor(new PlayerRankCommand());
        getCommand("mineperms").setExecutor(new MinePermsCommand());
        getCommand("mineperms").setTabCompleter(new MinePermsCommand());


        MinePermsCommand.registerSubCommand(new CreateCommand());
        MinePermsCommand.registerSubCommand(new ListCommand());
        MinePermsCommand.registerSubCommand(new GroupCommand());
        MinePermsCommand.registerSubCommand(new UserCommand());
        MinePermsCommand.registerSubCommand(new SignCommand());
        MinePermsCommand.registerSubCommand(new RemoveCommand());
        MinePermsCommand.registerSubCommand(new HelpCommand());


        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerConnectionListener(), this);
        pluginManager.registerEvents(new PlayerChatEvent(), this);
        pluginManager.registerEvents(new SignListener(), this);

        signManager = new SignManager();
        signManager.run();
    }

    @Override
    public void onDisable() {
        signManager.stop();
        PermsPlayer.clearCache();
        instance = null;
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

    public SignManager getSignManager() {
        return signManager;
    }
}
