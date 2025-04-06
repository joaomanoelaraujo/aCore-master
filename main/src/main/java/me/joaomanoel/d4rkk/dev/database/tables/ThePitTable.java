package me.joaomanoel.d4rkk.dev.database.tables;

import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.DataTableInfo;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
    name = "aCoreThePit",
    create = "CREATE TABLE IF NOT EXISTS `aCoreThePit` (`name` VARCHAR(32), `kills` LONG, `deaths` LONG, `assists` LONG, `experience` LONG, `level` LONG, `monthlykills` LONG, `monthlydeaths` LONG, `monthlyassists` LONG, `month` TEXT, `coins` DOUBLE, `laststreak` LONG, `killstreak` LONG, `cosmetics` TEXT, `selected` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `aCoreThePit` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `aCoreThePit` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `aCoreThePit` SET `kills` = ?, `deaths` = ?, `assists` = ?, `experience` = ?, `level` = ?, `monthlykills` = ?, `montlhydeaths` = ?, `monthlyassists` = ?, `month` = ?, `coins` = ?, `laststreak` = ?, `killstreak` = ?, `cosmetics` = ?, `selected` = ? WHERE LOWER(`name`) = ?"
)
public class ThePitTable extends DataTable {
  
  @Override
  public void init(Database database) {
  }
  
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("kills", new DataContainer(0L));
    defaultValues.put("deaths", new DataContainer(0L));
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
