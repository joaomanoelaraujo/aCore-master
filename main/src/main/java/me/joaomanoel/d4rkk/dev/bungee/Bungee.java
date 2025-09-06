package me.joaomanoel.d4rkk.dev.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.joaomanoel.d4rkk.dev.bungee.cmd.Commands;
import me.joaomanoel.d4rkk.dev.bungee.listener.Listeners;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;

public class Bungee extends Plugin {
  
  public static final String STEVE =
      "eyJ0aW1lc3RhbXAiOjE1ODcxNTAzMTc3MjAsInByb2ZpbGVJZCI6IjRkNzA0ODZmNTA5MjRkMzM4NmJiZmM5YzEyYmFiNGFlIiwicHJvZmlsZU5hbWUiOiJzaXJGYWJpb3pzY2hlIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYTRhZjcxODQ1NWQ0YWFiNTI4ZTdhNjFmODZmYTI1ZTZhMzY5ZDE3NjhkY2IxM2Y3ZGYzMTlhNzEzZWI4MTBiIn19fQ==:syZ2Mt1vQeEjh/t8RGbv810mcfTrhQvnwEV7iLCd+5udVeroTa5NjoUehgswacTML3k/KxHZHaq4o6LmACHwsj/ivstW4PWc2RmVn+CcOoDKI3ytEm70LvGz0wAaTVKkrXHSw/RbEX/b7g7oQ8F67rzpiZ1+Z3TKaxbgZ9vgBQZQdwRJjVML2keI0669a9a1lWq3V/VIKFZc1rMJGzETMB2QL7JVTpQFOH/zXJGA+hJS5bRol+JG3LZTX93+DililM1e8KEjKDS496DYhMAr6AfTUfirLAN1Jv+WW70DzIpeKKXWR5ZeI+9qf48+IvjG8DhRBVFwwKP34DADbLhuebrolF/UyBIB9sABmozYdfit9uIywWW9+KYgpl2EtFXHG7CltIcNkbBbOdZy0Qzq62Tx6z/EK2acKn4oscFMqrobtioh5cA/BCRb9V4wh0fy5qx6DYHyRBdzLcQUfb6DkDx1uyNJ7R5mO44b79pSo8gdd9VvMryn/+KaJu2UvyCrMVUtOOzoIh4nCMc9wXOFW3jZ7ZTo4J6c28ouL98rVQSAImEd/P017uGvWIT+hgkdXnacVG895Y6ilXqJToyvf1JUQb4dgry0WTv6UTAjNgrm5a8mZx9OryLuI2obas97LCon1rydcNXnBtjUk0TUzdrvIa5zNstYZPchUb+FSnU=";
  public static final String ALEX =
      "eyJ0aW1lc3RhbXAiOjE1ODcxMzkyMDU4MzUsInByb2ZpbGVJZCI6Ijc1MTQ0NDgxOTFlNjQ1NDY4Yzk3MzlhNmUzOTU3YmViIiwicHJvZmlsZU5hbWUiOiJUaGFua3NNb2phbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNiNjBhMWY2ZDU2MmY1MmFhZWJiZjE0MzRmMWRlMTQ3OTMzYTNhZmZlMGU3NjRmYTQ5ZWEwNTc1MzY2MjNjZDMiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==:W60UUuAYlWfLFt5Ay3Lvd/CGUbKuuU8+HTtN/cZLhc0BC22XNgbY1btTite7ZtBUGiZyFOhYqQi+LxVWrdjKEAdHCSYWpCRMFhB1m0zEfu78yg4XMcFmd1v7y9ZfS45b3pLAJ463YyjDaT64kkeUkP6BUmgsTA2iIWvM33k6Tj3OAM39kypFSuH+UEpkx603XtxratD+pBjUCUvWyj2DMxwnwclP/uACyh0ZVrI7rC5xJn4jSura+5J2/j6Z/I7lMBBGLESt7+pGn/3/kArDE/1RShOvm5eYKqrTMRfK4n3yd1U1DRsMzxkU2AdlCrv1swT4o+Cq8zMI97CF/xyqk8z2L98HKlzLjtvXIE6ogljyHc9YsfU9XhHwZ7SKXRNkmHswOgYIQCSa1RdLHtlVjN9UdUyUoQIIO2AWPzdKseKJJhXwqKJ7lzfAtStErRzDjmjr7ld/5tFd3TTQZ8yiq3D6aRLRUnOMTr7kFOycPOPhOeZQlTjJ6SH3PWFsdtMMQsGzb2vSukkXvJXFVUM0TcwRZlqT5MFHyKBBPprIt0wVN6MmSKc8m5kdk7ZBU2ICDs/9Cd/fyzAIRDu3Kzm7egbAVK9zc1kXwGzowUkGGy1XvZxyRS5jF1zu6KzVgaXOGcrOLH4z/OHzxvbyW22/UwahWGN7MD4j37iJ7gjZDrk=";
  private static Bungee instance;
  private static final Map<String, String> fakeNames = new HashMap<>();
  private static final Map<String, Role> fakeRoles = new HashMap<>();
  private static final Map<String, String> fakeSkins = new HashMap<>();
  private static List<String> randoms;
  private Configuration config;
  private Configuration utils;
  private Configuration roles;
  
