package de.hunjy.mineperms.scoreboard;

import de.hunjy.mineperms.group.PermissionGroup;
import de.hunjy.mineperms.player.PermsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class ScoreManager {

    private static final HashMap<Player, ScoreManager> cache = new HashMap<>();

    private final Scoreboard scoreboard;
    private final Player player;

    public static ScoreManager get(Player player) {
        if (!cache.containsKey(player)) {
            cache.put(player, new ScoreManager(player));
        }
        return cache.get(player);
    }

    public ScoreManager(Player player) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = scoreboardManager.getNewScoreboard();
        this.player = player;
        update();
    }

    public void update() {
        registerTeams();
        player.setScoreboard(scoreboard);
    }

    public void registerTeams() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            PermsPlayer permsPlayer = PermsPlayer.get(all.getUniqueId());
            PermissionGroup permissionGroup = permsPlayer.getDisplayPermissionGroup();
            Team team;

            team = scoreboard.getTeam(permissionGroup.getSortID() + "-" + all.getUniqueId().toString().substring(0, 8));
            if (team == null) {
                team = scoreboard.registerNewTeam(permissionGroup.getSortID() + "-" + all.getUniqueId().toString().substring(0, 8));
            }
            if(permissionGroup.getGroupOptions().get("prefix") != null) {
                team.setPrefix(permissionGroup.getGroupOptions().get("prefix"));
            }

            if(permissionGroup.getGroupOptions().get("color") == null) {
                team.setColor(ChatColor.GRAY);
            }else {
                try {
                    team.setColor(ChatColor.valueOf(permissionGroup.getGroupOptions().get("color").toUpperCase()));
                }catch (IllegalArgumentException e) {
                    team.setColor(ChatColor.GRAY);
                }

            }
            team.addEntry(all.getName());
        }
    }

    public static void updateAll() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            get(all).update();
        }
    }
}
