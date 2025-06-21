package me.joaomanoel.d4rkk.dev.database;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.booster.NetworkBooster;
import me.joaomanoel.d4rkk.dev.database.cache.RoleCache;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.exception.ProfileLoadException;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class HikariDatabase extends Database {
  
  private final String host;
  private final String port;
  private final String dbname;
  private final String username;
  private final String password;
  private final boolean mariadb;
  private final ExecutorService executor;
  private HikariDataSource dataSource;
  
  public HikariDatabase(String host, String port, String dbname, String username, String password, boolean mariadb) {
    this.host = host;
    this.port = port;
    this.dbname = dbname;
    this.username = username;
    this.password = password;
    this.mariadb = mariadb;
    
    this.openConnection();
    this.executor = Executors.newCachedThreadPool();
    
    this.update(
        "CREATE TABLE IF NOT EXISTS `aCoreNetworkBooster` (`id` VARCHAR(32), `booster` TEXT, `multiplier` DOUBLE, `expires` LONG, PRIMARY KEY(`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;");
    
    DataTable.listTables().forEach(table -> {
      this.update(table.getInfo().create());
      try (
          Connection connection = getConnection();
          PreparedStatement ps = connection.prepareStatement("ALTER TABLE `" + table.getInfo().name() + "` ADD INDEX `namex` (`name` DESC)")
      ) {
        ps.executeUpdate();
      } catch (SQLException ignore) {
        // Index já existe
      }
      table.init(this);
    });
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
  }
  
  @Override
  public NetworkBooster getBooster(String minigame) {
    try (CachedRowSet rs = query("SELECT * FROM `aCoreNetworkBooster` WHERE `id` = ?", minigame)) {
      if (rs != null) {
        String booster = rs.getString("booster");
        double multiplier = rs.getDouble("multiplier");
        long expires = rs.getLong("expires");
        if (expires > System.currentTimeMillis()) {
          rs.close();
          return new NetworkBooster(booster, multiplier, expires);
        }
      }
    } catch (SQLException ignored) {
    }
    
    return null;
  }
  
  @Override
  public String getRankAndName(String player) {
    try (CachedRowSet rs = query("SELECT `name`, `role` FROM `aCoreProfile` WHERE LOWER(`name`) = ?", player.toLowerCase())) {
      if (rs != null) {
        String result = rs.getString("role") + " : " + rs.getString("name");
        RoleCache.setCache(player, rs.getString("role"), rs.getString("name"));
        return result;
      }
    } catch (SQLException ignored) {
    }
    return null;
  }
  
  @Override
  public boolean getPreference(String player, String id, boolean def) {
    boolean preference = true;
    try (CachedRowSet rs = query("SELECT `preferences` FROM `aCoreProfile` WHERE LOWER(`name`) = ?", player.toLowerCase())) {
      if (rs != null) {
        preference = ((JSONObject) new JSONParser().parse(rs.getString("preferences"))).get(id).equals(0L);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    
    return preference;
  }

  @Override
  public List<String[]> getLeaderBoard(String table, String... columns) {
    List<String[]> result = new ArrayList<>();
    StringBuilder add = new StringBuilder(), select = new StringBuilder();
    for (String column : columns) {
      add.append("`").append(column).append("` + ");
      select.append("`").append(column).append("`, ");
    }

    // lê a opção do config.yml
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

    return result;
  }

  
  @Override
  public void close() {
    this.executor.shutdownNow().forEach(Runnable::run);
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
    this.save0(name, tableMap, true);
  }
  
  @Override
  public void saveSync(String name, Map<String, Map<String, DataContainer>> tableMap) {
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
    HikariConfig config = new HikariConfig();
    config.setPoolName("aConnectionPool");
    config.setMaximumPoolSize(32);
    config.setConnectionTimeout(30000L);
    config.setDriverClassName(this.mariadb ? "org.mariadb.jdbc.Driver" : "com.mysql.jdbc.Driver");
    config.setJdbcUrl((this.mariadb ? "jdbc:mariadb://" : "jdbc:mysql://") + this.host + ":" + this.port + "/" + this.dbname);
    config.setUsername(this.username);
    config.setPassword(this.password);
    config.addDataSourceProperty("autoReconnect", "true");
    this.dataSource = new HikariDataSource(config);

    LOGGER.info("Connected to MySQL!");

  }
  
  public void closeConnection() {
    if (isConnected()) {
      this.dataSource.close();
    }
  }
  
  public Connection getConnection() throws SQLException {
    return this.dataSource.getConnection();
  }
  
  public boolean isConnected() {
    return !this.dataSource.isClosed();
  }
  
  public void update(String sql, Object... vars) {
    Connection connection = null;
    PreparedStatement ps = null;
    try {
      connection = getConnection();
      ps = connection.prepareStatement(sql);
      for (int i = 0; i < vars.length; i++) {
        ps.setObject(i + 1, vars[i]);
      }
      ps.executeUpdate();
    } catch (SQLException ex) {
      LOGGER.log(Level.WARNING, "Nao foi possivel executar um SQL: ", ex);
    } finally {
      try {
        if (connection != null && !connection.isClosed())
          connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        if (ps != null && !ps.isClosed())
          ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
  
  public void execute(String sql, Object... vars) {
    executor.execute(() -> {
      update(sql, vars);
    });
  }
  
  public int updateWithInsertId(String sql, Object... vars) {
    int id = -1;
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = getConnection();
      ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      for (int i = 0; i < vars.length; i++) {
        ps.setObject(i + 1, vars[i]);
      }
      ps.execute();
      rs = ps.getGeneratedKeys();
      if (rs.next()) {
        id = rs.getInt(1);
      }
    } catch (SQLException ex) {
      LOGGER.log(Level.WARNING, "Nao foi possivel executar um SQL: ", ex);
    } finally {
      try {
        if (connection != null && !connection.isClosed())
          connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        if (ps != null && !ps.isClosed())
          ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        if (rs != null && !rs.isClosed())
          rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    
    return id;
  }
  
  public CachedRowSet query(String query, Object... vars) {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    CachedRowSet rowSet = null;
    try {
      connection = getConnection();
      ps = connection.prepareStatement(query);
      for (int i = 0; i < vars.length; i++) {
        ps.setObject(i + 1, vars[i]);
      }
      rs = ps.executeQuery();
      rowSet = RowSetProvider.newFactory().createCachedRowSet();
      rowSet.populate(rs);
      
      if (rowSet.next()) {
        return rowSet;
      }
    } catch (SQLException ex) {
      LOGGER.log(Level.WARNING, "Nao foi possivel executar um Requisicao: ", ex);
    } finally {
      try {
        if (connection != null && !connection.isClosed())
          connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        if (ps != null && !ps.isClosed())
          ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        if (rs != null && !rs.isClosed())
          rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    
    return null;
  }
}
