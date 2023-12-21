package de.hunjy.mineperms.sql.table;

import de.hunjy.mineperms.MinePerms;

import java.util.ArrayList;
import java.util.List;

public class SQLTableBuilder {

    public enum DataType {
        VARCHAR("VARCHAR(255)"), INT("INT(16)"), LONGTEXT("LONGTEXT");

        private String key;

        DataType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
    public static class TableRow {

        private String key;
        private DataType dataType;
        private String[] attributes;

        public TableRow(String key, DataType dataType, String... attributes) {
            this.key = key;
            this.dataType = dataType;
            this.attributes = attributes;
        }

    }

    private String tableName;
    private List<TableRow> tableRows;
    private String primaryKey = null;

    public SQLTableBuilder(String tableName) {
        this.tableName = tableName;
        this.tableRows = new ArrayList<>();
    }

    public SQLTableBuilder addRow(TableRow tableRow) {
        this.tableRows.add(tableRow);
        return this;
    }

    public void setPrimaryKey(String key) {
        this.primaryKey = key;
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("CREATE TABLE IF NOT EXISTS " + MinePerms.getInstance().getConfigManager().getString("mysql.table_prefix") + tableName + " (");

        for(int i = 0; i < tableRows.size(); i++) {
            TableRow row = tableRows.get(i);
            stringBuilder.append(row.key + " " + row.dataType.getKey() + " ");
            for(String attribute : row.attributes) {
                stringBuilder.append(attribute + " ");
            }
            if(i < (tableRows.size() - 1)) {
                stringBuilder.append(",");
            }
        }

        if(primaryKey != null) {
            stringBuilder.append(",PRIMARY KEY (" + primaryKey + ")");
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }
}
