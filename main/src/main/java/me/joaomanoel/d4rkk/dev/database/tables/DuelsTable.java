package me.joaomanoel.d4rkk.dev.database.tables;

import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.MySQLDatabase;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.DataTableInfo;

import javax.sql.rowset.CachedRowSet;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
        name = "aCoreDuels",
        create = "CREATE TABLE IF NOT EXISTS `aCoreDuels` (`name` VARCHAR(32),`uhckills` LONG, `uhcdeaths` LONG, `uhcwins` LONG, `uhcgames` LONG, `bowkills` LONG, `bowdeaths` LONG, `bowwins` LONG, `bowgames` LONG, `classickills` LONG, `classicdeaths` LONG, `classicwins` LONG, `classicgames` LONG, `opkills` LONG, `opdeaths` LONG, `opwins` LONG, `opgames` LONG, `sumokills` LONG, `sumodeaths` LONG, `sumowins` LONG, `sumogames` LONG, `bedfightkills` LONG, `bedfightdeaths` LONG, `bedfightwins` LONG, `bedfightgames` LONG, `pearlkills` LONG, `pearldeaths` LONG, `pearlwins` LONG, `pearlgames` LONG, `assists` LONG, `experience` LONG, `level` LONG, `monthlykills` LONG, `monthlydeaths` LONG, `monthlyassists` LONG, `monthlywins` LONG, `month` TEXT, `coins` DOUBLE, `laststreak` LONG, `bestkillstreak` LONG, `killstreak` LONG, `kitconfig` TEXT, `lastmap` LONG, `cosmetics` TEXT, `selected` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
        select = "SELECT * FROM `aCoreDuels` WHERE LOWER(`name`) = ?",
        insert = "INSERT INTO `aCoreDuels` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        update = "UPDATE `aCoreDuels` SET `uhckills` = ?, `uhcdeaths` = ?, `uhcwins` = ?, `uhcgames` = ?, `bowkills` = ?, `bowdeaths` = ?, `bowwins` = ?, `bowgames` = ?, `classickills` = ?, `classicdeaths` = ?, `classicwins` = ?, `classicgames` = ?, `opkills` = ?, `opdeaths` = ?, `opwins` = ?, `opgames` = ?, `sumokills` = ?, `sumodeaths` = ?, `sumowins` = ?, `sumogames` = ?, `bedfightkills` = ?, `bedfightdeaths` = ?, `bedfightwins` = ?, `bedfightgames` = ?, `pearlkills` = ?, `pearldeaths` = ?, `pearlwins` = ?, `pearlgames` = ?, `assists` = ?, `experience` = ?, `level` = ?, `monthlykills` = ?, `monthlydeaths` = ?, `monthlyassists` = ?, `monthlywins` = ?, `month` = ?, `coins` = ?, `laststreak` = ?, `killstreak` = ?, `bestkillstreak` = ?, `kitconfig` = ?, `lastmap` = ?, `cosmetics` = ?, `selected` = ? WHERE LOWER(`name`) = ?"
)
public class DuelsTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      checkAndAddColumn((MySQLDatabase) database, "lastmap", "LONG DEFAULT 0 AFTER `coins`");
      checkAndAddColumn((MySQLDatabase) database, "kitconfig", "TEXT DEFAULT '{}' AFTER `selected`");
      checkAndAddColumn((MySQLDatabase) database, "cosmetics", "TEXT DEFAULT '{}' AFTER `kitconfig`");
      checkAndAddColumn((MySQLDatabase) database, "selected", "TEXT DEFAULT '{}' AFTER `cosmetics`");
    }
  }

  private void checkAndAddColumn(MySQLDatabase mysql, String column, String definition) {
    try (CachedRowSet rs = mysql.query("SHOW COLUMNS FROM `aCoreDuels` LIKE '" + column + "'")) {
      if (rs == null || !rs.next()) {
        mysql.execute("ALTER TABLE `aCoreDuels` ADD `" + column + "` " + definition);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();

    for (String key : new String[]{"uhc", "bow", "classic", "op", "sumo", "bedfight", "pearl"}) {
      defaultValues.put(key + "kills", new DataContainer(0L));
      defaultValues.put(key + "deaths", new DataContainer(0L));
      defaultValues.put(key + "games", new DataContainer(0L));
      defaultValues.put(key + "wins", new DataContainer(0L));
    }

    defaultValues.put("assists", new DataContainer(0L));
    defaultValues.put("experience", new DataContainer(0L));
    defaultValues.put("level", new DataContainer(1L));

    for (String key : new String[]{"kills", "deaths", "assists", "wins"}) {
      defaultValues.put("monthly" + key, new DataContainer(0L));
    }

    defaultValues.put("month",
            new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" +
                    Calendar.getInstance().get(Calendar.YEAR)));

    defaultValues.put("coins", new DataContainer(0L));
    defaultValues.put("laststreak", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("killstreak", new DataContainer(0L));
    defaultValues.put("bestkillstreak", new DataContainer(0L));
    defaultValues.put("kitconfig", new DataContainer("{}"));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));

    return defaultValues;
  }
}
