package me.joaomanoel.d4rkk.dev.database;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.booster.NetworkBooster;
import me.joaomanoel.d4rkk.dev.database.cache.RoleCache;
import me.joaomanoel.d4rkk.dev.database.conversor.MongoDBConversor;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.DataTable;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.DataTableInfo;
import me.joaomanoel.d4rkk.dev.database.exception.ProfileLoadException;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.reflection.Accessors;
import me.joaomanoel.d4rkk.dev.reflection.acessors.MethodAccessor;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

public class MongoDBDatabase extends Database {
  
  private final String url;
  
  private MongoClient client;
  private MongoDatabase database;
  private MongoCollection<Document> collection;
  private final Collation collation;
  private final UpdateOptions updateOptions;
  private final ExecutorService executor;
  
  private final List<String> tables;
  
  public MongoDBDatabase(String mongoURL) {
    this.url = mongoURL;
    
    this.openConnection();
    this.executor = Executors.newCachedThreadPool();
    
    this.collation = Collation.builder().locale("en_US").collationStrength(CollationStrength.SECONDARY).build();
    this.updateOptions = new UpdateOptions().collation(this.collation);
    this.tables =
        DataTable.listTables().stream().map(DataTable::getInfo).map(DataTableInfo::name).filter(name -> !name.equalsIgnoreCase("aCoreProfile")).collect(Collectors.toList());
    
    if (!Manager.BUNGEE) {
      Object pluginManager = Accessors.getMethod(org.bukkit.Bukkit.class, "getPluginManager").invoke(null);
      MethodAccessor registerEvents = Accessors.getMethod(pluginManager.getClass(), "registerEvents", org.bukkit.event.Listener.class, org.bukkit.plugin.Plugin.class);
      registerEvents.invoke(pluginManager, new MongoDBConversor(), Core.getInstance());
    }
  }
  
  @Override
  public void convertDatabase(Object player) {
    if (!Manager.BUNGEE) {
      if (MongoDBConversor.CONVERT != null) {
        ((org.bukkit.entity.Player) player).sendMessage("§cUma conversão de Banco de Dados está em andamento.");
        return;
      }
      
      MongoDBConversor.CONVERT = new String[5];
      ((org.bukkit.entity.Player) player).sendMessage("§aIniciando conversão §8(MySQL -> MongoDB)");
      ((org.bukkit.entity.Player) player).sendMessage("§aInsira a Host do MySQL!");
      ((org.bukkit.entity.Player) player).sendMessage("§cVocê pode cancelar essa Operação ao digitar 'cancelar' (sem aspas).");
    }
  }
  
  @Override
  public void setupBoosters() {
    if (!Manager.BUNGEE) {
      MongoCollection<Document> collection = this.database.getCollection("aCoreNetworkBooster");
      for (String mg : Core.minigames) {
        if (collection.find(new BasicDBObject("_id", mg)).first() == null) {
          this.executor.execute(() -> collection.insertOne(new Document("_id", mg).append("booster", "d4rkk").append("multiplier", 1.0).append("expires", 0L)));
        }
      }
    }
  }
  
  @Override
  public void setBooster(String minigame, String booster, double multiplier, long expires) {
    this.executor.execute(() -> this.database.getCollection("aCoreNetworkBooster")
        .updateOne(Filters.eq("_id", minigame), new BasicDBObject("$set", new BasicDBObject("booster", booster).append("multiplier", multiplier).append("expires", expires))));
  }
  
  @Override
  public NetworkBooster getBooster(String minigame) {
    try {
      Document document = this.executor.submit(() -> this.database.getCollection("aCoreNetworkBooster").find(new BasicDBObject("_id", minigame)).first()).get();
      if (document != null) {
        String booster = document.getString("booster");
        double multiplier = document.getDouble("multiplier");
        long expires = document.getLong("expires");
        if (expires > System.currentTimeMillis()) {
          return new NetworkBooster(booster, multiplier, expires);
        }
      }
    } catch (Exception ignored) {
    }
    
    return null;
  }
  
  @Override
  public String getRankAndName(String player) {
    try {
      Document document = this.executor
          .submit(() -> this.collection.find(new BasicDBObject("_id", player.toLowerCase())).projection(fields(include("_id", "role"))).collation(this.collation).first()).get();
      if (document != null) {
        String result = document.getString("role") + " : " + document.getString("_id");
        RoleCache.setCache(player, document.getString("role"), document.getString("_id"));
        return result;
      }
    } catch (Exception ignored) {
    }
    return null;
  }
  
