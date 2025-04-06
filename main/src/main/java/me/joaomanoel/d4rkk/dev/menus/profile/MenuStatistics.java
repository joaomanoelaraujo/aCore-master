package me.joaomanoel.d4rkk.dev.menus.profile;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.MenuProfile;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuStatistics extends PlayerMenu {

  public MenuStatistics(Profile profile) {
    super(profile.getPlayer(), LanguageAPI.getConfig(profile).getString("menu.statistics.title"), 4);

    this.setItem(10, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
            LanguageAPI.getConfig(profile).getString("menu.statistics.skywars"))));

    this.setItem(12, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
            LanguageAPI.getConfig(profile).getString("menu.statistics.bedwars"))));

    this.setItem(14, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
            LanguageAPI.getConfig(profile).getString("menu.statistics.thebridge"))));

    this.setItem(16, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
            LanguageAPI.getConfig(profile).getString("menu.statistics.thepit"))));

    this.setItem(31, BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.back")));

    this.register(Core.getInstance());
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
            if (evt.getSlot() == 10 || evt.getSlot() == 12 || evt.getSlot() == 14 || evt.getSlot() == 16 || evt.getSlot() == 22) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == 31) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuProfile(profile);
            }
          }
        }
      }
    }
  }

  public void cancel() {
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
