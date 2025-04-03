package me.joaomanoel.d4rkk.dev.database.tables;

import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.DataTableInfo;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
    name = "aCoreBlockSumo",
    create = "CREATE TABLE IF NOT EXISTS `aCoreBlockSumo` (`name` VARCHAR(32), `skills` LONG, `sdeaths` LONG, `experience` LONG, `level` LONG, `sassists` LONG, `sgames` LONG, `swins` LONG, `normalkills` LONG, `normaldeaths` LONG, `normalassists` LONG, `normalgames` LONG, `normalwins` LONG, `normalpoints` LONG, `monthlykills` LONG, `monthlydeaths` LONG, `monthlypoints` LONG, `monthlyassists` LONG, `monthlywins` LONG, `monthlygames` LONG, `month` TEXT, `coins` DOUBLE, `cosmetics` TEXT, `selected` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `aCoreBlockSumo` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `aCoreBlockSumo` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `aCoreBlockSumo` SET `skills` = ?, `sdeaths` = ?, `sassists` = ?, `sgames` = ?, `swins` = ?, `experience` = ?, `level` = ?, `normalkills` = ?, `normaldeaths` = ?, `normalassists` = ?, `normalgames` = ?, `normalwins` = ?, `normalpoints` = ?, `monthlykills` = ?, `montlhydeaths` = ?, `monthlypoints` = ?, `monthlyassists` = ?, `monthlywins` = ?, `monthlygames` = ?, `month` = ?, `coins` = ?, `cosmetics` = ?, `selected` = ? WHERE LOWER(`name`) = ?"
)
public class BlockSumoTable extends DataTable {
  
  @Override
  public void init(Database database) {
  }
  
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("skills", new DataContainer(0L));
    defaultValues.put("sdeaths", new DataContainer(0L));
    defaultValues.put("sassists", new DataContainer(0L));
    defaultValues.put("sgames", new DataContainer(0L));
    defaultValues.put("swins", new DataContainer(0L));
    defaultValues.put("normalkills", new DataContainer(0L));
    defaultValues.put("normaldeaths", new DataContainer(0L));
    defaultValues.put("normalassists", new DataContainer(0L));
    defaultValues.put("normalgames", new DataContainer(0L));
    defaultValues.put("normalwins", new DataContainer(0L));
    defaultValues.put("normalpoints", new DataContainer(0L));
    for (String key : new String[]{"kills", "deaths", "points",
        "assists", "wins", "games"}) {
      defaultValues.put("monthly" + key, new DataContainer(0L));
    }
    defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" +
        Calendar.getInstance().get(Calendar.YEAR)));
    defaultValues.put("coins", new DataContainer(0L));
    defaultValues.put("experience", new DataContainer(0L));
    defaultValues.put("level", new DataContainer(1L));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    return defaultValues;
  }
}
