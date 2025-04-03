package me.joaomanoel.d4rkk.dev.menus;

import me.clip.placeholderapi.PlaceholderAPI;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.languages.translates.EN_US;
import me.joaomanoel.d4rkk.dev.libraries.menu.UpdatablePlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.servers.ServerItem;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuServers extends UpdatablePlayerMenu {
  
  public MenuServers(Profile profile) {
    super(profile.getPlayer(), ServerItem.CONFIG.getString("title"), ServerItem.CONFIG.getInt("rows"));


    if (EN_US.menu$minigames$active) {
      this.setItem(EN_US.menu$minigames$slot, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
              EN_US.menu$minigames$contribute)));
    }

    this.update();
    this.register(Core.getInstance(), 20);
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ItemStack item = evt.getCurrentItem();
          
          if (item != null && item.getType() != Material.AIR) {
            if (ServerItem.DISABLED_SLOTS.contains(evt.getSlot())) {
              this.player.sendMessage("§cYou're already connected to this server.");
              return;

            } else if (evt.getSlot() == EN_US.menu$minigames$slot){
              if (EN_US.menu$minigames$active) {
                player.chat("/shop");
              }
            }
            
            ServerItem.listServers().stream().filter(s -> s.getSlot() == evt.getSlot()).findFirst().ifPresent(serverItem -> serverItem.connect(profile));
          }
        }
      }
    }
  }
  
  @Override
  public void update() {
    for (ServerItem serverItem : ServerItem.listServers()) {
      this.setItem(serverItem.getSlot(),
          BukkitUtils.deserializeItemStack(serverItem.getIcon().replace("{players}", StringUtils.formatNumber(ServerItem.getServerCount(serverItem)))));
    }
  }
  
  public void cancel() {
    super.cancel();
    HandlerList.unregisterAll(this);
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
}
