package me.joaomanoel.d4rkk.dev.listeners;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.cosmetic.CosmeticType;
import me.joaomanoel.d4rkk.dev.cosmetic.container.CosmeticsContainer;
import me.joaomanoel.d4rkk.dev.cosmetic.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.cosmetic.types.MvpColor;
import me.joaomanoel.d4rkk.dev.cosmetic.types.PunchMessage;
import me.joaomanoel.d4rkk.dev.database.exception.ProfileLoadException;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.libraries.npc.NPCLibrary;
import me.joaomanoel.d4rkk.dev.menus.others.MenuOtherProfile;
import me.joaomanoel.d4rkk.dev.nms.NMSManager;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.enums.ChatMention;
import me.joaomanoel.d4rkk.dev.player.enums.PrivateMessages;
import me.joaomanoel.d4rkk.dev.player.enums.ProtectionLobby;
import me.joaomanoel.d4rkk.dev.player.hotbar.HotbarButton;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.reflection.Accessors;
import me.joaomanoel.d4rkk.dev.reflection.acessors.FieldAccessor;
import me.joaomanoel.d4rkk.dev.titles.TitleManager;
import me.joaomanoel.d4rkk.dev.utils.PlayerIPUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.TagUtils;
import me.joaomanoel.d4rkk.dev.utils.aUpdater;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.WatchdogThread;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class Listeners implements Listener {

  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger()).getModule("Listeners");
  public static final Map<String, Long> DELAY_PLAYERS = new HashMap<>();
  private static final Map<UUID, Long> PROTECTION_LOBBY = new ConcurrentHashMap<>();
  private static final Map<UUID, Long> MESSAGE_COOLDOWNS = new ConcurrentHashMap<>();
  private final Set<UUID> firstTimePlayers = Collections.newSetFromMap(new ConcurrentHashMap<>());

  private static final FieldAccessor<Map> COMMAND_MAP = Accessors.getField(SimpleCommandMap.class, "knownCommands", Map.class);
  private static final SimpleCommandMap SIMPLE_COMMAND_MAP = (SimpleCommandMap) Accessors.getMethod(Bukkit.getServer().getClass(), "getCommandMap").invoke(Bukkit.getServer());
  private static final FieldAccessor<WatchdogThread> RESTART_WATCHDOG = Accessors.getField(WatchdogThread.class, "instance", WatchdogThread.class);
  private static final FieldAccessor<Boolean> RESTART_WATCHDOG_STOPPING = Accessors.getField(WatchdogThread.class, "stopping", boolean.class);

  public static void setupListeners() {
    Bukkit.getPluginManager().registerEvents(new Listeners(), Core.getInstance());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent evt) {
    if (evt.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
      return;
    }

    try {
      UUID playerId = evt.getUniqueId();
      String playerName = evt.getName();

      Profile profile = Profile.loadIfExistsInDatabase(playerName);
      if (profile == null) {
        Profile.createOrLoadProfile(playerName);
        firstTimePlayers.add(playerId);
        LOGGER.log(Level.INFO, "Created new profile for: " + playerName);
      } else {
        LOGGER.log(Level.INFO, "Loaded profile for: " + playerName);
      }
    } catch (ProfileLoadException ex) {
      LOGGER.log(Level.SEVERE, "Failed to load profile for: " + evt.getName(), ex);
      evt.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
              "§cFailed to load your profile. Please try again later.");
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent evt) {
    Player player = evt.getPlayer();
    UUID playerId = player.getUniqueId();
    if (player.isOp()) {
      sendUpdateNotification(player);
    }
//    if (firstTimePlayers.remove(playerId)) {
//      handleFirstTimePlayer(player);
//    }
  }

  //todo: fazer futuramente quando tiver diversidade de linguagens feitas.
  private void handleFirstTimePlayer(Player player) {
    EnumSound.LEVEL_UP.play(player, 1.0F, 2.0F);
    String country = PlayerIPUtils.getPlayerCountry(player);
    //String message = LanguageMessage.getLanguageMessage(player, country);
    // player.sendMessage(message);

  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerLoginMonitor(PlayerLoginEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());

    if (profile == null) {
      evt.disallow(PlayerLoginEvent.Result.KICK_OTHER,
              "§cYour profile couldn't be loaded.\n\n§cThis usually happens when the server isn't ready yet. Please try again.");
      return;
    }

    profile.setPlayer(player);
    handleMvpPlusCosmetics(profile);
  }

  private void handleMvpPlusCosmetics(Profile profile) {
    if (!profile.getPlayer().hasPermission("role.mvpplus")) {
      return;
    }

    Cosmetic.listByType(MvpColor.class).stream()
            .filter(cosmetic -> !profile.getAbstractContainer("aCoreProfile", "cosmetics", CosmeticsContainer.class).hasCosmetic(cosmetic))
            .forEach(cosmetic -> cosmetic.give(profile));

    Cosmetic.listByType(PunchMessage.class).stream()
            .filter(cosmetic -> !profile.getAbstractContainer("aCoreProfile", "cosmetics", CosmeticsContainer.class).hasCosmetic(cosmetic))
            .forEach(cosmetic -> cosmetic.give(profile));
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractAtPlayer(PlayerInteractAtEntityEvent evt) {
    if (!(evt.getRightClicked() instanceof Player)) {
      return;
    }

    Player clickedPlayer = (Player) evt.getRightClicked();
    if (NPCLibrary.isNPC(evt.getRightClicked())) {
      return;
    }

    Profile profile = Profile.getProfile(evt.getPlayer().getName());
    if (profile == null || Core.minigame.equals("The Pit")) {
      return;
    }
    if (!evt.getPlayer().getItemInHand().hasItemMeta()) {
      if (!profile.playingGame()) {
        new MenuOtherProfile(profile, clickedPlayer);
      }
    }
  }

  @EventHandler
  public void onPlayerHit(EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
      return;
    }

    if (Core.minigame.equals("The Pit")) {
      return;
    }

    Player victim = (Player) event.getEntity();
    Player attacker = (Player) event.getDamager();

    Profile attackerProfile = Profile.getProfile(attacker.getName());
    Profile victimProfile = Profile.getProfile(victim.getName());

    if (attackerProfile == null || victimProfile == null) {
      return;
    }

    if (attackerProfile.playingGame() || !attacker.getWorld().getName().equalsIgnoreCase(Core.getLobby().getWorld().getName())) {
      return;
    }

    handlePunchMessage(attacker, victim, attackerProfile);
  }

  private void handlePunchMessage(Player attacker, Player victim, Profile attackerProfile) {
    PunchMessage punchMessage = attackerProfile.getAbstractContainer("aCoreProfile", "cselected", SelectedContainer.class)
            .getSelected(CosmeticType.PUNCH, PunchMessage.class);

    if (punchMessage == null) {
      return;
    }

    UUID attackerId = attacker.getUniqueId();
    long currentTime = System.currentTimeMillis();
    long lastMessageTime = MESSAGE_COOLDOWNS.getOrDefault(attackerId, 0L);
    long timeLeft = 20000 - (currentTime - lastMessageTime);

    if (timeLeft > 0) {
      attacker.sendMessage(LanguageAPI.getConfig(Profile.getProfile(attacker.getName())).getString("waiting$timer").replace("{more}", StringUtils.formatNumber(timeLeft / 1000)));
      return;
    }

    MESSAGE_COOLDOWNS.put(attackerId, currentTime);
    String message = punchMessage.getRandomMessage()
            .replace("{punched}", Role.getColored(victim.getName()))
            .replace("{player}", Role.getColored(attacker.getName()));

    attacker.getWorld().getPlayers().forEach(worldPlayer -> worldPlayer.sendMessage(message));
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent evt) {
    Player player = evt.getPlayer();
    UUID playerId = player.getUniqueId();
    String playerName = player.getName();

    Profile profile = Profile.unloadProfile(playerName);
    if (profile == null) {
      return;
    }

    handleProfileCleanup(profile);
    cleanupPlayerData(playerId, playerName);
    TagUtils.destroy(player);
  }

  private void handleProfileCleanup(Profile profile) {
    if (profile.getGame() != null) {
      profile.getGame().leave(profile, profile.getGame());
    }

    TitleManager.leaveServer(profile);

    if (RESTART_WATCHDOG_STOPPING.get(RESTART_WATCHDOG.get(null))) {
      profile.saveSync();
    } else {
      profile.save();
    }

    profile.destroy();
  }

  private void cleanupPlayerData(UUID playerId, String playerName) {
/*    FakeManager.fakeNames.remove(playerName);
    FakeManager.fakeRoles.remove(playerName);
    FakeManager.fakeSkins.remove(playerName);*/
    DELAY_PLAYERS.remove(playerId);
    PROTECTION_LOBBY.remove(playerId);
    MESSAGE_COOLDOWNS.remove(playerId);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent evt) {
    if (evt.isCancelled()) {
      return;
    }

    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    if (profile == null) {
      return;
    }

    String format = String.format(evt.getFormat(), player.getName(), evt.getMessage());
    TextComponent component = createChatComponent(player, format, profile);

    evt.setCancelled(true);
    evt.getRecipients().forEach(recipient -> handleChatRecipient(recipient, player, format, component, profile));
  }

  private TextComponent createChatComponent(Player player, String format, Profile profile) {
    String currentName = Manager.getCurrent(player.getName());
    Role role = Role.getPlayerRole(player);

    TextComponent component = new TextComponent("");
    for (BaseComponent baseComponent : TextComponent.fromLegacyText(format)) {
      component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + currentName + " "));
      component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
              TextComponent.fromLegacyText(StringUtils.getLastColor(role.getPrefix()) + currentName +
                      "\n§fGroup: " + role.getName() + "\n \n§eClick to send a private message.")));
      component.addExtra(baseComponent);
    }
    return component;
  }

  private void handleChatRecipient(Player recipient, Player sender, String format, TextComponent component, Profile senderProfile) {
    if (recipient == null) {
      return;
    }

    if (format.contains(recipient.getName()) && isMentionEnabled(recipient, sender)) {
      handleMention(recipient, sender, format, component, senderProfile);
    } else {
      recipient.spigot().sendMessage(component);
    }
  }

  private boolean isMentionEnabled(Player recipient, Player sender) {
    Profile recipientProfile = Profile.getProfile(recipient.getName());
    return recipientProfile != null &&
            recipient != sender &&
            recipientProfile.getPreferencesContainer().getChatMention() == ChatMention.ATIVADO;
  }

  private void handleMention(Player recipient, Player sender, String format, TextComponent component, Profile senderProfile) {
    NMSManager.sendActionBar(Role.getColored(sender.getName()) + " §ementioned you in chat!", recipient);
    EnumSound.ORB_PICKUP.play(recipient, 1.0F, 1.0F);
    Role role = Role.getPlayerRole(senderProfile.getPlayer());

    String mentionedFormat = format.replace(recipient.getName(),
            "§e@" + recipient.getName() + (role.isDefault() ? "§7" : "§f"));

    TextComponent mentionedComponent = createChatComponent(sender, mentionedFormat, senderProfile);
    recipient.spigot().sendMessage(mentionedComponent);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent evt) {
    if (evt.isCancelled()) {
      return;
    }

    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    if (profile == null) {
      return;
    }

    String[] args = evt.getMessage().replace("/", "").split(" ");
    if (args.length == 0) {
      return;
    }

    String command = args[0].toLowerCase();
    switch (command) {
      case "lobby":
        handleLobbyCommand(evt, player, profile);
        break;
      case "tell":
        if (args.length > 1) {
          handleTellCommand(evt, args[1], player);
        }
        break;
    }
  }

  private void handleLobbyCommand(PlayerCommandPreprocessEvent evt, Player player, Profile profile) {
    if (!COMMAND_MAP.get(SIMPLE_COMMAND_MAP).containsKey("lobby") ||
            profile.getPreferencesContainer().getProtectionLobby() != ProtectionLobby.ATIVADO) {
      return;
    }

    UUID playerId = player.getUniqueId();
    long lastAttempt = PROTECTION_LOBBY.getOrDefault(playerId, 0L);

    if (lastAttempt > System.currentTimeMillis()) {
      PROTECTION_LOBBY.remove(playerId);
      return;
    }

    evt.setCancelled(true);
    PROTECTION_LOBBY.put(playerId, System.currentTimeMillis() + 3000);
    player.sendMessage("§aAre you sure? Use /lobby again to confirm.");
  }

  private void handleTellCommand(PlayerCommandPreprocessEvent evt, String targetName, Player sender) {
    if (targetName.equalsIgnoreCase(sender.getName())) {
      return;
    }

    Profile targetProfile = Profile.getProfile(targetName);
    if (targetProfile != null && targetProfile.getPreferencesContainer().getPrivateMessages() != PrivateMessages.TODOS) {
      evt.setCancelled(true);
      sender.sendMessage("§cThis user has disabled private messages.");
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());

    if (profile == null || profile.getHotbar() == null) {
      return;
    }

    ItemStack item = player.getItemInHand();
    if (evt.getAction().name().contains("CLICK") && item != null && item.hasItemMeta()) {
      HotbarButton button = profile.getHotbar().compareButton(player, item);
      if (button != null) {
        evt.setCancelled(true);
        button.getAction().execute(profile);
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent evt) {
    if (evt.getRightClicked() instanceof ArmorStand &&
            evt.getPlayer().getGameMode() == GameMode.ADVENTURE) {
      evt.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryClick(InventoryClickEvent evt) {
    if (!(evt.getWhoClicked() instanceof Player)) {
      return;
    }

    Player player = (Player) evt.getWhoClicked();
    Profile profile = Profile.getProfile(player.getName());

    if (profile == null || profile.getHotbar() == null ||
            evt.getClickedInventory() == null || !evt.getClickedInventory().equals(player.getInventory())) {
      return;
    }

    ItemStack item = evt.getCurrentItem();
    if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
      HotbarButton button = profile.getHotbar().compareButton(player, item);
      if (button != null) {
        evt.setCancelled(true);
        button.getAction().execute(profile);
      }
    }
  }

  private void sendUpdateNotification(Player player) {
    new BukkitRunnable() {
      @Override
      public void run() {
        if (aUpdater.UPDATER == null || !aUpdater.UPDATER.canDownload) {
          return;
        }

        TextComponent component = new TextComponent("");
        for (BaseComponent baseComponent : TextComponent.fromLegacyText(" \n §6§laCore Update Available\n \n §7There's an update available for aCore. Click ")) {
          component.addExtra(baseComponent);
        }

        TextComponent click = new TextComponent("HERE");
        click.setColor(ChatColor.GREEN);
        click.setBold(true);
        click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ac update"));
        click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponent.fromLegacyText("§7Click here to update aCore.")));
        component.addExtra(click);

        for (BaseComponent baseComponent : TextComponent.fromLegacyText("§7.\n ")) {
          component.addExtra(baseComponent);
        }

        player.spigot().sendMessage(component);
      }
    }.runTaskLater(Core.getInstance(), 20L);
  }
}