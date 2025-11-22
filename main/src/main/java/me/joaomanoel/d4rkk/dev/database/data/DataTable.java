package me.joaomanoel.d4rkk.dev.database.data;

import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.MySQLDatabase;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.DataTableInfo;
import me.joaomanoel.d4rkk.dev.database.tables.*;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    TABLES.add(new MissionsTable());
    TABLES.add(new SkyWarsTable());
  }

  public static void registerTable(DataTable table) {
    TABLES.add(table);
  }

  public static Collection<DataTable> listTables() {
    return TABLES;
  }

  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      autoMigrateColumns((MySQLDatabase) database);
    }
  }

  /**
   * Método que detecta e aplica alterações automáticas nas colunas
   */
  protected void autoMigrateColumns(MySQLDatabase database) {
    DataTableInfo info = getInfo();
    String tableName = info.name();

    // Extrai as colunas definidas no CREATE TABLE
    Map<String, ColumnDefinition> expectedColumns = parseCreateStatement(info.create());

    // Obtém as colunas atuais do banco
    Map<String, ColumnDefinition> currentColumns = getCurrentColumns(database, tableName);

    // Detecta colunas novas que precisam ser adicionadas
    for (Map.Entry<String, ColumnDefinition> entry : expectedColumns.entrySet()) {
      String columnName = entry.getKey();
      ColumnDefinition expectedDef = entry.getValue();

      if (!currentColumns.containsKey(columnName)) {
        // Coluna não existe, adicionar
        String alterSQL = String.format("ALTER TABLE `%s` ADD COLUMN `%s` %s",
                tableName, columnName, expectedDef.definition);
        database.execute(alterSQL);
        Database.LOGGER.info(String.format("Added column '%s' to table '%s'", columnName, tableName));
      } else {
        // Coluna existe, verificar se o tipo mudou
        ColumnDefinition currentDef = currentColumns.get(columnName);
        if (!currentDef.type.equalsIgnoreCase(expectedDef.type)) {
          // Tipo mudou, fazer ALTER para modificar
          String alterSQL = String.format("ALTER TABLE `%s` MODIFY COLUMN `%s` %s",
                  tableName, columnName, expectedDef.definition);
          database.execute(alterSQL);
          Database.LOGGER.info(String.format("Modified column '%s' in table '%s'", columnName, tableName));
        }
      }
    }

    // Opcional: Detectar colunas removidas (comentado por segurança)
    /*
    for (String currentColumn : currentColumns.keySet()) {
      if (!expectedColumns.containsKey(currentColumn) && !currentColumn.equals("name")) {
        // Coluna existe no banco mas não está mais na definição
        // CUIDADO: Isso vai deletar dados!
        String alterSQL = String.format("ALTER TABLE `%s` DROP COLUMN `%s`", tableName, currentColumn);
        database.execute(alterSQL);
        Database.LOGGER.info(String.format("Dropped column '%s' from table '%s'", currentColumn, tableName));
      }
    }
    */
  }

  /**
   * Extrai as definições de colunas do CREATE TABLE
   */
  private Map<String, ColumnDefinition> parseCreateStatement(String createSQL) {
    Map<String, ColumnDefinition> columns = new LinkedHashMap<>();

    // Pattern para extrair colunas: `nome` TIPO
    Pattern pattern = Pattern.compile("`(\\w+)`\\s+([^,)]+?)(?:,|\\)|\\s+PRIMARY|\\s+ENGINE)");
    Matcher matcher = pattern.matcher(createSQL);

    while (matcher.find()) {
      String columnName = matcher.group(1);
      String definition = matcher.group(2).trim();

      // Ignora PRIMARY KEY e outras constraints
      if (columnName.equalsIgnoreCase("PRIMARY") || columnName.equalsIgnoreCase("INDEX")) {
        continue;
      }

      String type = definition.split("\\s+")[0];
      columns.put(columnName, new ColumnDefinition(type, definition));
    }

    return columns;
  }

  /**
   * Obtém as colunas atuais da tabela no banco de dados
   */
  private Map<String, ColumnDefinition> getCurrentColumns(MySQLDatabase database, String tableName) {
    Map<String, ColumnDefinition> columns = new LinkedHashMap<>();

    try (CachedRowSet rs = database.query("SHOW COLUMNS FROM `" + tableName + "`")) {
      if (rs != null) {
        rs.beforeFirst();
        while (rs.next()) {
          String columnName = rs.getString("Field");
          String type = rs.getString("Type");
          String nullable = rs.getString("Null");
          String defaultValue = rs.getString("Default");

          StringBuilder definition = new StringBuilder(type);
          if ("NO".equals(nullable)) {
            definition.append(" NOT NULL");
          }
          if (defaultValue != null) {
            definition.append(" DEFAULT ").append(defaultValue);
          }

          columns.put(columnName, new ColumnDefinition(type, definition.toString()));
        }
      }
    } catch (SQLException ex) {
      Database.LOGGER.warning("Failed to get current columns for table " + tableName + ": " + ex.getMessage());
    }

    return columns;
  }

  public abstract Map<String, DataContainer> getDefaultValues();

  public DataTableInfo getInfo() {
    return this.getClass().getAnnotation(DataTableInfo.class);
  }

  /**
   * Classe auxiliar para armazenar definição de coluna
   */
  private static class ColumnDefinition {
    String type;
    String definition;

    ColumnDefinition(String type, String definition) {
      this.type = type;
      this.definition = definition;
    }
  }
}
