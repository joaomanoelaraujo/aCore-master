package me.joaomanoel.d4rkk.dev.database.tables;

import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.DataTableInfo;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
    name = "aCoreBatteryDash",
    create = "CREATE TABLE IF NOT EXISTS `aCoreBatteryDash` (`name` VARCHAR(32),`1v1kills` LONG, `1v1deaths` LONG, `1v1wins` LONG, `1v1games` LONG, `4v4kills` LONG, `4v4deaths` LONG, `4v4wins` LONG, `4v4games` LONG, `assists` LONG, `experience` LONG, `level` LONG, `monthlykills` LONG, `monthlydeaths` LONG, `monthlyassists` LONG, `month` TEXT, `coins` DOUBLE, `laststreak` LONG, `killstreak` LONG, `cosmetics` TEXT, `selected` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `aCoreBatteryDash` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `aCoreBatteryDash` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `aCoreBatteryDash` SET `1v1kills` = ?, `1v1deaths` = ?, `1v1wins` = ?, `1v1games` = ?, `4v4kills` = ?, `4v4deaths` = ?, `4v4wins` = ?, `4v4games` = ?, `assists` = ?, `experience` = ?, `level` = ?, `monthlykills` = ?, `montlhydeaths` = ?, `monthlyassists` = ?, `month` = ?, `coins` = ?, `laststreak` = ?, `killstreak` = ?, `cosmetics` = ?, `selected` = ? WHERE LOWER(`name`) = ?"
)
public class BatteryDashTable extends DataTable {
  
  @Override
  public void init(Database database) {
  }
  
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    for (String key : new String[]{"1v1","4v4"}) {
      defaultValues.put(key + "kills", new DataContainer(0L));
      defaultValues.put(key + "deaths", new DataContainer(0L));
      defaultValues.put(key + "games", new DataContainer(0L));
      defaultValues.put(key + "wins", new DataContainer(0L));
    }
    defaultValues.put("assists", new DataContainer(0L));
    defaultValues.put("experience", new DataContainer(0L));
    defaultValues.put("level", new DataContainer(0L));

    for (String key : new String[]{"kills", "deaths", "assists"}) {
      defaultValues.put("monthly" + key, new DataContainer(0L));
    }
    defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" +
        Calendar.getInstance().get(Calendar.YEAR)));
    defaultValues.put("coins", new DataContainer(0L));
    defaultValues.put("laststreak", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("killstreak", new DataContainer(0L));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    return defaultValues;
  }
}
