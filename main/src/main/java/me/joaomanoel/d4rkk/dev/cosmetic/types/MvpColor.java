package me.joaomanoel.d4rkk.dev.cosmetic.types;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cosmetic.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.languages.translates.EN_US;
import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.cosmetic.CosmeticType;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumRarity;
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
        (isSelected ? EN_US.cosmetics$color$selected : EN_US.cosmetics$color$unlocked) :
        ((CashManager.CASH && cash >= this.getCash())) && canBuy ? EN_US.cosmetics$color$canbuy : EN_US.cosmetics$color$locked;
    String desc = (has && canBuy ?
        EN_US.cosmetics$mvpcolor$icon$has_desc$start.replace("{has_desc_status}", isSelected ? EN_US.cosmetics$icon$has_desc$selected : EN_US.cosmetics$icon$has_desc$select) :
        canBuy ?
            EN_US.cosmetics$mvpcolor$icon$buy_desc$start
                .replace("{buy_desc_status}", ((CashManager.CASH && cash >= this.getCash())) ? EN_US.cosmetics$icon$buy_desc$click_to_buy : EN_US.cosmetics$icon$buy_desc$enough) :
            EN_US.cosmetics$mvpcolor$icon$perm_desc$start
                .replace("{perm_desc_status}", (role == null ? EN_US.cosmetics$icon$perm_desc$common : EN_US.cosmetics$icon$perm_desc$role.replace("{role}", role.getName()))))
        .replace("{name}", this.name);
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + " : name>" + color + this.name + " : desc>" + desc);
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }

    return item;
  }
}
