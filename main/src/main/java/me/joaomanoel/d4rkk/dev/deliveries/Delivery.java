package me.joaomanoel.d4rkk.dev.deliveries;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Delivery {
  
  private static final List<Delivery> DELIVERIES = new ArrayList<>();
  private final long id;
  private final int days;
  private final int slot;
  private final String permission;
  private final List<DeliveryReward> rewards;
  private final String icon;
  private final String message;
  
  public Delivery(int days, int slot, String permission, List<DeliveryReward> rewards, String icon, String message) {
    this.id = DELIVERIES.size();
    this.days = days;
    this.slot = slot;
    this.permission = permission;
    this.rewards = rewards;
    this.icon = icon;
    this.message = StringUtils.formatColors(message);
  }
  
  public static void setupDeliveries() {
    KConfig config = Core.getInstance().getConfig("deliveries");
    
    for (String key : config.getSection("deliveries").getKeys(false)) {
      int slot = config.getInt("deliveries." + key + ".slot");
      int days = config.getInt("deliveries." + key + ".days");
      String permission = config.getString("deliveries." + key + ".permission");
      String icon = config.getString("deliveries." + key + ".icon");
      String message = config.getString("deliveries." + key + ".message");
      List<DeliveryReward> rewards = new ArrayList<>();
      for (String reward : config.getStringList("deliveries." + key + ".rewards")) {
        rewards.add(new DeliveryReward(reward));
      }
      
      DELIVERIES.add(new Delivery(days, slot, permission, rewards, icon, message));
    }
  }
  
  public static Collection<Delivery> listDeliveries() {
    return DELIVERIES;
  }
  
  public long getId() {
    return this.id;
  }
  
  public long getDays() {
    return TimeUnit.DAYS.toMillis(this.days);
  }
  
  public int getSlot() {
    return this.slot;
  }
  
  public boolean hasPermission(Player player) {
    return this.permission.isEmpty() || player.hasPermission(this.permission);
  }
  
  public List<DeliveryReward> listRewards() {
    return this.rewards;
  }
  
  public ItemStack getIcon(Profile profile) {
    Player player = profile.getPlayer();
    
    String desc = "";
    boolean permission = !this.hasPermission(player);
    boolean alreadyClaimed = profile.getDeliveriesContainer().alreadyClaimed(this.id);
    if (permission) {
      Role role = Role.getRoleByPermission(this.permission);
      desc = role == null ? "\n \n&cYou do not have permission." : "\n \n&7Exclusive to " + role.getName() + "&7.";
    } else if (alreadyClaimed) {
      desc = "\n \n&7You can collect again in &f" + TimeUtils.getTimeUntil(profile.getDeliveriesContainer().getClaimTime(this.id)) + "&7.";
    }

    
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon.replace("{color}", !permission && !alreadyClaimed ? "&a" : "&c") + desc);
    if (!permission && alreadyClaimed) {
      if (item.getType() == Material.STORAGE_MINECART) {
        item.setType(Material.MINECART);
        item.setDurability((short) 0);
      } else if (item.getType() == Material.POTION) {
        item.setType(Material.GLASS_BOTTLE);
        item.setDurability((short) 0);
      }
    }
    return item;
  }
  
  public String getMessage() {
    return this.message;
  }
}
