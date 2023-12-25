package de.hunjy.mineperms.player;

import de.hunjy.mineperms.MinePerms;
import de.hunjy.mineperms.MinePermsLogger;
import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.events.PlayerGroupChangeEvent;
import de.hunjy.mineperms.events.PlayerGroupChangeType;
import de.hunjy.mineperms.events.PlayerGroupExpiredEvent;
import de.hunjy.mineperms.group.PermissionGroup;
import de.hunjy.mineperms.scoreboard.ScoreManager;
import de.hunjy.mineperms.sql.query.PlayerInfoResultQueryListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.lang.reflect.Field;
import java.util.*;


public class PermsPlayer {

    private static HashMap<UUID, PermsPlayer> cache = new HashMap<>();

    public static PermsPlayer get(UUID uuid) {
        if (!cache.containsKey(uuid)) {
            cache.put(uuid, new PermsPlayer(uuid));
        }

        cache.get(uuid).checkPlayer();
        return cache.get(uuid);
    }


    private Player player;
    private final UUID uuid;
    private List<PermissionGroup> permanentGroups;
    private final HashMap<PermissionGroup, Long> tempGroups;
    private List<String> allPermissions;
    private PermissionAttachment permissionAttachment;


    public PermsPlayer(UUID uuid) {
        this.uuid = uuid;
        if (Bukkit.getPlayer(uuid) != null) {
            this.player = Bukkit.getPlayer(uuid);
        }
        this.permanentGroups = new ArrayList<>();
        this.tempGroups = new HashMap<>();
        this.allPermissions = new ArrayList<>();

        loadPlayerData();
    }

    public void loadPlayerData() {
        if(permanentGroups.isEmpty()) {
            MinePerms.getInstance().getSQLConnection().loadUser(uuid, new PlayerInfoResultQueryListener() {
                @Override
                public void onQueryResult(HashMap<String, String> rawUserData) {
                    if (rawUserData.containsKey("GROUPS")) {
                        String[] groupData = rawUserData.get("GROUPS").split("::");

                        for (String group : groupData) {
                            if (group.contains("#t#")) {
                                String[] tmpGroup = group.split("#t#");
                                PermissionGroup permissionGroup = MinePerms.getInstance().getPermissionGroupManager().getCache().get(tmpGroup[0]);
                                if(permissionGroup != null) {
                                    tempGroups.put(permissionGroup, Long.parseLong(tmpGroup[1]));
                                }
                            } else {
                                PermissionGroup permissionGroup = MinePerms.getInstance().getPermissionGroupManager().getCache().get(group);
                                if(permissionGroup != null) {
                                    permanentGroups.add(permissionGroup);
                                }
                            }

                            updatePlayerPermissions();
                        }
                    }
                }

                @Override
                public void onQueryError(Exception exception) {
                    MinePermsLogger.warning(exception.getMessage());
                    if(player.isOnline()) {
                        player.kickPlayer("§cEs ist ein Fehler aufgetreten \n\n §7Versuche es erneut!");
                    }
                    cache.remove(uuid);
                }
            });
        }
    }

    public boolean isOnline() {
        return player != null;
    }

    public void addGroup(PermissionGroup permissionGroup) {
        if (!permanentGroups.contains(permissionGroup)) {
            permanentGroups.add(permissionGroup);
            Bukkit.getPluginManager().callEvent(new PlayerGroupChangeEvent(player, permissionGroup, PlayerGroupChangeType.ADD));
            updatePlayerGroup();
            updatePlayerPermissions();
            ScoreManager.updateAll();
        }
    }

    public void addGroup(PermissionGroup permissionGroup, long duration) {
        if (permanentGroups.contains(permissionGroup)) {
            removeGroup(permissionGroup);
        }
        tempGroups.put(permissionGroup, duration);
        Bukkit.getPluginManager().callEvent(new PlayerGroupChangeEvent(player, permissionGroup, PlayerGroupChangeType.ADD_TEMP));
        updatePlayerGroup();
        updatePlayerPermissions();
        ScoreManager.updateAll();
    }

