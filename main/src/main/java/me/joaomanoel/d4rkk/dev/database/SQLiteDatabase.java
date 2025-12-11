package me.joaomanoel.d4rkk.dev.database;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.booster.NetworkBooster;
import me.joaomanoel.d4rkk.dev.database.cache.DatabaseCache;
import me.joaomanoel.d4rkk.dev.database.cache.RoleCache;
import me.joaomanoel.d4rkk.dev.database.conversor.DatabaseConverter;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.exception.ProfileLoadException;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SQLiteDatabase extends Database {

  private final File databaseFile;
  private Connection connection;
  private final ExecutorService executor;

  public SQLiteDatabase(File databaseFile) {
    this(databaseFile, false);
  }

  public SQLiteDatabase(File databaseFile, boolean skipTables) {
    this.databaseFile = databaseFile;

    if (!databaseFile.exists()) {
      try {
        databaseFile.getParentFile().mkdirs();
        databaseFile.createNewFile();
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Failed to create SQLite database file: ", e);
      }
    }

    this.openConnection();
    this.executor = createOptimizedExecutor();

    if (!skipTables) {
      this.update(
              "CREATE TABLE IF NOT EXISTS `aCoreNetworkBooster` (`id` VARCHAR(32) PRIMARY KEY, `booster` TEXT, `multiplier` DOUBLE, `expires` LONG);");

      DataTable.listTables().forEach(table -> {
        String createQuery = table.getInfo().create()
                .replace("ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin", "")
                .replace("DEFAULT CHARSET=utf8", "");
        this.update(createQuery);
        table.init(this);
      });

      // Otimizações do SQLite
      this.update("PRAGMA journal_mode=WAL;"); // Write-Ahead Logging
      this.update("PRAGMA synchronous=NORMAL;"); // Menos seguro, mas mais rápido
      this.update("PRAGMA cache_size=10000;"); // Cache maior
      this.update("PRAGMA temp_store=MEMORY;"); // Temp tables em memória
      this.update("PRAGMA mmap_size=30000000000;"); // Memory-mapped I/O
    }

    scheduleCleanupTask();
  }

  private ExecutorService createOptimizedExecutor() {
    ThreadFactory threadFactory = new ThreadFactory() {
      private final AtomicInteger counter = new AtomicInteger(0);

      @Override
      public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("aCore-SQLite-Worker-" + counter.incrementAndGet());
        thread.setDaemon(true);
        thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
      }
    };

    int threadPoolSize = Math.max(4, Runtime.getRuntime().availableProcessors() * 2);

    return new ThreadPoolExecutor(
            threadPoolSize / 2,
            threadPoolSize,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy()
    );
  }

  private void scheduleCleanupTask() {
    Executors.newSingleThreadScheduledExecutor(r -> {
      Thread t = new Thread(r);
      t.setName("aCore-Cache-Cleaner");
      t.setDaemon(true);
      return t;
    }).scheduleAtFixedRate(() -> {
      DatabaseCache.cleanExpired();
      LOGGER.info("Cache cleaned. Current size: " + DatabaseCache.size());
    }, 5, 5, TimeUnit.MINUTES);
  }

  @Override
  public void setupBoosters() {
    if (!Manager.BUNGEE) {
      for (String mg : Core.minigames) {
        if (!existsBooster(mg)) {
          execute("INSERT INTO `aCoreNetworkBooster` VALUES (?, ?, ?, ?)", mg, "d4rkk", 1.0, 0L);
        }
      }
    }
  }

  private boolean existsBooster(String minigame) {
    try (PreparedStatement ps = prepareStatement("SELECT * FROM `aCoreNetworkBooster` WHERE `id` = ?", minigame);
         ResultSet rs = ps.executeQuery()) {
      return rs.next();
    } catch (SQLException e) {
      return false;
    }
  }

  @Override
  public void setBooster(String minigame, String booster, double multiplier, long expires) {
    execute("UPDATE `aCoreNetworkBooster` SET `booster` = ?, `multiplier` = ?, `expires` = ? WHERE `id` = ?", booster, multiplier, expires, minigame);
    DatabaseCache.invalidate("booster:" + minigame);
  }

  @Override
  public NetworkBooster getBooster(String minigame) {
    NetworkBooster cached = DatabaseCache.get("booster:" + minigame);
    if (cached != null) {
      return cached;
    }

    try (PreparedStatement ps = prepareStatement("SELECT * FROM `aCoreNetworkBooster` WHERE `id` = ?", minigame);
         ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        String booster = rs.getString("booster");
        double multiplier = rs.getDouble("multiplier");
        long expires = rs.getLong("expires");
        if (expires > System.currentTimeMillis()) {
          NetworkBooster nb = new NetworkBooster(booster, multiplier, expires);
          DatabaseCache.put("booster:" + minigame, nb, TimeUnit.MINUTES.toMillis(10));
          return nb;
        }
      }
    } catch (SQLException ignored) {
    }

    return null;
  }

  @Override
  public String getRankAndName(String player) {
    String cached = DatabaseCache.get("rank:" + player.toLowerCase());
    if (cached != null) {
      return cached;
    }

    try (PreparedStatement ps = prepareStatement("SELECT `name`, `role` FROM `aCoreProfile` WHERE LOWER(`name`) = ?", player.toLowerCase());
         ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        String result = rs.getString("role") + " : " + rs.getString("name");
        RoleCache.setCache(player, rs.getString("role"), rs.getString("name"));
        DatabaseCache.put("rank:" + player.toLowerCase(), result, TimeUnit.MINUTES.toMillis(10));
        return result;
      }
    } catch (SQLException ignored) {
    }
    return null;
  }

  @Override
  public boolean getPreference(String player, String id, boolean def) {
    String cacheKey = "pref:" + player.toLowerCase() + ":" + id;
    Boolean cached = DatabaseCache.get(cacheKey);
    if (cached != null) {
      return cached;
    }

    boolean preference = def;
    try (PreparedStatement ps = prepareStatement("SELECT `preferences` FROM `aCoreProfile` WHERE LOWER(`name`) = ?", player.toLowerCase());
         ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        preference = ((JSONObject) new JSONParser().parse(rs.getString("preferences"))).get(id).equals(0L);
        DatabaseCache.put(cacheKey, preference, TimeUnit.MINUTES.toMillis(15));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return preference;
  }

  @Override
  public List<String[]> getLeaderBoard(String table, String... columns) {
    String cacheKey = "leaderboard:" + table + ":" + String.join(",", columns);
    List<String[]> cached = DatabaseCache.get(cacheKey);
    if (cached != null) {
      return cached;
    }

    List<String[]> result = new ArrayList<>();
    StringBuilder add = new StringBuilder(), select = new StringBuilder();
    for (String column : columns) {
      add.append("`").append(column).append("` + ");
      select.append("`").append(column).append("`, ");
    }

    boolean showRole = Core.getInstance()
            .getConfig()
            .getBoolean("leaderboard.show-role", true);

    String sql = "SELECT " + select + "`name` FROM `" + table +
            "` ORDER BY " + add + " 0 DESC LIMIT 100";

    try (PreparedStatement ps = prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        long count = 0;
        String raw = rs.getString("name");
        for (String col : columns) {
          count += rs.getLong(col);
        }

        String displayName;
        if (showRole) {
          displayName = Role.getPrefixed(raw);
        } else {
          Role roleObj = Role.getRoleByName(
                  Database.getInstance()
                          .getRankAndName(raw)
                          .split(" : ")[0]
          );
          String colorOnly = roleObj != null
                  ? StringUtils.getLastColor(roleObj.getPrefix())
                  : "";
          displayName = colorOnly + raw;
        }

        result.add(new String[]{
                displayName,
                StringUtils.formatNumber(count)
        });
      }
    } catch (SQLException ignore) {
    }

    DatabaseCache.put(cacheKey, result, TimeUnit.SECONDS.toMillis(30));
    return result;
  }

  @Override
  public void close() {
    this.executor.shutdownNow().forEach(Runnable::run);
    DatabaseCache.clear();
    this.closeConnection();
  }

  @Override
  public Map<String, Map<String, DataContainer>> load(String name) throws ProfileLoadException {
    Map<String, Map<String, DataContainer>> tableMap = new HashMap<>();
    for (DataTable table : DataTable.listTables()) {
      Map<String, DataContainer> containerMap = new LinkedHashMap<>();
      tableMap.put(table.getInfo().name(), containerMap);

      try (PreparedStatement ps = prepareStatement(table.getInfo().select(), name.toLowerCase());
           ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          for (int collumn = 2; collumn <= rs.getMetaData().getColumnCount(); collumn++) {
            containerMap.put(rs.getMetaData().getColumnName(collumn), new DataContainer(rs.getObject(collumn)));
          }
          continue;
        }
      } catch (SQLException ex) {
        throw new ProfileLoadException(ex.getMessage());
      }

      containerMap = table.getDefaultValues();
      tableMap.put(table.getInfo().name(), containerMap);
      List<Object> list = new ArrayList<>();
      list.add(name);
      list.addAll(containerMap.values().stream().map(DataContainer::get).collect(Collectors.toList()));
      this.execute(table.getInfo().insert(), list.toArray());
      list.clear();
    }

    return tableMap;
  }

  @Override
  public void convertDatabase(Player player) {
    KConfig config = KConfig.getConfig(Core.getInstance(), Core.getInstance().getDataFolder().getPath(), "config");
    String type = config.getString("database.type");

    Database targetDb;

    if (type.equalsIgnoreCase("mysql")) {
      boolean hikari = config.getBoolean("database.mysql.hikari");
      boolean mariadb = config.getBoolean("database.mysql.mariadb");

      if (hikari) {
        targetDb = new HikariDatabase(
                config.getString("database.mysql.host"),
                config.getString("database.mysql.port"),
                config.getString("database.mysql.database"),
                config.getString("database.mysql.username"),
                config.getString("database.mysql.password"),
                mariadb
        );
      } else {
        targetDb = new MySQLDatabase(
                config.getString("database.mysql.host"),
                config.getString("database.mysql.port"),
                config.getString("database.mysql.database"),
                config.getString("database.mysql.username"),
                config.getString("database.mysql.password"),
                mariadb
        );
      }

    } else if (type.equalsIgnoreCase("mongodb")) {
      targetDb = new MongoDBDatabase(config.getString("database.mongodb.url"));

    } else {
      player.sendMessage("§cBanco alvo inválido ou igual ao SQLite!");
      return;
    }

    player.sendMessage("§6§l[aCore] §rCriando backup do SQLite...");
    DatabaseConverter.backupSQLiteDatabase("backups");

    player.sendMessage("§6§l[aCore] §rIniciando conversão...");
    DatabaseConverter.convertFromSQLite(player, this, targetDb);
  }

  @Override
  public void save(String name, Map<String, Map<String, DataContainer>> tableMap) {
    DatabaseCache.invalidatePattern("rank:" + name.toLowerCase());
    DatabaseCache.invalidatePattern("pref:" + name.toLowerCase());
    this.save0(name, tableMap, true);
  }

  @Override
  public void saveSync(String name, Map<String, Map<String, DataContainer>> tableMap) {
    DatabaseCache.invalidatePattern("rank:" + name.toLowerCase());
    DatabaseCache.invalidatePattern("pref:" + name.toLowerCase());
    this.save0(name, tableMap, false);
  }

  private void save0(String name, Map<String, Map<String, DataContainer>> tableMap, boolean async) {
    for (DataTable table : DataTable.listTables()) {
      Map<String, DataContainer> rows = tableMap.get(table.getInfo().name());
      if (rows.values().stream().noneMatch(DataContainer::isUpdated)) {
        continue;
      }

      List<Object> values = rows.values().stream().filter(DataContainer::isUpdated).map(DataContainer::get).collect(Collectors.toList());
      StringBuilder query = new StringBuilder("UPDATE `" + table.getInfo().name() + "` SET ");
      for (Map.Entry<String, DataContainer> collumn : rows.entrySet()) {
        if (collumn.getValue().isUpdated()) {
          collumn.getValue().setUpdated(false);
          query.append("`").append(collumn.getKey()).append("` = ?, ");
        }
      }
      query.deleteCharAt(query.length() - 1);
      query.deleteCharAt(query.length() - 1);
      query.append(" WHERE LOWER(`name`) = ?");
      values.add(name.toLowerCase());
      if (async) {
        this.execute(query.toString(), values.toArray());
      } else {
        this.update(query.toString(), values.toArray());
      }
      values.clear();
    }
  }

  @Override
  public String exists(String name) {
    try (PreparedStatement ps = prepareStatement("SELECT `name` FROM `aCoreProfile` WHERE LOWER(`name`) = ?", name.toLowerCase());
         ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        return rs.getString("name");
      }
    } catch (Exception ex) {
      return null;
    }
    return null;
  }

  public void openConnection() {
    try {
      boolean reconnected = this.connection != null;
      Class.forName("org.sqlite.JDBC");
      this.connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
      if (reconnected) {
        LOGGER.info("Reconnected to SQLite!");
        return;
      }

      LOGGER.info("Connected to SQLite with optimizations!");
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Failed to connect to SQLite: ", ex);
      System.exit(0);
    }
  }

  public void closeConnection() {
    if (isConnected()) {
      try {
        connection.close();
      } catch (SQLException e) {
        LOGGER.log(Level.WARNING, "Failed to close SQLite connection: ", e);
      }
    }
  }

  public Connection getConnection() throws SQLException {
    if (!isConnected()) {
      this.openConnection();
    }

    return this.connection;
  }

  public boolean isConnected() {
    try {
      return !(connection == null || connection.isClosed());
    } catch (SQLException ex) {
      LOGGER.log(Level.SEVERE, "Failed to verify SQLite connection: ", ex);
      return false;
    }
  }

  public void update(String sql, Object... vars) {
    try (PreparedStatement ps = prepareStatement(sql, vars)) {
      ps.executeUpdate();
    } catch (SQLException ex) {
      LOGGER.log(Level.WARNING, "Failed to execute an SQL query: ", ex);
    }
  }

  public void execute(String sql, Object... vars) {
    executor.execute(() -> update(sql, vars));
  }

  public PreparedStatement prepareStatement(String query, Object... vars) {
    try {
      PreparedStatement ps = getConnection().prepareStatement(query);
      for (int i = 0; i < vars.length; i++) {
        ps.setObject(i + 1, vars[i]);
      }
      return ps;
    } catch (SQLException ex) {
      LOGGER.log(Level.WARNING, "Failed to prepare an SQL statement: ", ex);
    }

    return null;
  }

  public String query(String query, Object... vars) {
    try {
      Future<String> future = executor.submit(() -> {
        try (PreparedStatement ps = prepareStatement(query, vars);
             ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            return rs.getString(1);
          }
        } catch (SQLException ex) {
          LOGGER.log(Level.WARNING, "Failed to execute a Request: ", ex);
        }
        return null;
      });

      return future.get();
    } catch (Exception ex) {
      LOGGER.log(Level.WARNING, "Failed to schedule a Future Task: ", ex);
    }

    return null;
  }
}