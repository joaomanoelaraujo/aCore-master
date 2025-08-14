package me.joaomanoel.d4rkk.dev.database.tables;

import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.DataTableInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
        name = "aCoreMissions",
        create = "CREATE TABLE IF NOT EXISTS `aCoreMissions` (`name` VARCHAR(32), `SkyWars` TEXT, `BedWars` TEXT, `Duels` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
        select = "SELECT * FROM `aCoreMissions` WHERE LOWER(`name`) = ?",
        insert = "INSERT INTO `aCoreMissions` VALUES (?, ?, ?, ?)",
        update = "UPDATE `aCoreMissions` SET `SkyWars` = ?, `BedWars` = ?, `Duels` = ? WHERE LOWER(`name`) = ?"
)
public class MissionsTable extends DataTable {

    @Override
    public void init(Database database) {}

    @Override
    public Map<String, DataContainer> getDefaultValues() {
        Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
        defaultValues.put("SkyWars", new DataContainer(""));
        defaultValues.put("BedWars", new DataContainer(""));
        defaultValues.put("Duels", new DataContainer(""));
        return defaultValues;
    }
}
