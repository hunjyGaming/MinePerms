package de.hunjy.mineperms.sql;

import de.hunjy.mineperms.MinePermsLogger;
import de.hunjy.mineperms.group.PermissionGroup;
import de.hunjy.mineperms.config.ConfigManager;
import de.hunjy.mineperms.sql.query.GroupResultQueryListener;
import de.hunjy.mineperms.sql.query.PlayerInfoResultQueryListener;
import de.hunjy.mineperms.sql.table.SQLTableBuilder;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SQLConnection {
    private final ExecutorService service = Executors.newFixedThreadPool(1);
    public Connection con;
    String host;
    String name;
    String password;
    String database;

    public SQLConnection() {
        this.host = ConfigManager.getString("mysql.host");
        this.name = ConfigManager.getString("mysql.username");
        this.password = ConfigManager.getString("mysql.password");
        this.database = ConfigManager.getString("mysql.database");
        connect();
        updateTableStructure();
    }

    public void connect() {
        if (!this.isConnected()) {
            try {
                this.con = DriverManager.getConnection("jdbc:mysql://" + this.host + ":3306/" + this.database + "?autoReconnect=true", this.name, this.password);
                MinePermsLogger.log("[MySQL] Verbindung zur MySQL hergestellt!");
            } catch (SQLException e) {
                MinePermsLogger.warning("[MySQL] Fehler: " + e.getMessage());
            }
        }
    }

    private void updateTableStructure() {
        SQLTableBuilder groupTable = new SQLTableBuilder("groups");
        groupTable.addRow(new SQLTableBuilder.TableRow("group_name", SQLTableBuilder.DataType.VARCHAR));
        groupTable.addRow(new SQLTableBuilder.TableRow("group_options", SQLTableBuilder.DataType.LONGTEXT));
        groupTable.addRow(new SQLTableBuilder.TableRow("group_permissions", SQLTableBuilder.DataType.LONGTEXT));
        groupTable.setPrimaryKey("group_name");
        query(groupTable.build());

        String rawOptions = "createAt::" + System.currentTimeMillis() + "//sortid::" + 99;
        String options = Base64.getEncoder().encodeToString(rawOptions.getBytes());
        query("INSERT IGNORE INTO " + ConfigManager.getString("mysql.table_prefix") + "groups (group_name, group_options) VALUES ('" + ConfigManager.getString("default_group") + "', '" + options + "')");


        SQLTableBuilder userTable = new SQLTableBuilder("users");
        userTable.addRow(new SQLTableBuilder.TableRow("user_uuid", SQLTableBuilder.DataType.VARCHAR));
        userTable.addRow(new SQLTableBuilder.TableRow("user_groups", SQLTableBuilder.DataType.LONGTEXT));
        userTable.setPrimaryKey("user_uuid");
        query(userTable.build());
    }

    public void close() {
        try {
            this.service.shutdown();
            this.service.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.isConnected()) {
            try {
                this.con.close();
                MinePermsLogger.log("[MySQL] Verbindung zur MySQL beendet!");
            } catch (SQLException var2) {
                var2.printStackTrace();
                MinePermsLogger.warning("[MySQL] Fehler: " + var2.getMessage());
            }
        }

    }

    public boolean isConnected() {
        return this.con != null;
    }

    public void query(PreparedStatement qry) {
        this.service.execute(() -> {
            if (!this.isConnected()) {
                this.connect();
            }

            if (this.isConnected()) {
                try {
                    qry.executeUpdate();
                    qry.close();
                } catch (SQLException var3) {
                    var3.printStackTrace();
                }
            }
        });
    }

    public void query(String qry) {
        this.service.execute(() -> {
            if (!this.isConnected()) {
                this.connect();
            }

            if (this.isConnected()) {
                try (PreparedStatement preparedStatement = con.prepareStatement(qry)) {
                    preparedStatement.executeUpdate();
                } catch (SQLException var3) {
                    var3.printStackTrace();
                }
            }
        });
    }

    public Connection getCon() {
        return this.con;
    }

    public void loadUser(UUID player, PlayerInfoResultQueryListener resultQueryListener) {

        query("INSERT IGNORE INTO " + ConfigManager.getString("mysql.table_prefix") + "users (user_uuid, user_groups) VALUES ('" + player.toString() + "', '" + Base64.getEncoder().encodeToString(ConfigManager.getString("default_group").getBytes()) + "')");
        Objects.requireNonNull(resultQueryListener);
        this.service.execute(() -> {
            if (this.isConnected()) {

                HashMap<String, String> rawPlayerData = new HashMap<>();
                try (PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM " + ConfigManager.getString("mysql.table_prefix") + "users WHERE user_uuid='" + player.toString() + "'")) {

                    try (ResultSet set = stmt.executeQuery()) {
                        while (set.next()) {
                            if (set.getString("user_groups") != null) {
                                rawPlayerData.put("GROUPS", new String(Base64.getDecoder().decode(set.getString("user_groups").getBytes())));
                            }
                        }
                    }
                } catch (SQLException exception) {
                    resultQueryListener.onQueryError(exception);
                    return;
                }
                resultQueryListener.onQueryResult(rawPlayerData);
            }
        });
    }

    public void loadGroups(GroupResultQueryListener resultQueryListener) {
        Objects.requireNonNull(resultQueryListener);
        this.service.execute(() -> {
            if (this.isConnected()) {
                List<PermissionGroup> permissionGroups = new ArrayList<>();
                try (PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM " + ConfigManager.getString("mysql.table_prefix") + "groups")) {
                    try (ResultSet set = stmt.executeQuery()) {
                        while (set.next()) {
                            permissionGroups.add(
                                    new PermissionGroup(
                                            set.getString("group_name"),
                                            set.getString("group_options"),
                                            set.getString("group_permissions"))
                            );
                        }
                    }
                } catch (SQLException exception) {
                    resultQueryListener.onQueryError(exception);
                    return;
                }
                resultQueryListener.onQueryResult(permissionGroups);
            }
        });
    }


}