  @Override
  public boolean getPreference(String player, String id, boolean def) {
    boolean preference = true;
    try {
      Document document = this.executor
          .submit(() -> this.collection.find(new BasicDBObject("_id", player.toLowerCase())).projection(fields(include("preferences"))).collation(this.collation).first()).get();
      if (document != null) {
        preference = ((JSONObject) new JSONParser().parse(document.getString("preferences"))).get(id).equals(0L);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    
    return preference;
  }
  
  @Override
  public List<String[]> getLeaderBoard(String table, String... columns) {
    List<String[]> result = new ArrayList<>();
    String name = columns[0].equals("insanekills") ? "totalkills" : columns[0].equals("insanewins") ? "totalwins" : columns[0].equals("insanepoints") ? "totalpoints" : columns[0];
    try {
      MongoCursor<Document> cursor = this.executor
          .submit(() -> this.collection.find().projection(fields(include("_id", "role", table + "." + name))).sort(new BasicDBObject(table + "." + name, -1)).limit(10).cursor())
          .get();
      while (cursor.hasNext()) {
        Document document = cursor.next();
        Document subDocument = document.get(table, Document.class);
        long count = (subDocument == null || !subDocument.containsKey(name) ? 0L : Long.parseLong(subDocument.get(name).toString()));
        result
            .add(new String[]{StringUtils.getLastColor(Role.getRoleByName(document.getString("role")).getPrefix()) +
                document.getString("_id"), StringUtils.formatNumber(count)});
      }
      cursor.close();
    } catch (Exception ignore) {}
    
    return result;
  }
  
  @Override
  public void close() {
    this.executor.shutdownNow().forEach(Runnable::run);
    this.client.close();
  }
  
  public void openConnection() {
    this.client = MongoClients.create(this.url);
    this.database = this.client.getDatabase("aCore");
    this.collection = this.database.getCollection("Profile");
    LOGGER.info("Conected to MongoDB");
  }
  
  @Override
  public Map<String, Map<String, DataContainer>> load(String name) throws ProfileLoadException {
    Map<String, Map<String, DataContainer>> tableMap = new HashMap<>();
    
    List<String> includes = new ArrayList<>();
    for (DataTable table : DataTable.listTables()) {
      Map<String, DataContainer> containerMap = table.getDefaultValues();
      String prefix = table.getInfo().name().equalsIgnoreCase("aCoreprofile") ? "" : table.getInfo().name() + ".";
      containerMap.keySet().forEach(key -> includes.add(prefix + key));
      tableMap.put(table.getInfo().name(), containerMap);
    }
    
    Document document;
    try {
      document = this.executor.submit(() -> this.collection.find(new BasicDBObject("_id", name)).projection(fields(include(includes))).collation(this.collation).first()).get();
    } catch (InterruptedException | ExecutionException ex) {
      throw new ProfileLoadException(ex.getMessage());
    }
    
    if (document != null) {
      tableMap.values().forEach(map -> map.values().forEach(dc -> dc.setUpdated(true)));
      for (String key : document.keySet()) {
        if (key.equalsIgnoreCase("_id") || key.equalsIgnoreCase("totalkills") || key.equalsIgnoreCase("totalwins") || key.equalsIgnoreCase("totalpoints")) {
          continue;
        }
        
        if (this.tables.contains(key)) {
          Document subDocument = document.get(key, Document.class);
          subDocument.keySet().forEach(subKey -> tableMap.get(key).put(subKey, new DataContainer(subDocument.get(subKey))));
          continue;
        }
        
        tableMap.get("aCoreProfile").put(key, new DataContainer(document.get(key)));
      }
    } else {
      Document insert = new Document();
      insert.put("_id", name);
      for (Map.Entry<String, Map<String, DataContainer>> tables : tableMap.entrySet()) {
        if (this.tables.contains(tables.getKey())) {
          Document table = new Document();
          for (Map.Entry<String, DataContainer> containers : tables.getValue().entrySet()) {
            table.put(containers.getKey(), containers.getValue().get());
          }
          insert.put(tables.getKey(), table);
          continue;
        }
        
        for (Map.Entry<String, DataContainer> containers : tables.getValue().entrySet()) {
          insert.put(containers.getKey(), containers.getValue().get());
        }
      }
      this.executor.execute(() -> collection.insertOne(insert));
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
    final Document save = new Document();
    for (DataTable table : DataTable.listTables()) {
      Map<String, DataContainer> rows = tableMap.get(table.getInfo().name());
      if (rows.values().stream().noneMatch(DataContainer::isUpdated)) {
        continue;
      }
      
      String prefix = table.getInfo().name().equalsIgnoreCase("aCoreprofile") ? "" : table.getInfo().name() + ".";
      if (table.getInfo().name().contains("BlockSumo") || table.getInfo().name().contains("TheBridge") || table.getInfo().name().contains("BedWars")) {
        if (table.getInfo().name().contains("BlockSumo") || table.getInfo().name().contains("TheBridge")) {
          save.put(prefix + "totalkills", rows.get("skills").getAsLong() + rows.get("normalkills").getAsLong());
          save.put(prefix + "totalwins", rows.get("swins").getAsLong() + rows.get("normalwins").getAsLong());
        }
        if (table.getInfo().name().contains("TheBridge")) {
          save.put(prefix + "totalpoints", rows.get("insanepoints").getAsLong() + rows.get("insane2v2points").getAsLong());
        }
        if (table.getInfo().name().contains("BedWars")) {
          save.put(prefix + "totalkills", rows.get("insanekills").getAsLong() + rows.get("insane2v2kills").getAsLong() + rows.get("4v4kills").getAsLong());
          save.put(prefix + "totalwins", rows.get("insanewins").getAsLong() + rows.get("insane2v2wins").getAsLong() + rows.get("4v4wins").getAsLong());
        }
      }
      
      for (Map.Entry<String, DataContainer> entry : rows.entrySet()) {
        if (entry.getValue().isUpdated()) {
          entry.getValue().setUpdated(false);
          save.put(prefix + entry.getKey(), entry.getValue().get());
        }
      }
    }
    
    if (save.isEmpty()) {
      return;
    }
    
    if (async) {
      this.executor.execute(() -> this.collection.updateOne(Filters.eq("_id", name), new Document("$set", save), this.updateOptions));
    } else {
      this.collection.updateOne(Filters.eq("_id", name), new Document("$set", save), this.updateOptions);
    }
  }
  
  @Override
  public String exists(String name) {
    try {
      return Objects
          .requireNonNull(this.executor.submit(() -> this.collection.find(new BasicDBObject("_id", name)).projection(fields(include("_id"))).collation(collation)).get().first())
          .getString("_id");
    } catch (Exception ex) {
      return null;
    }
  }
  
  public MongoDatabase getDatabase() {
    return this.database;
  }
  
  public MongoCollection<Document> getCollection() {
    return this.collection;
  }
  
  public ExecutorService getExecutor() {
    return this.executor;
  }
}
