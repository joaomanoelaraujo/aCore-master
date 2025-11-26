package me.joaomanoel.d4rkk.dev.database.tables;

import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.HikariDatabase;
import me.joaomanoel.d4rkk.dev.database.MySQLDatabase;
import me.joaomanoel.d4rkk.dev.database.SQLiteDatabase;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.DataTableInfo;

import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(name = "aCoreBedWars",
        create = "CREATE TABLE IF NOT EXISTS `aCoreBedWars` (`name` VARCHAR(32)," +
                " `1v1kills` LONG," +
                " `1v1deaths` LONG," +
                " `1v1games` LONG," +
                " `1v1bedsdestroyeds` LONG," +
                " `1v1bedslosteds` LONG," +
                " `1v1finalkills` LONG," +
                " `1v1finaldeaths` LONG," +
                " `1v1wins` LONG," +
                " `2v2kills` LONG," +
                " `2v2deaths` LONG," +
                " `2v2games` LONG," +
                " `2v2bedsdestroyeds` LONG," +
                " `2v2bedslosteds` LONG," +
                " `2v2finalkills` LONG," +
                " `2v2finaldeaths` LONG," +
                " `2v2wins` LONG," +
                " `3v3kills` LONG," +
                " `3v3deaths` LONG," +
                " `3v3games` LONG," +
                " `3v3bedsdestroyeds` LONG," +
                " `3v3bedslosteds` LONG," +
                " `3v3finalkills` LONG," +
                " `3v3finaldeaths` LONG," +
                " `3v3wins` LONG,"+
                " `4v4kills` LONG," +
                " `4v4deaths` LONG," +
                " `4v4games` LONG," +
                " `4v4bedsdestroyeds` LONG," +
                " `4v4bedslosteds` LONG," +
                " `4v4finalkills` LONG," +
                " `4v4finaldeaths` LONG," +
                " `4v4wins` LONG,"+
                " `monthlykills` LONG," +
                " `monthlydeaths` LONG," +
                " `monthlyassists` LONG," +
                " `monthlybeds` LONG," +
                " `monthlywins` LONG," +
                " `monthlygames` LONG," +
                " `month` TEXT," +
                " `coins` DOUBLE," +
                " `lastmap` LONG," +
                " `cosmetics` TEXT," +
                " `selected` TEXT," +
                " `favorites` TEXT," +
                " `level` LONG," +
                " `experience` LONG," +
                " PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
        select = "SELECT * FROM `aCoreBedWars` WHERE LOWER(`name`) = ?",
        insert = "INSERT INTO `aCoreBedWars` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        update = "UPDATE `aCoreBedWars` SET `1v1kills` = ?," +
                " `1v1deaths` = ?," +
                " `1v1games` = ?," +
                " `1v1bedsdestroyeds` = ?," +
                " `1v1bedslosteds` = ?," +
                " `1v1finalkills` = ?," +
                " `1v1finaldeaths` = ?," +
                " `1v1wins` = ?," +
                " `2v2kills` = ?," +
                " `2v2deaths` = ?," +
                " `2v2games` = ?," +
                " `2v2bedsdestroyeds` = ?," +
                " `2v2bedslosteds` = ?," +
                " `2v2finalkills` = ?," +
                " `2v2finaldeaths` = ?," +
                " `2v2wins` = ?," +
                " `3v3kills` = ?," +
                " `3v3deaths` = ?," +
                " `3v3games` = ?," +
                " `3v3bedsdestroyeds` = ?," +
                " `3v3bedslosteds` = ?," +
                " `3v3finalkills` = ?," +
                " `3v3finaldeaths` = ?," +
                " `3v3wins` = ?," +
                " `4v4kills` = ?," +
                " `4v4deaths` = ?," +
                " `4v4games` = ?," +
                " `4v4bedsdestroyeds` = ?," +
                " `4v4bedslosteds` = ?," +
                " `4v4finalkills` = ?," +
                " `4v4finaldeaths` = ?," +
                " `4v4wins` = ?," +
                " `monthlykills` = ?," +
                " `monthlydeaths` = ?," +
                " `monthlyassists` = ?," +
                " `monthlybeds` = ?," +
                " `monthlywins` = ?," +
                " `monthlygames` = ?," +
                " `month` = ?," +
                " `coins` = ?," +
                " `lastmap` = ?," +
                " `cosmetics` = ?," +
                " `selected` = ?," +
                " `favorites` = ?," +
                " `level` = ?," +
                " `experience` = ?," +
                " WHERE LOWER(`name`) = ?")
