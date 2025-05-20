package me.joaomanoel.d4rkk.dev.database.data;

import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.DataTableInfo;
import me.joaomanoel.d4rkk.dev.database.tables.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class DataTable {
  
  private static final List<DataTable> TABLES = new ArrayList<>();
  
  static {
    TABLES.add(new CoreTable());
    TABLES.add(new BlockSumoTable());
    TABLES.add(new BedWarsTable());
    TABLES.add(new ThePitTable());
    TABLES.add(new TheBridgeTable());
    TABLES.add(new BatteryDashTable());
    TABLES.add(new DuelsTable());
    TABLES.add(new SkyWarsTable());
  }
  
  public static void registerTable(DataTable table) {
    TABLES.add(table);
  }
  
  public static Collection<DataTable> listTables() {
    return TABLES;
  }
  
  public abstract void init(Database database);
  
  public abstract Map<String, DataContainer> getDefaultValues();
  
  public DataTableInfo getInfo() {
    return this.getClass().getAnnotation(DataTableInfo.class);
  }
}
