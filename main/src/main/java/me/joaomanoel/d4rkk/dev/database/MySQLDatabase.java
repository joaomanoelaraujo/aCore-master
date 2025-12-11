package me.joaomanoel.d4rkk.dev.database;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.booster.NetworkBooster;
import me.joaomanoel.d4rkk.dev.database.cache.DatabaseCache;
import me.joaomanoel.d4rkk.dev.database.cache.RoleCache;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.exception.ProfileLoadException;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class MySQLDatabase extends Database {

  private final String host;
  private final String port;
  private final String dbname;
  private final String username;
  private final String password;
  private final boolean mariadb;

  private Connection connection;
  private final ExecutorService executor;

  public MySQLDatabase(String host, String port, String dbname, String username, String password, boolean mariadb) {
    this(host, port, dbname, username, password, mariadb, false);
  }

  public MySQLDatabase(String host, String port, String dbname, String username, String password, boolean mariadb, boolean skipTables) {
    this.host = host;
    this.port = port;
    this.dbname = dbname;
    this.username = username;
    this.password = password;
    this.mariadb = mariadb;

    this.openConnection();
    this.executor = createOptimizedExecutor();

    if (!skipTables) {
      this.update(
              "CREATE TABLE IF NOT EXISTS `aCoreNetworkBooster` (`id` VARCHAR(32), `booster` TEXT, `multiplier` DOUBLE, `expires` LONG, PRIMARY KEY(`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;");

      DataTable.listTables().forEach(table -> {
        this.update(table.getInfo().create());
        try (PreparedStatement ps = prepareStatement("ALTER TABLE `" + table.getInfo().name() + "` ADD INDEX `namex` (`name` DESC)")) {
          ps.executeUpdate();
        } catch (SQLException ignore) {
        }
        table.init(this);
      });
    }

    scheduleCleanupTask();
  }

  private ExecutorService createOptimizedExecutor() {
    ThreadFactory threadFactory = new ThreadFactory() {
      private final AtomicInteger counter = new AtomicInteger(0);

      @Override
      public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("aCore-MySQL-Worker-" + counter.incrementAndGet());
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
  public void convertDatabase(Player player) {
    player.sendMessage("§aJá está usando " + this.getClass().getSimpleName().replace("Database", ""));
    player.sendMessage("§7Para converter FROM SQLite, mude a config para SQLite primeiro, depois altere para MySQL/Hikari e rode /ac convert");
  }

  @Override
  public void setupBoosters() {
    if (!Manager.BUNGEE) {
      for (String mg : Core.minigames) {
        if (query("SELECT * FROM `aCoreNetworkBooster` WHERE `id` = ?", mg) == null) {
          execute("INSERT INTO `aCoreNetworkBooster` VALUES (?, ?, ?, ?)", mg, "d4rkk", 1.0, 0L);
        }
      }
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

    try (CachedRowSet rs = query("SELECT * FROM `aCoreNetworkBooster` WHERE `id` = ?", minigame)) {
      if (rs != null) {
        String booster = rs.getString("booster");
        double multiplier = rs.getDouble("multiplier");
        long expires = rs.getLong("expires");
        if (expires > System.currentTimeMillis()) {
          rs.close();
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

    try (CachedRowSet rs = query("SELECT `name`, `role` FROM `aCoreProfile` WHERE LOWER(`name`) = ?", player.toLowerCase())) {
      if (rs != null) {
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

    boolean preference = true;
    try (CachedRowSet rs = query("SELECT `preferences` FROM `aCoreProfile` WHERE LOWER(`name`) = ?", player.toLowerCase())) {
      if (rs != null) {
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

    try (CachedRowSet rs = query(sql)) {
      if (rs != null) {
        rs.beforeFirst();
        while (rs.next()) {
          long count   = 0;
          String raw   = rs.getString("name");
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
      }
    } catch (SQLException ignore) {}

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

      try (CachedRowSet rs = this.query(table.getInfo().select(), name.toLowerCase())) {
        if (rs != null) {
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
    try {
      return this.query("SELECT `name` FROM `aCoreProfile` WHERE LOWER(`name`) = ?", name.toLowerCase()).getString("name");
    } catch (Exception ex) {
      return null;
    }
  }

  public void openConnection() {
    try {
      boolean reconnected = this.connection != null;
      Class.forName(this.mariadb ? "org.mariadb.jdbc.Driver" : "com.mysql.cj.jdbc.Driver");

      this.connection = DriverManager.getConnection(
              (this.mariadb ? "jdbc:mariadb://" : "jdbc:mysql://") +
                      host + ":" + port + "/" + dbname +
                      "?verifyServerCertificate=false" +
                      "&useSSL=false" +
                      "&useUnicode=yes" +
                      "&characterEncoding=UTF-8" +
                      "&serverTimezone=UTC" +
                      "&allowPublicKeyRetrieval=true" +
                      "&cachePrepStmts=true" +
                      "&prepStmtCacheSize=250" +
                      "&prepStmtCacheSqlLimit=2048" +
                      "&useServerPrepStmts=true",
              username,
              password
      );

      if (reconnected) {
        LOGGER.info("Reconnected to MySQL!");
        return;
      }

      LOGGER.info("Connected to MySQL with optimizations!");
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Failed to connect to MySQL: ", ex);
      System.exit(0);
    }
  }

  public void closeConnection() {
    if (isConnected()) {
      try {
        connection.close();
      } catch (SQLException e) {
        LOGGER.log(Level.WARNING, "Failed to close MySQL connection: ", e);
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
      return !(connection == null || connection.isClosed() || !connection.isValid(5));
    } catch (SQLException ex) {
      LOGGER.log(Level.SEVERE, "Failed to verify MySQL connection: ", ex);
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

  public int updateWithInsertId(String sql, Object... vars) {
    int id = -1;
    ResultSet rs = null;
    try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      for (int i = 0; i < vars.length; i++) {
        ps.setObject(i + 1, vars[i]);
      }
      ps.execute();
      rs = ps.getGeneratedKeys();
      if (rs.next()) {
        id = rs.getInt(1);
      }
    } catch (SQLException ex) {
      LOGGER.log(Level.WARNING, "Failed to execute an SQL query: ", ex);
    } finally {
      try {
        if (rs != null && !rs.isClosed())
          rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return id;
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

  public CachedRowSet query(String query, Object... vars) {
    CachedRowSet rowSet = null;
    try {
      Future<CachedRowSet> future = executor.submit(() -> {
        CachedRowSet crs = null;
        try (PreparedStatement ps = prepareStatement(query, vars); ResultSet rs = ps.executeQuery()) {

          CachedRowSet rs2 = RowSetProvider.newFactory().createCachedRowSet();
          rs2.populate(rs);

          if (rs2.next()) {
            crs = rs2;
          }
        } catch (SQLException ex) {
          LOGGER.log(Level.WARNING, "Failed to execute a Request: ", ex);
        }

        return crs;
      });

      if (future.get() != null) {
        rowSet = future.get();
      }
    } catch (Exception ex) {
      LOGGER.log(Level.WARNING, "Failed to schedule a Future Task: ", ex);
    }

    return rowSet;
  }
}