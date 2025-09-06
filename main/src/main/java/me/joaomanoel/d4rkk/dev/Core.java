package me.joaomanoel.d4rkk.dev;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.joaomanoel.d4rkk.dev.achievements.Achievement;
import me.joaomanoel.d4rkk.dev.booster.Booster;
import me.joaomanoel.d4rkk.dev.bungee.PluginMessageListenerExample;
import me.joaomanoel.d4rkk.dev.cmd.Commands;
import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.tables.extension.SkyWarsPlaceholder;
import me.joaomanoel.d4rkk.dev.deliveries.Delivery;
import me.joaomanoel.d4rkk.dev.game.GameState;
import me.joaomanoel.d4rkk.dev.hook.aCoreExpansion;
import me.joaomanoel.d4rkk.dev.hook.protocollib.EntityAdapter;
import me.joaomanoel.d4rkk.dev.hook.protocollib.FakeAdapter;
import me.joaomanoel.d4rkk.dev.hook.protocollib.NPCAdapter;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.libraries.npc.NPCLibrary;
import me.joaomanoel.d4rkk.dev.listeners.Listeners;
import me.joaomanoel.d4rkk.dev.listeners.PluginMessageListener;
import me.joaomanoel.d4rkk.dev.nms.NMSManager;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.fake.FakeManager;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.KPlugin;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.servers.ServerItem;
import me.joaomanoel.d4rkk.dev.titles.TitleLoader;
import me.joaomanoel.d4rkk.dev.utils.LanguageIcons;
import me.joaomanoel.d4rkk.dev.utils.queue.Queue;
import me.joaomanoel.d4rkk.dev.utils.queue.QueuePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;

@SuppressWarnings("unchecked")
public class Core extends KPlugin {
  
  public static final List<String> warnings = new ArrayList<>();
  public static final List<String> minigames = Arrays.asList("Block Sumo", "Sky Wars", "Bed Wars", "The Bridge", "The Pit", "Duels");

  public static boolean validInit;
  public static boolean aFriends;

  public static String minigame = "";

  private static Core instance;
  private static Location lobby;

  public static Location getLobby() {
    return lobby;
  }
  
  public static void setLobby(Location location) {
    lobby = location;
  }
  
  public static Core getInstance() {
    return instance;
  }
  
  public static void sendServer(Profile profile, String name) {
    if (!Core.getInstance().isEnabled()) {
      return;
    }
    
    Player player = profile.getPlayer();
    if (Core.getInstance().getConfig("utils").getBoolean("queue")) {
      if (player != null) {
        player.closeInventory();
        Queue queue = player.hasPermission("aCore.queue") ? Queue.VIP : Queue.MEMBER;
        QueuePlayer qp = queue.getQueuePlayer(player);
        if (qp != null) {
          if (qp.server.equalsIgnoreCase(name)) {
            qp.player.sendMessage(LanguageAPI.getConfig(profile).getString("already"));
          } else {
            qp.server = name;
          }
          return;
        } //teste
        
        queue.queue(player, profile, name);
      }
    } else {
      if (player != null) {
        Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
          if (player.isOnline()) {
            player.closeInventory();
            NMSManager.sendActionBar("", player);
            player.sendMessage(LanguageAPI.getConfig(profile).getString("connecting.message"));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(name);
            player.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
          }
        });
      }
    }
  }

  @Override
  public void start() {
    instance = this;
  }
  
  @Override
  public void load() {
  }
  
  @Override
  public void enable() {
    NMSManager.setupNMS(this);
    saveDefaultConfig();
    //new aUpdater(this, 1).run();
    lobby = Bukkit.getWorlds().get(0).getSpawnLocation();

    // Remover o spawn-protection-size
    if (Bukkit.getSpawnRadius() != 0) {
      Bukkit.setSpawnRadius(0);
    }

    // Plugins que causaram incompatibilidades
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getResource("blacklist.txt"), StandardCharsets.UTF_8))) {
      String plugin;
      while ((plugin = reader.readLine()) != null) {
        if (Bukkit.getPluginManager().getPlugin(plugin.split(" ")[0]) != null) {
          warnings.add(" - " + plugin);
        }
      }
    } catch (IOException ex) {
      getLogger().log(Level.SEVERE, "Cannot load blacklist.txt: ", ex);
    }

    if (!warnings.isEmpty()) {
      CommandSender sender = Bukkit.getConsoleSender();
      StringBuilder sb = new StringBuilder(" \n §6§lIMPORTANT NOTICE\n \n §7It seems you are using plugins that conflict with aCore.\n §7You will not be able to start the server with the following plugins:");
      for (String warning : warnings) {
        sb.append("\n§f").append(warning);
      }
      sb.append("\n ");
      sender.sendMessage(sb.toString());
      System.exit(0);
      return;
    }


    if (MinecraftVersion.getCurrentVersion().getVersion().equals("1.8.8")) {
      try {
        SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
        Field field = simpleCommandMap.getClass().getDeclaredField("knownCommands");
        field.setAccessible(true);
        Map<String, Command> knownCommands = (Map<String, Command>) field.get(simpleCommandMap);
        knownCommands.remove("rl");
        knownCommands.remove("reload");
        knownCommands.remove("bukkit:rl");
        knownCommands.remove("bukkit:reload");
      } catch (ReflectiveOperationException ex) {
        getLogger().log(Level.SEVERE, "Cannot remove reload command: ", ex);
      }
    }

    if (!PlaceholderAPIPlugin.getInstance().getDescription().getVersion().equals("2.10.5")) {
      Bukkit.getConsoleSender().sendMessage(" \n §6§lIMPORTANT NOTICE\n \n §7Please use version 2.10.5 of PlaceHolderAPI. You are currently using v" + PlaceholderAPIPlugin.getInstance().getDescription().getVersion() + "\n ");
      System.exit(0);
      return;
    }
    PlaceholderAPI.registerExpansion(new aCoreExpansion());
    new SkyWarsPlaceholder().register();


    try {
      LanguageAPI.setupLanguages("EN_US", "PT_BR", "ES_ES", "JA_JP", "KO_KR", "ZH_CN");
    } catch (IOException ex) {
      getLogger().severe("Ocorreu um erro ao carregar as linguagens padrão.");
      throw new RuntimeException(ex);
    }
    Language.setupLanguage();
    GameState.loadLanguage(getConfig());
    aFriends = Bukkit.getPluginManager().getPlugin("aFriends") != null;
    Database.setupDatabase(
            getConfig().getString("database.type"),
            getConfig().getString("database.mysql.host"),
            getConfig().getString("database.mysql.port"),
            getConfig().getString("database.mysql.name"),
            getConfig().getString("database.mysql.user"),
            getConfig().getString("database.mysql.pass"),
            getConfig().getBoolean("database.mysql.hikari", false),
            getConfig().getBoolean("database.mysql.mariadb", false),
            getConfig().getString("database.mongodb.url", "")
    );


    setupRoles();
    FakeManager.setupFake();
    TitleLoader.loadTitles();
    Achievements.setupAchaviments();
    Booster.setupBoosters();
    Delivery.setupDeliveries();
    ServerItem.setupServers();
    Achievement.setupAchievements();

    NPCLibrary.setupNPCManager();

    //Setup cosmetics
    Cosmetic.setupCosmetics();

    //carrega os ultimos logins
    loadLastLogins();

    Commands.setupCommands();
    Listeners.setupListeners();
    LanguageIcons.load(this);

    FakeAdapter.setup();
    ProtocolLibrary.getProtocolManager().addPacketListener(new NPCAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new EntityAdapter());

    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageListener());
    getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageListenerExample());