  public Bungee() {
    instance = this;
  }
  
  public static Bungee getInstance() {
    return instance;
  }

  public static void sendRole(ProxiedPlayer player, String sound) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("FAKE_BOOK");
    out.writeUTF(player.getName());
    if (sound != null) {
      out.writeUTF(sound);
    }
    player.getServer().sendData("acore:main", out.toByteArray());
  }

  public static void sendSkin(ProxiedPlayer player, String roleName, String sound) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("FAKE_BOOK2");
    out.writeUTF(player.getName());
    out.writeUTF(roleName);
    out.writeUTF(sound);
    player.getServer().sendData("acore:main", out.toByteArray());
  }

  public static void applyFake(ProxiedPlayer player, String fakeName, String role, String skin) {
    player.disconnect(TextComponent.fromLegacyText(StringUtils.formatColors(getInstance().getConfig().getString("fake.kick-apply")).replace("\\n", "\n")));
    fakeNames.put(player.getName(), fakeName);
    fakeRoles.put(player.getName(), Role.getRoleByName(role));
    fakeSkins.put(player.getName(), skin);
  }

  public static void removeFake(ProxiedPlayer player) {
    player.disconnect(TextComponent.fromLegacyText(StringUtils.formatColors(getInstance().getConfig().getString("fake.kick-remove")).replace("\\n", "\n")));
    fakeNames.remove(player.getName());
    fakeRoles.remove(player.getName());
    fakeSkins.remove(player.getName());
  }
  public static ProxiedPlayer getPlayerByName(String playerName) {
    return Bungee.getInstance().getProxy().getPlayer(playerName);
  }

  public static boolean isBungeeActive() {
    return getInstance().getProxy() != null && !getInstance().getProxy().getPlayers().isEmpty();
  }

  public static String getCurrent(String playerName) {
    return isFake(playerName) ? getFake(playerName) : playerName;
  }
  
  public static String getFake(String playerName) {
    return fakeNames.get(playerName);
  }
  
  public static Role getRole(String playerName) {
    return fakeRoles.getOrDefault(playerName, Role.getLastRole());
  }
  
  public static String getSkin(String playerName) {
    return fakeSkins.getOrDefault(playerName, STEVE);
  }

  public static String getServerName(ProxiedPlayer player) {
    if (player != null) {
      return player.getServer().getInfo().getName();
    }
    return null;
  }

  public static boolean isFake(String playerName) {
    return fakeNames.containsKey(playerName);
  }
  
  public static boolean isUsable(String name) {
    return !fakeNames.containsKey(name) && !fakeNames.containsValue(name) && getInstance().getProxy().getPlayer(name) == null;
  }
  
  public static List<String> listNicked() {
    return new ArrayList<>(fakeNames.keySet());
  }
  
  public static List<String> getRandomNicks() {
    if (randoms == null) {
      randoms = getInstance().getConfig().getStringList("fake.randoms");
    }
    
    return randoms;
  }
  
  public static boolean isFakeRole(String roleName) {
    return getInstance().getConfig().getStringList("fake.role").stream().anyMatch(role -> role.equalsIgnoreCase(roleName));
  }
  
  /**
   * Copia um arquivo a partir de um InputStream.
   *
   * @param input O input para ser copiado.
   * @param out   O arquivo destinario.
   */
  public static void copyFile(InputStream input, File out) {
    FileOutputStream ou = null;
    try {
      ou = new FileOutputStream(out);
      byte[] buff = new byte[1024];
      int len;
      while ((len = input.read(buff)) > 0) {
        ou.write(buff, 0, len);
      }
    } catch (IOException ex) {
      getInstance().getLogger().log(Level.WARNING, "Failed at copy file " + out.getName() + "!", ex);
    } finally {
      try {
        if (ou != null) {
          ou.close();
        }
        if (input != null) {
          input.close();
        }
      } catch (IOException ignore) {
      }
    }
  }
  
  @Override
  public void onEnable() {
    saveDefaultConfig();
    
    Database.setupDatabase(
        config.getString("database.type"),
        config.getString("database.mysql.host"),
        config.getString("database.mysql.port"),
        config.getString("database.mysql.name"),
        config.getString("database.mysql.user"),
        config.getString("database.mysql.pass"),
        config.getBoolean("database.mysql.hikari", false),
        config.getBoolean("database.mysql.mariadb", false),
        config.getString("database.mongodb.url", "")
    );
    
    setupRoles();
    LanguageBungee.setupLanguage();
    Commands.setupCommands();
    getProxy().getPluginManager().registerListener(this, new Listeners());
    
    getProxy().registerChannel("acore:main");
    
    this.getLogger().info("The plugin has been activated.");
  }
  
  @Override
  public void onDisable() {
    this.getLogger().info("The plugin has been deactivated..");
  }
  
  public void saveDefaultConfig() {
    for (String fileName : new String[]{"config", "roles", "utils"}) {
      File file = new File("plugins/aCore/" + fileName + ".yml");
      if (!file.exists()) {
        file.getParentFile().mkdirs();
        copyFile(Bungee.getInstance().getResourceAsStream(fileName + ".yml"), file);
      }
      
      try {
        if (fileName.equals("config")) {
          this.config = YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } else if (fileName.equals("utils")) {
          this.utils = YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } else {
          this.roles = YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        }
      } catch (IOException ex) {
        this.getLogger().log(Level.WARNING, "Cannot load " + fileName + ".yml: ", ex);
      }
    }
  }

  public Configuration getConfig() {
    return utils;
  }
  
  private void setupRoles() {
    try {
      if (utils.get("fake.role") instanceof String) {
        utils.set("fake.role", Arrays.asList(utils.getString("fake.role")));
        YamlConfiguration.getProvider(YamlConfiguration.class).save(utils, new File("plugins/aCore/utils.yml"));
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    
    for (String key : roles.getSection("roles").getKeys()) {
      String name = roles.getString("roles." + key + ".name");
      String prefix = roles.getString("roles." + key + ".prefix");
      String permission = roles.getString("roles." + key + ".permission");
      boolean broadcast = roles.getBoolean("roles." + key + ".broadcast", true);
      boolean alwaysVisible = roles.getBoolean("roles." + key + ".alwaysvisible", false);
      
      Role.listRoles().add(new Role(name, prefix, permission, alwaysVisible, broadcast));
    }
    
    if (Role.listRoles().isEmpty()) {
      Role.listRoles().add(new Role("&7Member", "&7", "", false, false));
    }
  }
}
