package me.joaomanoel.d4rkk.dev.database;

import me.joaomanoel.d4rkk.dev.booster.NetworkBooster;
import me.joaomanoel.d4rkk.dev.database.cache.RoleCache;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.exception.ProfileLoadException;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public abstract class Database {

  public static Logger LOGGER;
  private static Database instance;
  private static boolean IS_VELOCITY = false;

  public static void setupDatabase(String type, String mysqlHost, String mysqlPort, String mysqlDbname,
                                   String mysqlUsername, String mysqlPassword, boolean hikari, boolean mariadb,
                                   String mongoURL, File sqliteFile) {
    try {
      Class.forName("com.velocitypowered.api.proxy.ProxyServer");
      IS_VELOCITY = true;
      LOGGER = getVelocityLogger();
    } catch (ClassNotFoundException e) {
      IS_VELOCITY = false;
      LOGGER = getBukkitLogger();
    }

    if (type.equalsIgnoreCase("mongodb")) {
      instance = new MongoDBDatabase(mongoURL);
    } else if (type.equalsIgnoreCase("sqlite")) {
      instance = new SQLiteDatabase(sqliteFile);
    } else {
      if (hikari) {
        instance = new HikariDatabase(mysqlHost, mysqlPort, mysqlDbname, mysqlUsername, mysqlPassword, mariadb);
      } else {
        instance = new MySQLDatabase(mysqlHost, mysqlPort, mysqlDbname, mysqlUsername, mysqlPassword, mariadb);
      }
    }

    new Timer().scheduleAtFixedRate(RoleCache.clearCache(), TimeUnit.SECONDS.toMillis(60), TimeUnit.SECONDS.toMillis(60));
  }

  private static Logger getVelocityLogger() {
    try {
      Class<?> velocityPlugin = Class.forName("me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin");
      Object instance = velocityPlugin.getMethod("getInstance").invoke(null);
      Object slf4jLogger = velocityPlugin.getMethod("getLogger").invoke(instance);

      return new VelocityLoggerWrapper((org.slf4j.Logger) slf4jLogger);
    } catch (Exception e) {
      throw new RuntimeException("Failed to get Velocity logger", e);
    }
  }

  private static Logger getBukkitLogger() {
    try {
      try {
        Class<?> core = Class.forName("me.joaomanoel.d4rkk.dev.Core");
        Object instance = core.getMethod("getInstance").invoke(null);
        return (Logger) core.getMethod("getLogger").invoke(instance);
      } catch (ClassNotFoundException e) {
        Class<?> bungee = Class.forName("me.joaomanoel.d4rkk.dev.bungee.Bungee");
        Object instance = bungee.getMethod("getInstance").invoke(null);
        return (Logger) bungee.getMethod("getLogger").invoke(instance);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to get Bukkit/Bungee logger", e);
    }
  }

  public static Database getInstance() {
    return instance;
  }

  public static boolean isVelocity() {
    return IS_VELOCITY;
  }

  public void setupBoosters() {
  }

  public void convertDatabase(Object player) {
    // Implementação vazia - cada subclasse implementa se necessário
  }

  public abstract void convertDatabase(Player player);

  public abstract void setBooster(String minigame, String booster, double multiplier, long expires);

  public abstract NetworkBooster getBooster(String minigame);

  public abstract String getRankAndName(String player);

  public abstract boolean getPreference(String player, String id, boolean def);

  public abstract List<String[]> getLeaderBoard(String table, String... columns);

  public abstract void close();

  public abstract Map<String, Map<String, DataContainer>> load(String name) throws ProfileLoadException;

  public abstract void save(String name, Map<String, Map<String, DataContainer>> tableMap);

  public abstract void saveSync(String name, Map<String, Map<String, DataContainer>> tableMap);

  public abstract String exists(String name);

  /**
   * Wrapper para converter SLF4J Logger (Velocity) para java.util.logging.Logger
   */
  private static class VelocityLoggerWrapper extends Logger {
    private final org.slf4j.Logger slf4jLogger;

    public VelocityLoggerWrapper(org.slf4j.Logger slf4jLogger) {
      super("VelocityLogger", null);
      this.slf4jLogger = slf4jLogger;
    }

    @Override
    public void info(String msg) {
      slf4jLogger.info(msg);
    }

    @Override
    public void warning(String msg) {
      slf4jLogger.warn(msg);
    }

    @Override
    public void severe(String msg) {
      slf4jLogger.error(msg);
    }

    @Override
    public void log(java.util.logging.Level level, String msg) {
      if (level == java.util.logging.Level.SEVERE) {
        slf4jLogger.error(msg);
      } else if (level == java.util.logging.Level.WARNING) {
        slf4jLogger.warn(msg);
      } else {
        slf4jLogger.info(msg);
      }
    }

    @Override
    public void log(java.util.logging.Level level, String msg, Throwable thrown) {
      if (level == java.util.logging.Level.SEVERE) {
        slf4jLogger.error(msg, thrown);
      } else if (level == java.util.logging.Level.WARNING) {
        slf4jLogger.warn(msg, thrown);
      } else {
        slf4jLogger.info(msg, thrown);
      }
    }
  }
}