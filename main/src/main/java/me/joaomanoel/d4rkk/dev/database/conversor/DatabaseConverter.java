package me.joaomanoel.d4rkk.dev.database.conversor;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.HikariDatabase;
import me.joaomanoel.d4rkk.dev.database.MySQLDatabase;
import me.joaomanoel.d4rkk.dev.database.SQLiteDatabase;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConverter {

  public static void convertFromSQLite(Player player, SQLiteDatabase sqliteDb, Database targetDb) {
    Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
      try {
        player.sendMessage("§6§l[aCore] §rIniciando conversão de dados do SQLite para " + targetDb.getClass().getSimpleName().replace("Database", "") + "...");

        List<String> tables = getTables(sqliteDb);
        int totalPlayers = 0;
        int convertedPlayers = 0;
        int failedPlayers = 0;

        for (String table : tables) {
          try {
            List<String> playerNames = getPlayerNamesFromTable(sqliteDb, table);
            totalPlayers += playerNames.size();

            for (String playerName : playerNames) {
              try {
                Map<String, Map<String, DataContainer>> playerData = sqliteDb.load(playerName);
                if (playerData != null && !playerData.isEmpty()) {
                  targetDb.save(playerName, playerData);
                  convertedPlayers++;
                }
              } catch (Exception e) {
                failedPlayers++;
                Core.getInstance().getLogger().warning("Falha ao converter dados de " + playerName + " da tabela " + table + ": " + e.getMessage());
              }
            }
          } catch (Exception e) {
            Core.getInstance().getLogger().warning("Erro ao processar tabela " + table + ": " + e.getMessage());
          }
        }

        int finalFailedPlayers = failedPlayers;
        int finalTotalPlayers = totalPlayers;
        int finalConvertedPlayers = convertedPlayers;
        Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
          player.sendMessage("§6§l[aCore] §r§aConversão concluída!");
          player.sendMessage("§8▪ §fTotal de jogadores: §b" + finalTotalPlayers);
          player.sendMessage("§8▪ §aConvertidos com sucesso: §b" + finalConvertedPlayers);
          player.sendMessage("§8▪ §cFalhas: §b" + finalFailedPlayers);

          if (finalFailedPlayers > 0) {
            player.sendMessage("§c⚠ Verifique os logs do servidor para mais detalhes sobre as falhas.");
          }
        });

      } catch (Exception e) {
        Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
          player.sendMessage("§c§l[aCore] §rErro durante a conversão: " + e.getMessage());
          e.printStackTrace();
        });
      }
    });
  }

  private static List<String> getTables(SQLiteDatabase sqliteDb) throws SQLException {
    List<String> tables = new ArrayList<>();
    try (Connection conn = sqliteDb.getConnection();
         ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {
      while (rs.next()) {
        String tableName = rs.getString("TABLE_NAME");
        if (tableName.startsWith("aCore")) {
          tables.add(tableName);
        }
      }
    }
    return tables;
  }

  private static List<String> getPlayerNamesFromTable(SQLiteDatabase sqliteDb, String tableName) throws SQLException {
    List<String> playerNames = new ArrayList<>();
    String query = "SELECT DISTINCT LOWER(name) as name FROM " + tableName;

    try (Connection conn = sqliteDb.getConnection();
         PreparedStatement ps = conn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        playerNames.add(rs.getString("name"));
      }
    }

    return playerNames;
  }

  public static boolean validateDatabaseConnection(Database targetDb) {
    try {
      if (targetDb instanceof MySQLDatabase) {
        MySQLDatabase mysqlDb = (MySQLDatabase) targetDb;
        return validateMySQLConnection(mysqlDb);
      } else if (targetDb instanceof HikariDatabase) {
        HikariDatabase hikariDb = (HikariDatabase) targetDb;
        return validateHikariConnection(hikariDb);
      }
      return false;
    } catch (Exception e) {
      Core.getInstance().getLogger().warning("Erro ao validar conexão com banco de dados: " + e.getMessage());
      return false;
    }
  }

  private static boolean validateMySQLConnection(MySQLDatabase db) {
    try {
      db.query("SELECT 1");
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private static boolean validateHikariConnection(HikariDatabase db) {
    try {
      db.query("SELECT 1");
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static void backupSQLiteDatabase(String backupPath) {
    try {
      File sourceFile = new File("database.db");
      File backupFile = new File(backupPath + "/database_backup_" + System.currentTimeMillis() + ".db");

      if (!backupFile.getParentFile().exists()) {
        backupFile.getParentFile().mkdirs();
      }

      java.nio.file.Files.copy(
              sourceFile.toPath(),
              backupFile.toPath(),
              java.nio.file.StandardCopyOption.REPLACE_EXISTING
      );

      Core.getInstance().getLogger().info("Backup do SQLite criado em: " + backupFile.getAbsolutePath());
    } catch (Exception e) {
      Core.getInstance().getLogger().warning("Erro ao criar backup do SQLite: " + e.getMessage());
    }
  }
}
