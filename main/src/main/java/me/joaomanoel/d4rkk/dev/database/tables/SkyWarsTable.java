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

@DataTableInfo(
        name = "aCoreSkyWars",
        create = "CREATE TABLE IF NOT EXISTS `aCoreSkyWars` (" +
                "`name` VARCHAR(32), " +
                "`kitconfig` TEXT, " +
                "`cosmetics` TEXT, " +
                "`selected` TEXT, " +
                "`souls` LONG, " +
                "`winsouls` LONG, " +
                "`maxsouls` LONG, " +
                "`wellsouls` LONG, " +
                "`level` LONG, " +
                "`insanekills` LONG, " +
                "`insanedeaths` LONG, " +
                "`experience` LONG, " +
                "`insaneassists` LONG, " +
                "`insanegames` LONG, " +
                "`insanewins` LONG, " +
                "`insane2v2kills` LONG, " +
                "`insane2v2deaths` LONG, " +
                "`insane2v2assists` LONG, " +
                "`insane2v2games` LONG, " +
                "`insane2v2wins` LONG, " +
                "`normal2v2kills` LONG, " +
                "`normal2v2deaths` LONG, " +
                "`normal2v2assists` LONG, " +
                "`normal2v2games` LONG, " +
                "`normal2v2wins` LONG, " +
                "`rankedkills` LONG, " +
                "`rankeddeaths` LONG, " +
                "`rankedassists` LONG, " +
                "`rankedgames` LONG, " +
                "`rankedwins` LONG, " +
                "`rankedpoints` LONG, " +
                "`normalkills` LONG, " +
                "`normaldeaths` LONG, " +
                "`normalassists` LONG, " +
                "`normalgames` LONG, " +
                "`normalwins` LONG, " +
                "`luckykills` LONG, " +
                "`luckydeaths` LONG, " +
                "`luckyassists` LONG, " +
                "`luckygames` LONG, " +
                "`luckywins` LONG, " +
                "`chaoskills` LONG, " +
                "`chaosdeaths` LONG, " +
                "`chaosassists` LONG, " +
                "`chaosgames` LONG, " +
                "`chaoswins` LONG, " +
                "`monthlykills` LONG, " +
                "`monthlydeaths` LONG, " +
                "`monthlypoints` LONG, " +
                "`monthlyassists` LONG, " +
                "`monthlywins` LONG, " +
                "`monthlygames` LONG, " +
                "`month` TEXT, " +
                "`coins` DOUBLE, " +
                "`lastmap` LONG, " +
                "`flavor` TEXT, " +
                "`kill_time` TEXT, " +
                "`mode` TEXT, " +
                "`head_count` LONG, " +
                "`player_name` TEXT, " +
                "`limitedcoins` LONG, " +
                "PRIMARY KEY(`name`)) " + //49
                "ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
        select = "SELECT * FROM `aCoreSkyWars` WHERE LOWER(`name`) = ?",
        insert = "INSERT INTO `aCoreSkyWars` (" +
                "name, kitconfig, cosmetics, selected, souls, head_count, winsouls, maxsouls, wellsouls, level, insanekills, insanedeaths, experience, " +
                "insaneassists, insanegames, insanewins, insane2v2kills, insane2v2deaths, insane2v2assists, " +
                "insane2v2games, insane2v2wins, normal2v2kills, normal2v2deaths, normal2v2assists, normal2v2games, " +
                "normal2v2wins, rankedkills, rankeddeaths, rankedassists, rankedgames, rankedwins, rankedpoints, luckykills, luckydeaths, luckyassists, luckygames, luckywins, chaoskills, chaosdeaths, chaosassists, chaosgames, chaoswins, " +
                "normalkills, normaldeaths, normalassists, normalgames, normalwins, monthlykills, monthlydeaths, " +
                "monthlypoints, monthlyassists, monthlywins, monthlygames, month, coins, lastmap, flavor, " + //49
                "kill_time, mode, player_name, limitedcoins) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        update = "UPDATE `aCoreSkyWars` SET " +
                "cosmetics = ?, kitconfig = ?, selected = ?" +
                "souls = ?, winsouls = ?, head_count = ?, wellsouls = ?, maxsouls = ?, level = ?, insanekills = ?, insanedeaths = ?, " +
                "experience = ?, insaneassists = ?, insanegames = ?, insanewins = ?, insane2v2kills = ?, insane2v2deaths = ?, " +
                "insane2v2assists = ?, insane2v2games = ?, insane2v2wins = ?, normal2v2kills = ?, normal2v2deaths = ?, " +
                "normal2v2assists = ?, normal2v2games = ?, normal2v2wins = ?, rankedkills = ?, rankeddeaths = ?, " +
                "rankedassists = ?, rankedgames = ?, rankedwins = ?, rankedpoints = ?, luckykills = ?, luckydeaths = ?, luckyassists = ?, luckygames = ?, luckywins = ?, chaoskills = ?, chaosdeaths = ?, chaosassists = ?, chaosgames = ?, chaoswins = ?, normalkills = ?, normaldeaths = ?, " +
                "normalassists = ?, normalgames = ?, normalwins = ?, monthlykills = ?, monthlydeaths = ?, monthlypoints = ?, " +
                "monthlyassists = ?, monthlywins = ?, monthlygames = ?, month = ?, coins = ?, lastmap = ?, " +
                "flavor = ?, kill_time = ?, mode = ?, player_name = ?, limitedcoins = ? WHERE name = ?" //49
)
public class SkyWarsTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `aCoreSkyWars` LIKE 'lastmap'") == null) {
        ((MySQLDatabase) database).execute(
                "ALTER TABLE `aCoreSkyWars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `kitconfig` TEXT AFTER `selected`");
      }
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `aCoreSkyWars` LIKE 'kill_time'") == null) {
        ((MySQLDatabase) database).execute(
                "ALTER TABLE `aCoreSkyWars` ADD `kill_time` TEXT AFTER `kitconfig`");
      }
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `aCoreSkyWars` LIKE 'mode'") == null) {
        ((MySQLDatabase) database).execute(
                "ALTER TABLE `aCoreSkyWars` ADD `mode` TEXT AFTER `kill_time`");
      }
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `aCoreSkyWars` LIKE 'player_name'") == null) {
        ((MySQLDatabase) database).execute(
                "ALTER TABLE `aCoreSkyWars` ADD `player_name` TEXT AFTER `mode`");
      }
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `aCoreSkyWars` LIKE 'flavor'") == null) {
        ((MySQLDatabase) database).execute(
                "ALTER TABLE `aCoreSkyWars` ADD `flavor` TEXT AFTER `player_name`");
      }
    } else if (database instanceof HikariDatabase) {
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `aCoreSkyWars` LIKE 'lastmap'") == null) {
        ((HikariDatabase) database).execute(
                "ALTER TABLE `aCoreSkyWars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `kitconfig` TEXT AFTER `selected`");
      }
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `aCoreSkyWars` LIKE 'kill_time'") == null) {
        ((HikariDatabase) database).execute(
                "ALTER TABLE `aCoreSkyWars` ADD `kill_time` TEXT AFTER `kitconfig`");
      }
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `aCoreSkyWars` LIKE 'mode'") == null) {
        ((HikariDatabase) database).execute(
                "ALTER TABLE `aCoreSkyWars` ADD `mode` TEXT AFTER `kill_time`");
      }
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `aCoreSkyWars` LIKE 'player_name'") == null) {
        ((HikariDatabase) database).execute(
                "ALTER TABLE `aCoreSkyWars` ADD `player_name` TEXT AFTER `mode`");
      }
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `aCoreSkyWars` LIKE 'flavor'") == null) {
        ((HikariDatabase) database).execute(
                "ALTER TABLE `aCoreSkyWars` ADD `flavor` TEXT AFTER `player_name`");
      }
    }
  }

  @Override
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("kitconfig", new DataContainer("{}"));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    defaultValues.put("souls", new DataContainer(0));
    defaultValues.put("winsouls", new DataContainer(0));
    defaultValues.put("head_count", new DataContainer(0));
    defaultValues.put("maxsouls", new DataContainer(250));
    defaultValues.put("wellsouls", new DataContainer(1));
    defaultValues.put("level", new DataContainer(1));
    defaultValues.put("insanekills", new DataContainer(0L));
    defaultValues.put("insanedeaths", new DataContainer(0L));
    defaultValues.put("experience", new DataContainer(0L));
    defaultValues.put("insaneassists", new DataContainer(0L));
    defaultValues.put("insanegames", new DataContainer(0L));
    defaultValues.put("insanewins", new DataContainer(0L));
    defaultValues.put("insane2v2kills", new DataContainer(0L));
    defaultValues.put("insane2v2deaths", new DataContainer(0L));
    defaultValues.put("insane2v2assists", new DataContainer(0L));
    defaultValues.put("insane2v2games", new DataContainer(0L));
    defaultValues.put("insane2v2wins", new DataContainer(0L));
    defaultValues.put("normal2v2kills", new DataContainer(0L));
    defaultValues.put("normal2v2deaths", new DataContainer(0L));
    defaultValues.put("normal2v2assists", new DataContainer(0L));
    defaultValues.put("normal2v2games", new DataContainer(0L));
    defaultValues.put("normal2v2wins", new DataContainer(0L));
    defaultValues.put("normalkills", new DataContainer(0L));
    defaultValues.put("normaldeaths", new DataContainer(0L));
    defaultValues.put("normalassists", new DataContainer(0L));
    defaultValues.put("normalgames", new DataContainer(0L));
    defaultValues.put("normalwins", new DataContainer(0L));
    defaultValues.put("rankedkills", new DataContainer(0L));
    defaultValues.put("rankeddeaths", new DataContainer(0L));
    defaultValues.put("rankedassists", new DataContainer(0L));
    defaultValues.put("rankedgames", new DataContainer(0L));
    defaultValues.put("rankedwins", new DataContainer(0L));
    defaultValues.put("rankedpoints", new DataContainer(0L));
    defaultValues.put("luckykills", new DataContainer(0L));
    defaultValues.put("luckydeaths", new DataContainer(0L));
    defaultValues.put("luckyassists", new DataContainer(0L));
    defaultValues.put("luckygames", new DataContainer(0L));
    defaultValues.put("luckywins", new DataContainer(0L));
    defaultValues.put("chaoskills", new DataContainer(0L));
    defaultValues.put("chaosdeaths", new DataContainer(0L));
    defaultValues.put("chaosassists", new DataContainer(0L));
    defaultValues.put("chaosgames", new DataContainer(0L));
    defaultValues.put("chaoswins", new DataContainer(0L));

    for (String key : new String[]{"kills", "deaths", "points", "assists", "wins", "games"}) {
      defaultValues.put("monthly" + key, new DataContainer(0L));
    }
    defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR)));
    defaultValues.put("coins", new DataContainer(0L));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("flavor", new DataContainer(""));
    defaultValues.put("kill_time", new DataContainer(""));
    defaultValues.put("mode", new DataContainer(""));
    defaultValues.put("player_name", new DataContainer(""));
    defaultValues.put("limitedcoins", new DataContainer(15000L));
    return defaultValues;
  }
}
