package me.joaomanoel.d4rkk.dev.database.tables;

import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.HikariDatabase;
import me.joaomanoel.d4rkk.dev.database.MySQLDatabase;
import me.joaomanoel.d4rkk.dev.database.SQLiteDatabase;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.DataTableInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
    name = "aCoreProfile",
    create = "CREATE TABLE IF NOT EXISTS `aCoreProfile` (`name` VARCHAR(32), `cash` LONG, `role` TEXT, `deliveries` TEXT, `preferences` TEXT, `titles` TEXT, `boosters` TEXT, `cosmetics` TEXT, `achievements` TEXT, `selected` TEXT, `cselected` TEXT, `language` TEXT, `created` LONG, `clan` TEXT, `" +
            "lastlogin` LONG, `friends` LONG, `compban` LONG, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `aCoreProfile` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `aCoreProfile` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `aCoreProfile` SET `cash` = ?, `role` = ?, `deliveries` = ?, `preferences` = ?, `titles` = ?, `boosters` = ?, `cosmetics` = ?, `achievements` = ?, `selected` = ?, `cselected` = ?, `language` = ?, `created` = ?, `clan` = ?, `lastlogin` = ?, `friends` = ?, `compban` = ? WHERE LOWER(`name`) = ?"
)
public class CoreTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `aCoreProfile` LIKE 'cash'") == null) {
        ((MySQLDatabase) database).execute("ALTER TABLE `aCoreProfile` ADD `cash` LONG AFTER `name`");
      }
    } else if (database instanceof HikariDatabase) {
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `aCoreProfile` LIKE 'cash'") == null) {
        ((HikariDatabase) database).execute("ALTER TABLE `aCoreProfile` ADD `cash` LONG AFTER `name`");
      }
    } else if (database instanceof SQLiteDatabase) {
      checkAndAddColumnSQLite((SQLiteDatabase) database, "cash", "ALTER TABLE `aCoreProfile` ADD `cash` LONG DEFAULT 0");
    }
  }

  private void checkAndAddColumnSQLite(SQLiteDatabase database, String columnName, String alterSQL) {
    try (PreparedStatement ps = database.prepareStatement("PRAGMA table_info(aCoreProfile)");
         ResultSet rs = ps.executeQuery()) {
      boolean exists = false;
      while (rs.next()) {
        if (rs.getString("name").equalsIgnoreCase(columnName)) {
          exists = true;
          break;
        }
      }
      if (!exists) {
        database.update(alterSQL);
      }
    } catch (SQLException ex) {
      Database.LOGGER.warning("Error checking column " + columnName + ": " + ex.getMessage());
    }
  }

  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("cash", new DataContainer(0L));
    defaultValues.put("role", new DataContainer("Membro"));
    defaultValues.put("deliveries", new DataContainer("{}"));
    defaultValues.put("preferences", new DataContainer("{\"pv\": 0, \"pm\": 0, \"bg\": 0, \"cm\": 0, \"pl\": 0, \"mm\": 1}"));
    defaultValues.put("titles", new DataContainer("[]"));
    defaultValues.put("boosters", new DataContainer("{}"));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("achievements", new DataContainer("[]"));
    defaultValues.put("selected", new DataContainer("{}"));
    defaultValues.put("cselected", new DataContainer("{}"));
    defaultValues.put("language", new DataContainer(""));
    defaultValues.put("created", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("clan", new DataContainer(""));
    defaultValues.put("lastlogin", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("friends", new DataContainer("[]"));
    defaultValues.put("compban", new DataContainer(0L));
    return defaultValues;
  }

}
