package me.joaomanoel.d4rkk.dev.player.hotbar;

import me.clip.placeholderapi.PlaceholderAPI;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Hotbar {
  
  private static final List<Hotbar> HOTBARS = new ArrayList<>();
  private final String id;
  private final List<HotbarButton> buttons;
  
  public Hotbar(String id) {
    this.id = id;
    this.buttons = new ArrayList<>();
  }
  
  public static void addHotbar(Hotbar hotbar) {
    HOTBARS.add(hotbar);
  }
  
  public static Hotbar getHotbarById(String id) {
    return HOTBARS.stream().filter(hb -> hb.getName().equalsIgnoreCase(id)).findFirst().orElse(null);
  }
  
  public String getName() {
    return this.id;
  }
  
  public List<HotbarButton> getButtons() {
    return this.buttons;
  }
  
  public void apply(Profile profile) {
    Player player = profile.getPlayer();
    
    player.getInventory().clear();
    player.getInventory().setArmorContents(null);
    
    this.buttons.stream().filter(button -> button.getSlot() >= 0 && button.getSlot() <= 8).forEach(button -> {
      ItemStack icon = BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(player, button.getIcon().replace("%perfil%", "")));
      player.getInventory().setItem(button.getSlot(), button.getIcon().contains("%perfil%") ? BukkitUtils.putProfileOnSkull(player, icon) : icon);
    });
    
    player.updateInventory();
  }
  
  public HotbarButton compareButton(Player player, ItemStack item) {
    return this.buttons.stream().filter(button -> button.getSlot() >= 0 && button.getSlot() <= 8 && item.equals(player.getInventory().getItem(button.getSlot()))).findFirst()
        .orElse(null);
  }
}
