package me.joaomanoel.d4rkk.dev.cosmetic.types;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.cosmetic.CosmeticType;
import me.joaomanoel.d4rkk.dev.cosmetic.GlowUtils;
import me.joaomanoel.d4rkk.dev.cosmetic.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.game.FakeGame;
import me.joaomanoel.d4rkk.dev.game.Game;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumRarity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GlowCosmetic extends Cosmetic {

  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger()).getModule("GLOW");
  private final String name;
  private final String icon;
  private final String color;
  private final ChatColor chatColor;

  public GlowCosmetic(long id, EnumRarity rarity, long cash, String permission, String name, String icon, String color) {
    super(id, CosmeticType.GLOW, permission);
    this.name = name;
    this.icon = icon;
    this.color = color;
    this.rarity = rarity;
    this.cash = cash;
    this.chatColor = ChatColor.valueOf(color.toUpperCase());
  }

  public static void setupGlow() {
    KConfig config = Core.getInstance().getConfig("cosmetics", "glowing");
    for (String key : config.getKeys(false)) {
      long id = config.getInt(key + ".id", 0);
      long cash = config.getInt(key + ".cash", 0);
      String permission = config.getString(key + ".permission");
      String name = config.getString(key + ".name");
      String icon = config.getString(key + ".icon");
      String rarityName = config.getString(key + ".rarity");
      String colorName = config.getString(key + ".color");

      EnumRarity rarity = EnumRarity.fromName(rarityName);
      new GlowCosmetic(id, rarity, cash, permission, name, icon, colorName);
    }
  }

  public ChatColor getChatColor() {
    return chatColor;
  }

  @Override
  public String getName() {
    return this.name;
  }

  public String getColor() {
    return color;
  }

  @Override
  public ItemStack getIcon(Profile profile) {
    long cash = profile.getStats("aCoreProfile", "cash");
    boolean has = this.has(profile);
    boolean canBuy = this.canBuy(profile.getPlayer());
    boolean isSelected = this.isSelected(profile);

    if (isSelected && !canBuy) {
      isSelected = false;
      profile.getAbstractContainer("aCoreProfile", "cselected", SelectedContainer.class)
              .setSelected(getType(), 0);
    }

    Role role = Role.getRoleByPermission(this.getPermission());
    String color = has ?
            (isSelected ? LanguageAPI.getConfig(profile).getString("cosmetics.color.selected") :
                    LanguageAPI.getConfig(profile).getString("cosmetics.color.unlocked")) :
            ((CashManager.CASH && cash >= this.getCash())) && canBuy ?
                    LanguageAPI.getConfig(profile).getString("cosmetics.color.canbuy") :
                    LanguageAPI.getConfig(profile).getString("cosmetics.color.locked");

    String desc = (has && canBuy ?
            LanguageAPI.getConfig(profile).getString("cosmetics.joinmessage.icon.has_desc.start")
                    .replace("{has_desc_status}", isSelected ?
                            LanguageAPI.getConfig(profile).getString("cosmetics.icon.has_desc.selected") :
                            LanguageAPI.getConfig(profile).getString("cosmetics.icon.has_desc.select")) :
            canBuy ?
                    LanguageAPI.getConfig(profile).getString("cosmetics.joinmessage.icon.buy_desc.start")
                            .replace("{buy_desc_status}",
                                    ((CashManager.CASH && cash >= this.getCash())) ?
                                            LanguageAPI.getConfig(profile).getString("cosmetics.icon.buy_desc.click_to_buy") :
                                            LanguageAPI.getConfig(profile).getString("cosmetics.icon.buy_desc.enough")) :
                    LanguageAPI.getConfig(profile).getString("cosmetics.joinmessage.icon.perm_desc.start")
                            .replace("{perm_desc_status}", (role == null ?
                                    LanguageAPI.getConfig(profile).getString("cosmetics.icon.perm_desc.common") :
                                    LanguageAPI.getConfig(profile).getString("cosmetics.icon.perm_desc.role")
                                            .replace("{role}", role.getName())))).replace("{name}", this.name)
            .replace("{rarity}", this.getRarity().getName())
            .replace("{cash}", StringUtils.formatNumber(this.getCash()));

    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + " : name>" + color + this.name + " : desc>" + desc);

    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }
    return item;
  }

  // ===== SISTEMA DE CONTROLE DO GLOW =====

  /**
   * Verifica se um jogador está em uma partida real
   */
  private static boolean isInRealGame(Profile profile) {
    if (profile == null) return false;

    Game<?> game = profile.getGame();
    return game != null && !(game instanceof FakeGame);
  }

  /**
   * Obtém o glow selecionado do jogador usando o SelectedContainer
   */
  private static GlowCosmetic getSelectedGlow(Profile profile) {
    if (profile == null) return null;

    SelectedContainer container = profile.getAbstractContainer("aCoreProfile", "cselected", SelectedContainer.class);
    return container.getSelected(CosmeticType.GLOW, GlowCosmetic.class);
  }

  /**
   * Aplica o glow para TODOS os outros jogadores verem (exceto o próprio)
   */
  public static void applyGlowForAllViewers(Profile profile) {
    Player player = profile.getPlayer();
    if (player == null || isInRealGame(profile)) return;

    GlowCosmetic glow = getSelectedGlow(profile);
    if (glow != null) {
      // Aplica o glow para TODOS os outros jogadores online
      for (Player viewer : Bukkit.getOnlinePlayers()) {
        if (!viewer.equals(player)) {
          GlowUtils.applyGlow(player, viewer, glow.getChatColor());
        }
      }
    }
  }

  /**
   * Remove o glow de TODOS os viewers
   */
  public static void removeGlowFromAllViewers(Profile profile) {
    Player player = profile.getPlayer();
    if (player == null) return;

    // Remove o glow de TODOS os jogadores online
    for (Player viewer : Bukkit.getOnlinePlayers()) {
      if (!viewer.equals(player)) {
        GlowUtils.removeGlow(player, viewer);
      }
    }
  }

  /**
   * Aplica o glow para um viewer específico
   */
  public static void applyGlowForViewer(Profile profile, Player viewer) {
    Player player = profile.getPlayer();
    if (player == null || isInRealGame(profile) || viewer.equals(player)) return;

    GlowCosmetic glow = getSelectedGlow(profile);
    if (glow != null) {
      GlowUtils.applyGlow(player, viewer, glow.getChatColor());
    }
  }

  /**
   * Remove o glow de um viewer específico
   */
  public static void removeGlowFromViewer(Profile profile, Player viewer) {
    Player player = profile.getPlayer();
    if (player != null && !viewer.equals(player)) {
      GlowUtils.removeGlow(player, viewer);
    }
  }

  /**
   * Chamado quando um jogador entra em uma partida real - remove o glow
   */
  public static void onEnterGame(Profile profile) {
    removeGlowFromAllViewers(profile);
  }

  /**
   * Chamado quando um jogador sai de uma partida real - restaura o glow se estiver selecionado
   */
  public static void onLeaveGame(Profile profile) {
    if (hasGlowSelected(profile)) {
      applyGlowForAllViewers(profile);
    }
  }

  /**
   * Chamado quando um novo jogador entra no servidor - aplica todos os glows ativos para ele
   */
  public static void onPlayerJoin(Player newPlayer) {
    for (Player online : Bukkit.getOnlinePlayers()) {
      Profile onlineProfile = Profile.getProfile(online.getName());
      if (onlineProfile != null && hasGlowSelected(onlineProfile) && !isInRealGame(onlineProfile)) {
        applyGlowForViewer(onlineProfile, newPlayer);
      }
    }
  }

  /**
   * Verifica se um jogador tem glow selecionado
   */
  public static boolean hasGlowSelected(Profile profile) {
    return getSelectedGlow(profile) != null;
  }
}