    public void removeGroup(PermissionGroup permissionGroup) {
        if (permanentGroups.contains(permissionGroup)) {
            permanentGroups.remove(permissionGroup);
            Bukkit.getPluginManager().callEvent(new PlayerGroupChangeEvent(player, permissionGroup, PlayerGroupChangeType.REMOVE));
            updatePlayerGroup();
            updatePlayerPermissions();
            ScoreManager.updateAll();
        }
    }

    public void setGroup(PermissionGroup permissionGroup) {
        permanentGroups = new ArrayList<>();
        permanentGroups.add(permissionGroup);
        Bukkit.getPluginManager().callEvent(new PlayerGroupChangeEvent(player, permissionGroup, PlayerGroupChangeType.UPDATE));
        updatePlayerGroup();
        updatePlayerPermissions();
        ScoreManager.updateAll();
    }

    public void updatePlayerGroup() {
        if(!permanentGroups.isEmpty()) {
            String playerGroup = "";
            for (PermissionGroup group : permanentGroups) {
                playerGroup += group.getName() + "::";
            }
            for (PermissionGroup group : tempGroups.keySet()) {
                playerGroup += group.getName() + "#t#" + tempGroups.get(group) + "::";
            }
            if (playerGroup.length() > 2) {
                playerGroup = playerGroup.substring(0, playerGroup.length() - 2);
                String stmt = "UPDATE " + ConfigManager.getString("mysql.table_prefix") + "users SET user_groups='" + Base64.getEncoder().encodeToString(playerGroup.getBytes()) + "' WHERE user_uuid='" + uuid.toString() + "'";
                MinePerms.getInstance().getSQLConnection().query(stmt);
            }
        }
    }

    public void updatePlayerPermissions() {
        if (!isOnline()) {
            return;
        }
        if (permissionAttachment != null) {
            player.removeAttachment(permissionAttachment);
        }
        this.permissionAttachment = player.addAttachment(MinePerms.getInstance());
        allPermissions = new ArrayList<>();
        for (PermissionGroup group : permanentGroups) {
            allPermissions.addAll(group.getPermissions());
        }
        for (PermissionGroup group : tempGroups.keySet()) {
            allPermissions.addAll(group.getPermissions());
        }

        for (String permission : allPermissions) {
            permissionAttachment.setPermission(permission, true);
        }
    }

    public void checkPlayer() {
        if (player == null) {
            if (Bukkit.getPlayer(uuid) != null) {
                this.player = Bukkit.getPlayer(uuid);
            }
        }
        long currentMillis = System.currentTimeMillis();
        for (PermissionGroup permissionGroup : tempGroups.keySet()) {
            if (tempGroups.get(permissionGroup) < currentMillis) {
                tempGroups.remove(permissionGroup);
                player.sendMessage(ConfigManager.getMessage("COMMAND_USER_GROUP_EXPIRE", permissionGroup.getName()));
                Bukkit.getPluginManager().callEvent(new PlayerGroupChangeEvent(player, permissionGroup, PlayerGroupChangeType.EXPIRED));
                Bukkit.getPluginManager().callEvent(new PlayerGroupExpiredEvent(player, permissionGroup, getDisplayPermissionGroup()));
                updatePlayerGroup();
                updatePlayerPermissions();
            }
        }

    }

    public List<PermissionGroup> getPermanentGroups() {
        return permanentGroups;
    }

    public List<String> getPermissions() {
        return allPermissions;
    }

    public Player getPlayer() {
        return player;
    }

    public PermissionGroup getDisplayPermissionGroup() {
        if (permanentGroups.isEmpty() && tempGroups.isEmpty()) {
            return MinePerms.getInstance().getPermissionGroupManager().getCache().get(ConfigManager.getString("default_group"));
        }
        PermissionGroup tmp = permanentGroups.get(0);

        for (PermissionGroup group : permanentGroups) {
            if (group.getSortID() < tmp.getSortID()) {
                tmp = group;
            }
        }

        for (PermissionGroup group : tempGroups.keySet()) {
            if (group.getSortID() < tmp.getSortID()) {
                tmp = group;
            }
        }
        return tmp;

    }

    public HashMap<PermissionGroup, Long> getTempGroups() {
        return tempGroups;
    }

    public static void clearCache() {
        cache = new HashMap<>();
    }

    public String getName() {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    public boolean isLoaded() {
        return !permanentGroups.isEmpty();
    }
}
