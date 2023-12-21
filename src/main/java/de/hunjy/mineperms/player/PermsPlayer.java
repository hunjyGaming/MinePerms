package de.hunjy.mineperms.player;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.events.PlayerGroupChangeEvent;
import de.hunjy.mineperms.events.PlayerGroupChangeType;
import de.hunjy.mineperms.group.PermissionGroup;
import de.hunjy.mineperms.sql.query.PlayerInfoResultQueryListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PermsPlayer {

    private static HashMap<Player, PermsPlayer> cache = new HashMap<>();

    public static PermsPlayer get(Player player) {
        if(!cache.containsKey(player)) {
            cache.put(player, new PermsPlayer(player));
        }
        return cache.get(player);
    }

    private Player player;
    private List<PermissionGroup> activeGroups;
    private List<String> userPermissions;
    private HashMap<String, String> userOptions;

    public PermsPlayer(Player player) {
        this.player = player;
        this.activeGroups = new ArrayList<>();

        MinePerms.getInstance().getSQLConnection().loadUser(player, new PlayerInfoResultQueryListener() {
            @Override
            public void onQueryResult(HashMap<String, String> rawUserData) {
                Bukkit.getScheduler().runTask(MinePerms.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onQueryError(Exception exception) {
                String stmt = "INSERT INTO " + MinePerms.getInstance().getConfigManager().getString("mysql.table_prefix") + "users (user_uuid) VALUES ('" + player.getUniqueId() + "')";
                MinePerms.getInstance().getSQLConnection().query(stmt);
            }
        });
    }
    public void addGroup(PermissionGroup permissionGroup) {
        if (!activeGroups.contains(permissionGroup)) {
            activeGroups.add(permissionGroup);
            Bukkit.getPluginManager().callEvent(new PlayerGroupChangeEvent(player, activeGroups, PlayerGroupChangeType.ADD));
        }
    }

    public void removeGroup(PermissionGroup permissionGroup) {
        if (activeGroups.contains(permissionGroup)) {
            activeGroups.add(permissionGroup);
            Bukkit.getPluginManager().callEvent(new PlayerGroupChangeEvent(player, activeGroups, PlayerGroupChangeType.REMOVE));
        }
    }


}
