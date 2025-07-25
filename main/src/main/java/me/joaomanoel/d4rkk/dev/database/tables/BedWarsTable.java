package me.joaomanoel.d4rkk.dev.database.tables;

import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.HikariDatabase;
import me.joaomanoel.d4rkk.dev.database.MySQLDatabase;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.DataTableInfo;

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
        insert = "INSERT INTO `aCoreBedWars` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        update = "UPDATE `aCoreBedWars` SET `1v1kills` = ?," +
                " `1v1deaths` = ?," +
                " `1v1games` = ?," +
                " `1v1bedsdestroyeds` = ?," +
                " `1v1bedslosteds` = ?," +
                " `1v1finalkills` = ?," +
                " `1v1finaldeaths` = ?," +
                " `1v1wins` = ?`," +
                " `2v2kills` = ?," +
                " `2v2deaths` = ?," +
                " `2v2games` = ?," +
                " `2v2bedsdestroyeds` = ?," +
                " `2v2bedslosteds` = ?," +
                " `2v2finalkills` = ?," +
                " `2v2finaldeaths` = ?," +
                " `2v2wins` = ?`," +
                " `4v4kills` = ?," +
                " `4v4deaths` = ?," +
                " `4v4games` = ?," +
                " `4v4bedsdestroyeds` = ?," +
                " `4v4bedslosteds` = ?," +
                " `4v4finalkills` = ?," +
                " `4v4finaldeaths` = ?," +
                " `4v4wins` = ?`," +
                " `monthlykills` = ?," +
                " `montlhydeaths` = ?," +
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
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `aCoreBedWars` LIKE 'lastmap'") == null) {
        ((MySQLDatabase) database).execute(
                "ALTER TABLE `aCoreBedWars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `favorites` TEXT AFTER `selected`");
      }
    } else if (database instanceof HikariDatabase) {
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `aCoreBedWars` LIKE 'lastmap'") == null) {
        ((HikariDatabase) database).execute(
                "ALTER TABLE `aCoreBedWars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `favorites` TEXT AFTER `selected`");
      }
    }
  }

  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    for (String key : new String[]{"1v1", "2v2", "4v4"}) {
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