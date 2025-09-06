package me.joaomanoel.d4rkk.dev.player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.booster.Booster;
import me.joaomanoel.d4rkk.dev.booster.NetworkBooster;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.container.*;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.AbstractContainer;
import me.joaomanoel.d4rkk.dev.database.exception.ProfileLoadException;
import me.joaomanoel.d4rkk.dev.game.Game;
import me.joaomanoel.d4rkk.dev.game.GameTeam;
import me.joaomanoel.d4rkk.dev.hook.FriendsHook;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.player.enums.PlayerVisibility;
import me.joaomanoel.d4rkk.dev.player.hotbar.Hotbar;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.player.scoreboard.KScoreboard;
import me.joaomanoel.d4rkk.dev.titles.TitleManager;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Profile {

  private static final Map<String, UUID> UUID_CACHE = new HashMap<>();
  private static final Map<String, Profile> PROFILES = new ConcurrentHashMap<>();
  private static final SimpleDateFormat COMPARE_SDF = new SimpleDateFormat("yyyy/MM/dd");
  private String name;
  private Game<? extends GameTeam> game;
  private Hotbar hotbar;
  private KScoreboard scoreboard;
  private Map<String, Long> lastHit = new HashMap<>();
  private Map<String, Map<String, DataContainer>> tableMap;
  private Player player;

  private Object value;

  public Profile(String name) throws ProfileLoadException {
    this.name = name;
    this.tableMap = Database.getInstance().load(name);
    this.getDataContainer("aCoreProfile", "lastlogin").set(System.currentTimeMillis());
  }

  public static Profile createOrLoadProfile(String playerName) throws ProfileLoadException {
    Profile profile = PROFILES.getOrDefault(playerName.toLowerCase(), null);
    if (profile == null) {
      profile = new Profile(playerName);
      PROFILES.put(playerName.toLowerCase(), profile);
    }

    return profile;
  }

  public static Profile loadIfExists(String playerName) throws ProfileLoadException {
    Profile profile = PROFILES.getOrDefault(playerName.toLowerCase(), null);
    if (profile == null) {
      playerName = Database.getInstance().exists(playerName);
      if (playerName != null) {
        profile = new Profile(playerName);
      }
    }

    return profile;
  }

  public static Profile getProfile(String playerName) {
    return PROFILES.get(playerName.toLowerCase());
  }

  public static Profile unloadProfile(String playerName) {
    UUID_CACHE.remove(playerName.toLowerCase());
    return PROFILES.remove(playerName.toLowerCase());
  }

  public static Player findCached(String playerName) {
    UUID uuid = UUID_CACHE.get(playerName.toLowerCase());
    return uuid == null ? null : Bukkit.getPlayer(uuid);
  }

  public static boolean isOnline(String playerName) {
    return PROFILES.containsKey(playerName.toLowerCase());
  }

  public static Collection<Profile> listProfiles() {
    return PROFILES.values();
  }

  public static Profile loadIfExistsInDatabase(String playerName) throws ProfileLoadException {
    String storedName = Database.getInstance().exists(playerName);
    if (storedName != null) {
      Profile profile = new Profile(storedName);
      PROFILES.put(playerName.toLowerCase(), profile);
      return profile;
    }
    return null;
  }


  public void setHit(String name) {
    this.lastHit.put(name, System.currentTimeMillis() + 8000);
  }

  public void update() {
    try {
      if (this.scoreboard != null) {
        this.scoreboard.update();
      } else {
        System.out.println("Scoreboard não inicializado para o jogador: " + this.name);
      }
    } catch (Exception e) {
      System.err.println("Erro ao atualizar o scoreboard para o jogador: " + this.name);
      e.printStackTrace();
    }
  }

  public void refresh() {
    Player player = this.getPlayer();
    if (player == null) {
      return;
    }

    player.setMaxHealth(20.0);
    player.setHealth(20.0);
    player.setFoodLevel(20);
    player.setExhaustion(0.0f);
    player.setExp(0.0f);
    player.setLevel(0);
    player.setAllowFlight(false);
    player.closeInventory();
    for (PotionEffect pe : player.getActivePotionEffects()) {
      player.removePotionEffect(pe.getType());
    }

    if (!playingGame()) {
      player.setGameMode(GameMode.ADVENTURE);
      player.teleport(Core.getLobby());

      player.setAllowFlight(player.hasPermission("aCore.fly"));
      this.getDataContainer("aCoreProfile", "role").set(StringUtils.stripColors(Role.getPlayerRole(player, true).getName()));
    }

    if (this.hotbar != null) {
      this.hotbar.apply(this);
    }

    this.refreshPlayers();
  }

  public void refreshpit() {
    Player player = this.getPlayer();
    if (player == null) {
      return;
    }

    player.setMaxHealth(20.0);
    player.setHealth(20.0);
    player.setFoodLevel(20);
    player.setExhaustion(0.0f);
    player.setExp(0.0f);
    player.setLevel(0);
    player.setAllowFlight(false); // Desativa o voo
    player.setFlying(false); // Garante que o jogador não esteja voando
    player.closeInventory();

    // Remove os efeitos de poção ativos
    for (PotionEffect pe : player.getActivePotionEffects()) {
      player.removePotionEffect(pe.getType());
    }

    if (!playingGame()) {
      player.setGameMode(GameMode.ADVENTURE);
      player.teleport(Core.getLobby());

      this.getDataContainer("aCoreProfile", "role").set(StringUtils.stripColors(Role.getPlayerRole(player, true).getName()));
    }

    if (this.hotbar != null) {
      this.hotbar.apply(this);
    }
    this.refreshPlayers();
  }


  public void refreshPlayers() {
    Player player = this.getPlayer();
    if (player == null) {
      return;
    }

    if (this.hotbar != null) {
      this.hotbar.getButtons().forEach(button -> {
        if (button.getAction().getValue().equalsIgnoreCase("jogadores")) {
          player.getInventory().setItem(button.getSlot(), BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(player, button.getIcon())));
        }
      });
    }

    if (!this.playingGame()) {
      for (Player players : Bukkit.getOnlinePlayers()) {
        Profile profile = Profile.getProfile(players.getName());
        if (profile != null) {
          if (!profile.playingGame()) {
            boolean friend = FriendsHook.isFriend(player.getName(), players.getName());
            if ((this.getPreferencesContainer().getPlayerVisibility() == PlayerVisibility.TODOS || Role.getPlayerRole(players).isAlwaysVisible() || friend) && !FriendsHook
                    .isBlacklisted(player.getName(), players.getName())) {
              if (!player.canSee(players)) {
                TitleManager.show(this, profile);
              }
              player.showPlayer(players);
            } else {
              if (player.canSee(players)) {
                TitleManager.hide(this, profile);
              }
              player.hidePlayer(players);
            }

            if ((profile.getPreferencesContainer().getPlayerVisibility() == PlayerVisibility.TODOS || Role.getPlayerRole(player).isAlwaysVisible() || friend) && !FriendsHook
                    .isBlacklisted(players.getName(), player.getName())) {
              if (!players.canSee(player)) {
                TitleManager.show(profile, this);
              }
              players.showPlayer(player);
            } else {
              if (players.canSee(player)) {
                TitleManager.hide(profile, this);
              }
              players.hidePlayer(player);
            }
          } else {
            player.hidePlayer(players);
            players.hidePlayer(player);
          }
        }
      }
    }
  }

  public void save() {
    if (this.name == null || this.tableMap == null) {
      return;
    }

    Database.getInstance().save(this.name, this.tableMap);
  }

  public void saveSync() {
    if (this.name == null || this.tableMap == null) {
      return;
    }

    Database.getInstance().saveSync(this.name, this.tableMap);
  }

  public void destroy() {
    this.name = null;
    this.game = null;
    this.hotbar = null;
    this.scoreboard = null;
    this.lastHit.clear();
    this.lastHit = null;
    this.tableMap.values().forEach(containerMap -> {
      containerMap.values().forEach(DataContainer::gc);
      containerMap.clear();
    });
    this.tableMap.clear();
    this.tableMap = null;
  }

  public String getName() {
    return this.name;
  }

  public boolean isOnline() {
    return this.name != null && isOnline(this.name);
  }

  public Player getPlayer() {
    if (this.player == null) {
      this.player = this.name == null ? null : Bukkit.getPlayerExact(this.name);
    }

    return this.player;
  }

  public void setPlayer(Player player) {
    this.player = player;
    UUID_CACHE.put(this.name.toLowerCase(), player.getUniqueId());
  }

  public Game<?> getGame() {
    return this.getGame(Game.class);
  }

  public void setGame(Game<? extends GameTeam> game) {
    this.game = game;
    this.lastHit.clear();
    if (this.game != null) {
      TitleManager.leaveLobby(this);
    } else {
      Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), () -> {
        if (this.isOnline() && !this.playingGame()) {
          TitleManager.joinLobby(this);
        }
      }, 5);
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends Game<?>> T getGame(Class<T> gameClass) {
    return this.game != null && gameClass.isAssignableFrom(this.game.getClass()) ? (T) this.game : null;
  }

  public Hotbar getHotbar() {
    return this.hotbar;
  }

  public void setHotbar(Hotbar hotbar) {
    this.hotbar = hotbar;
  }

  public boolean playingGame() {
    return this.game != null;
  }

  public List<Profile> getLastHitters() {
    List<Profile> hitters = this.lastHit.entrySet().stream()
            .filter(entry -> entry.getValue() > System.currentTimeMillis() && isOnline(entry.getKey()))
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .map(entry -> getProfile(entry.getKey()))
            .collect(Collectors.toList());
    // limpar após uso
    this.lastHit.clear();
    return hitters;
  }

  public KScoreboard getScoreboard() {
    return this.scoreboard;
  }

  public void setScoreboard(KScoreboard scoreboard) {
    if (this.scoreboard != null) {
      this.scoreboard.destroy();
    }
    this.scoreboard = scoreboard;
  }

  public void addStats(String table, String... keys) {
    this.addStats(table, 1, keys);
  }

  public void addStats(String table, long amount, String... keys) {
    for (String key : keys) {
      if (!table.toLowerCase().contains("murder")) {
        if (key.startsWith("monthly")) {
          String month = this.getDataContainer(table, "month").getAsString();
          String current = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
          if (!month.equals(current)) {
            Map<String, DataContainer> containerMap = this.tableMap.get(table);
            containerMap.keySet().forEach(k -> {
              if (k.startsWith("monthly")) {
                containerMap.get(k).set(0L);
              }
            });
            containerMap.get("month").set(current);
          }
        }
      }

      this.getDataContainer(table, key).addLong(amount);
    }
  }

  public void setStats(String table, long amount, String... keys) {
    for (String key : keys) {
      this.getDataContainer(table, key).set(amount);
    }
  }

  public void updateDailyStats(String table, String date, long amount, String key) {
    long currentExpire = this.getStats(table, date);
    this.setStats(table, System.currentTimeMillis(), date);
    if (amount == 0 || (this.getStats(table, key) > 0 && !COMPARE_SDF.format(System.currentTimeMillis()).equals(COMPARE_SDF.format(currentExpire)))) {
      this.setStats(table, 0, key);
      return;
    }

    this.addStats(table, amount, key);
  }

  public void addCoins(String table, double amount) {
    if (Core.minigame.equals("The Pit") || Core.minigame.equals("The Bridge") || Core.minigame.equals("Bed Wars") || Core.minigame.equals("Block Sumo") || Core.minigame.equals("Duels")) {
      this.getDataContainer(table, "coins").addDouble(amount);
      return;
    }
    double limit = this.getLimitedCoins(table);
    double currentCoins = this.getCoins(table);
    double newTotal = currentCoins + amount;

    if (newTotal > limit) {
      amount = limit - currentCoins;
      if (amount < 0) amount = 0;
    }

    // Atualiza a quantidade de moedas
    this.getDataContainer(table, "coins").addDouble(amount);
  }

  // Com multiplicador
  public int addCoinsWM(String table, double amount) {
    amount = this.calculateWM(amount);
    this.addCoins(table, amount);
    return (int) amount;
  }

  public double calculateWM(double amount) {
    double add = 0.0D;
    String booster = this.getBoostersContainer().getEnabled();
    if (booster != null) {
      add = amount * Double.parseDouble(booster.split(":")[0]);
    }

    NetworkBooster nb = Booster.getNetworkBooster(Core.minigame);
    if (nb != null) {
      add += amount * nb.getMultiplier();
    }

    return (amount > 0.0 && add == 0.0) ? amount : add;
  }

  public void removeCoins(String table, double amount) {
    this.getDataContainer(table, "coins").removeDouble(amount);
  }

  public long getStats(String table, String... keys) {
    long stat = 0;
    for (String key : keys) {
      stat += this.getDataContainer(table, key).getAsLong();
    }

    return stat;
  }

  public void set(Object value) {
    this.value = value;
  }
  public void addString(String newValue) {
    if (this.value == null || this.value.toString().isEmpty()) {
      this.value = newValue;
    } else {
      this.value = this.value.toString() + "," + newValue;
    }
  }

  public String getAsString() {
    return value != null ? value.toString() : "";
  }

  public long getAsLong() {
    return value != null ? Long.parseLong(value.toString()) : 0;
  }

  public double getAsDouble() {
    return value != null ? Double.parseDouble(value.toString()) : 0.0;
  }

  // Resetar diariamente baseado em um Timemillis.
  public long getDailyStats(String table, String date, String key) {
    long currentExpire = this.getStats(table, date);
    if (!COMPARE_SDF.format(System.currentTimeMillis()).equals(COMPARE_SDF.format(currentExpire))) {
      this.setStats(table, 0, key);
    }

    this.setStats(table, System.currentTimeMillis(), date);
    return this.getStats(table, key);
  }

  public double getCoins(String table) {
    return this.getDataContainer(table, "coins").getAsDouble();
  }
  public double getLimitedCoins(String table) {
    return this.getDataContainer(table, "limitedcoins").getAsDouble();
  }

  public String getFormatedStats(String table, String... keys) {
    return StringUtils.formatNumber(this.getStats(table, keys));
  }

  public String getFormatedStatsDouble(String table, String key) {
    return StringUtils.formatNumber(this.getDataContainer(table, key).getAsDouble());
  }

  public DeliveriesContainer getDeliveriesContainer() {
    return this.getAbstractContainer("aCoreProfile", "deliveries", DeliveriesContainer.class);
  }

  public PreferencesContainer getPreferencesContainer() {
    return this.getAbstractContainer("aCoreProfile", "preferences", PreferencesContainer.class);
  }

  public TitlesContainer getTitlesContainer() {
    return this.getAbstractContainer("aCoreProfile", "titles", TitlesContainer.class);
  }

  public BoostersContainer getBoostersContainer() {
    return this.getAbstractContainer("aCoreProfile", "boosters", BoostersContainer.class);
  }

  public AchievementsContainer getAchievementsContainer() {
    return this.getAbstractContainer("aCoreProfile", "achievements", AchievementsContainer.class);
  }

  public SelectedContainer getSelectedContainer() {
    return this.getAbstractContainer("aCoreProfile", "selected", SelectedContainer.class);
  }

  public LanguageContainer getLanguageContainer() {
    return this.getAbstractContainer("aCoreProfile", "language", LanguageContainer.class);
  }

  public DataContainer getDataContainer(String table, String key) {
    if (tableMap == null) {
      tableMap = new HashMap<>();
    }
    return this.tableMap.get(table).get(key);
  }

  public <T extends AbstractContainer> T getAbstractContainer(String table, String key, Class<T> containerClass) {
    return this.getDataContainer(table, key).getContainer(containerClass);
  }

  public Map<String, Map<String, DataContainer>> getTableMap() {
    return this.tableMap;
  }
}