// Adicionado o incoming para BungeeCord
    getServer().getMessenger().registerOutgoingPluginChannel(this, "acore:main");
    getServer().getMessenger().registerIncomingPluginChannel(this, "acore:main", new PluginMessageListener());

    validInit = true;
    this.getLogger().info("The plugin has been activated.");
  }


  public Map<String, Long> ultimoLoginMap = new HashMap<>();
  private final File dataFile = new File(Core.getInstance().getDataFolder(), "last_logins.json");

  public void loadLastLogins() {
    if (dataFile.exists()) {
      try (Reader reader = new FileReader(dataFile)) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Long>>(){}.getType();
        ultimoLoginMap = gson.fromJson(reader, type);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void saveLastLogins() {
    try (Writer writer = new FileWriter(dataFile)) {
      Gson gson = new Gson();
      gson.toJson(ultimoLoginMap, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void disable() {
    if (validInit) {
      Bukkit.getOnlinePlayers().forEach(player -> {
        Profile profile = Profile.unloadProfile(player.getName());
        if (profile != null) {
          profile.saveSync();
          this.getLogger().info("Saved " + profile.getName());
          profile.destroy();
        }
      });

      Database.getInstance().close();
    }
    
    File update = new File("plugins/aCore/update", "aCore.jar");
    if (update.exists()) {
      try {
        this.getFileUtils().deleteFile(new File("plugins/aCore.jar"));
        this.getFileUtils().copyFile(new FileInputStream(update), new File("plugins/aCore.jar"));
        this.getFileUtils().deleteFile(update.getParentFile());
        this.getLogger().info("Update do aCore aplicada.");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    saveLastLogins();
    this.getLogger().info("The plugin has been deactivated..");
  }
  
  private void setupRoles() {
    KConfig config = getConfig("roles");
    for (String key : config.getSection("roles").getKeys(false)) {
      String name = config.getString("roles." + key + ".name");
      String prefix = config.getString("roles." + key + ".prefix");
      String permission = config.getString("roles." + key + ".permission");
      boolean broadcast = config.getBoolean("roles." + key + ".broadcast", true);
      boolean alwaysVisible = config.getBoolean("roles." + key + ".alwaysvisible", false);
      
      Role.listRoles().add(new Role(name, prefix, permission, alwaysVisible, broadcast));
    }
    
    if (Role.listRoles().isEmpty()) {
      Role.listRoles().add(new Role("&7Member", "&7", "", false, false));
    }
  }

}
