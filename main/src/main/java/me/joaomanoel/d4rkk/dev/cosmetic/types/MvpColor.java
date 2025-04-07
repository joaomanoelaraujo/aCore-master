package me.joaomanoel.d4rkk.dev.cosmetic.types;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cosmetic.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.cosmetic.CosmeticType;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import org.bukkit.inventory.ItemStack;

public class MvpColor extends Cosmetic {

  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger()).getModule("MVPCOLOR");
  private final String name;
  private final String icon;
  private final String messages;

  public MvpColor(long id, String permission, String name, String icon, String messages) {
    super(id, CosmeticType.MVPCOLOR, permission);
    this.name = name;
    this.icon = icon;
    this.messages = messages;
  }
  
  public static void setupColorMvp() {
    KConfig config = Core.getInstance().getConfig("cosmetics", "mvpcolor");
    
    for (String key : config.getKeys(false)) {
      long id = config.getInt(key + ".id");
      String permission = config.getString(key + ".permission");
      String name = config.getString(key + ".name");
      String icon = config.getString(key + ".icon");
     String sound = config.getString(key + ".color");
      
      new MvpColor(id, permission, name, icon, sound);
    }
  }
  
  @Override
  public String getName() {
    return this.name;
  }
  
  public String getColor() {
    return messages;
  }

  @Override
  public ItemStack getIcon(Profile profile) {
    boolean has = this.has(profile);
    boolean canBuy = this.canBuy(profile.getPlayer());
    boolean isSelected = this.isSelected(profile);
    if (isSelected && !canBuy) {
      isSelected = false;
      profile.getAbstractContainer("aCoreProfile", "cselected", SelectedContainer.class).setSelected(getType(), 0);
    }

    Role role = Role.getRoleByPermission(this.getPermission());
    String color = has ?
        (isSelected ? LanguageAPI.getConfig(profile).getString("cosmetics.color.selected") : LanguageAPI.getConfig(profile).getString("cosmetics.color.unlocked")) :
        ((CashManager.CASH && cash >= this.getCash())) && canBuy ? LanguageAPI.getConfig(profile).getString("cosmetics.color.canbuy") : LanguageAPI.getConfig(profile).getString("cosmetics.color.locked");
    String desc = (has && canBuy ?
            LanguageAPI.getConfig(profile).getString("cosmetics.mvpcolor.icon.has_desc.start").replace("{has_desc_status}", isSelected ? LanguageAPI.getConfig(profile).getString("cosmetics.icon.has_desc.selected") : LanguageAPI.getConfig(profile).getString("cosmetics.icon.has_desc.select")) :
        canBuy ?
                LanguageAPI.getConfig(profile).getString("cosmetics.mvpcolor.icon.buy_desc.start")
                .replace("{buy_desc_status}", ((CashManager.CASH && cash >= this.getCash())) ? LanguageAPI.getConfig(profile).getString("cosmetics.icon.buy_desc.click_to_buy") : LanguageAPI.getConfig(profile).getString("cosmetics.icon.buy_desc.enough")) :
                LanguageAPI.getConfig(profile).getString("cosmetics.mvpcolor.icon.perm_desc.start")
                .replace("{perm_desc_status}", (role == null ? LanguageAPI.getConfig(profile).getString("cosmetics.icon.perm_desc.common") : LanguageAPI.getConfig(profile).getString("cosmetics.icon.perm_desc.role").replace("{role}", role.getName()))))
        .replace("{name}", this.name);
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + " : name>" + color + this.name + " : desc>" + desc);
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }

    return item;
  }
}
