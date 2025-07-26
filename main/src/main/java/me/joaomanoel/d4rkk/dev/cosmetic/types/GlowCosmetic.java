package me.joaomanoel.d4rkk.dev.cosmetic.types;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.cosmetic.CosmeticType;
import me.joaomanoel.d4rkk.dev.cosmetic.container.SelectedContainer;
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
    this.chatColor = ChatColor.valueOf(color.toUpperCase());   // lança se cor inválida

  }

  public static void setupGlow() {
    KConfig config = Core.getInstance().getConfig("cosmetics", "glowing");
    
    for (String key : config.getKeys(false)) {
      long id = config.getInt(key + ".id");
      if (!config.contains(key + ".cash")) {
        config.set(key + ".cash", getAbsentProperty("glowing", key + ".cash"));
      }
      long cash = config.getInt(key + ".cash", 0);
      String permission = config.getString(key + ".permission");
      String name = config.getString(key + ".name");
      String icon = config.getString(key + ".icon");
      if (!config.contains(key + ".rarity")) {
        config.set(key + ".rarity", getAbsentProperty("glowing", key + ".rarity"));
      }
      String color = config.getString(key + ".color");
      
      new GlowCosmetic(id, EnumRarity.fromName(config.getString(key + ".rarity")), cash, permission, name, icon, color);
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

  public static void ApplyGlow(Player target, ChatColor color, boolean on) {
    ProtocolManager pm = ProtocolLibrary.getProtocolManager();
    String teamId = "glow_" + target.getUniqueId();

    PacketContainer teamPkt = pm.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
    teamPkt.getStrings().write(0, teamId);                   // team name
    PacketContainer metaPkt = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
    metaPkt.getIntegers().write(0, target.getEntityId());

    WrappedDataWatcher watcher   = WrappedDataWatcher.getEntityWatcher(target);
    Byte oldFlagsObj             = watcher.getByte(0);
    byte flags                   = oldFlagsObj == null ? 0 : oldFlagsObj;
    flags = on ? (byte) (flags | 0x40) : (byte) (flags & ~0x40);

    watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), flags);
    metaPkt.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

    for (Player viewer : Bukkit.getOnlinePlayers()) {
      pm.sendServerPacket(viewer, teamPkt);
      pm.sendServerPacket(viewer, metaPkt);
    }
  }

  @Override
  public ItemStack getIcon(Profile profile) {
    long cash = profile.getStats("aCoreProfile", "cash");
    boolean has = this.has(profile);
    boolean canBuy = this.canBuy(profile.getPlayer());
    boolean isSelected = this.isSelected(profile);
    if (isSelected && !canBuy) {
      isSelected = false;
      ApplyGlow(profile.getPlayer(), getChatColor(), false);
      profile.getAbstractContainer("aCoreProfile", "cselected", SelectedContainer.class).setSelected(getType(), 0);
    }
    
    Role role = Role.getRoleByPermission(this.getPermission());
    String color = has ?
        (isSelected ? LanguageAPI.getConfig(profile).getString("cosmetics.color.selected") : LanguageAPI.getConfig(profile).getString("cosmetics.color.unlocked")) :
        ((CashManager.CASH && cash >= this.getCash())) && canBuy ? LanguageAPI.getConfig(profile).getString("cosmetics.color.canbuy") : LanguageAPI.getConfig(profile).getString("cosmetics.color.locked");
    String desc = (has && canBuy ?
            LanguageAPI.getConfig(profile).getString("cosmetics.joinmessage.icon.has_desc.start").replace("{has_desc_status}", isSelected ? LanguageAPI.getConfig(profile).getString("cosmetics.icon.has_desc.selected") : LanguageAPI.getConfig(profile).getString("cosmetics.icon.has_desc.select")) :
        canBuy ?
                LanguageAPI.getConfig(profile).getString("cosmetics.joinmessage.icon.buy_desc.start")
                .replace("{buy_desc_status}", ((CashManager.CASH && cash >= this.getCash())) ? LanguageAPI.getConfig(profile).getString("cosmetics.icon.buy_desc.click_to_buy") : LanguageAPI.getConfig(profile).getString("cosmetics.icon.buy_desc.enough")) :
                LanguageAPI.getConfig(profile).getString("cosmetics.joinmessage.icon.perm_desc.start")
                .replace("{perm_desc_status}", (role == null ? LanguageAPI.getConfig(profile).getString("cosmetics.icon.perm_desc.common") : LanguageAPI.getConfig(profile).getString("cosmetics.icon.perm_desc.role").replace("{role}", role.getName()))))
        .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{cash}", StringUtils.formatNumber(this.getCash()));
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + " : name>" + color + this.name + " : desc>" + desc);
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
      ApplyGlow(profile.getPlayer(), getChatColor(), true);
    }
    return item;
  }
}