public class BedWarsTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      checkAndAddColumn((MySQLDatabase) database, "lastmap", "ALTER TABLE `aCoreBedWars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`");
      checkAndAddColumn((MySQLDatabase) database, "favorites", "ALTER TABLE `aCoreBedWars` ADD `favorites` TEXT AFTER `selected`");
      checkAndAddColumn((MySQLDatabase) database, "level", "ALTER TABLE `aCoreBedWars` ADD `level` LONG DEFAULT 0 AFTER `favorites`");
      checkAndAddColumn((MySQLDatabase) database, "experience", "ALTER TABLE `aCoreBedWars` ADD `experience` LONG DEFAULT 0 AFTER `level`");
    } else if (database instanceof HikariDatabase) {
      checkAndAddColumn((HikariDatabase) database, "lastmap", "ALTER TABLE `aCoreBedWars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`");
      checkAndAddColumn((HikariDatabase) database, "favorites", "ALTER TABLE `aCoreBedWars` ADD `favorites` TEXT AFTER `selected`");
      checkAndAddColumn((HikariDatabase) database, "level", "ALTER TABLE `aCoreBedWars` ADD `level` LONG DEFAULT 0 AFTER `favorites`");
      checkAndAddColumn((HikariDatabase) database, "experience", "ALTER TABLE `aCoreBedWars` ADD `experience` LONG DEFAULT 0 AFTER `level`");
    } else if (database instanceof SQLiteDatabase) {
      checkAndAddColumnSQLite((SQLiteDatabase) database, "lastmap", "ALTER TABLE `aCoreBedWars` ADD `lastmap` LONG DEFAULT 0");
      checkAndAddColumnSQLite((SQLiteDatabase) database, "favorites", "ALTER TABLE `aCoreBedWars` ADD `favorites` TEXT");
      checkAndAddColumnSQLite((SQLiteDatabase) database, "level", "ALTER TABLE `aCoreBedWars` ADD `level` LONG DEFAULT 0");
      checkAndAddColumnSQLite((SQLiteDatabase) database, "experience", "ALTER TABLE `aCoreBedWars` ADD `experience` LONG DEFAULT 0");
    }
  }

  private void checkAndAddColumn(MySQLDatabase database, String columnName, String alterSQL) {
    try (CachedRowSet rs = database.query("SHOW COLUMNS FROM `aCoreBedWars` LIKE ?", columnName)) {
      if (rs == null) {
        database.execute(alterSQL);
      }
    } catch (SQLException ex) {
      Database.LOGGER.warning("Error checking column " + columnName + ": " + ex.getMessage());
    }
  }

  private void checkAndAddColumn(HikariDatabase database, String columnName, String alterSQL) {
    try (CachedRowSet rs = database.query("SHOW COLUMNS FROM `aCoreBedWars` LIKE ?", columnName)) {
      if (rs == null) {
        database.execute(alterSQL);
      }
    } catch (SQLException ex) {
      Database.LOGGER.warning("Error checking column " + columnName + ": " + ex.getMessage());
    }
  }

  private void checkAndAddColumnSQLite(SQLiteDatabase database, String columnName, String alterSQL) {
    try (PreparedStatement ps = database.prepareStatement("PRAGMA table_info(aCoreBedWars)");
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
    for (String key : new String[]{"1v1", "2v2", "3v3", "4v4"}) {
      defaultValues.put(key + "kills", new DataContainer(0L));
      defaultValues.put(key + "deaths", new DataContainer(0L));
      defaultValues.put(key + "games", new DataContainer(0L));
      defaultValues.put(key + "bedsdestroyeds", new DataContainer(0L));
      defaultValues.put(key + "bedslosteds", new DataContainer(0L));
      defaultValues.put(key + "finalkills", new DataContainer(0L));
      defaultValues.put(key + "finaldeaths", new DataContainer(0L));
      defaultValues.put(key + "wins", new DataContainer(0L));
    }
    for (String key : new String[]{"kills", "deaths", "assists", "beds", "wins", "games"}) {
      defaultValues.put("monthly" + key, new DataContainer(0L));
    }
    defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" +
            Calendar.getInstance().get(Calendar.YEAR)));
    defaultValues.put("coins", new DataContainer(0.0D));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    defaultValues.put("favorites", new DataContainer("{}"));
    defaultValues.put("level", new DataContainer(0L));
    defaultValues.put("experience", new DataContainer(0L));
    return defaultValues;
  }
}
