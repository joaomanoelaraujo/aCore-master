package me.joaomanoel.d4rkk.dev.menus.apparence;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cosmetic.types.MvpColor;
import me.joaomanoel.d4rkk.dev.cosmetic.types.PunchMessage;
import me.joaomanoel.d4rkk.dev.languages.LangAPI;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.MenuProfile;
import me.joaomanoel.d4rkk.dev.menus.apparence.select.MenuSelect;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static me.joaomanoel.d4rkk.dev.languages.translates.EN_US.*;

public class MenuApparence extends PlayerMenu {


  public MenuApparence(Profile profile) {
    super(profile.getPlayer(), LangAPI.getTranslatedText("menu$apparence$title", profile), menu$apparence$rows);

    this.setItem(apparence$color$slot, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$apparence$color", profile)));
    this.setItem(apparence$punch$slot, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$apparence$punch", profile)));
    this.setItem(apparence$status$slot, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$apparence$status", profile)));

    this.setItem(31, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$back", profile)));

    this.register(Core.getInstance());
    this.open();
  }


  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory() != null && evt.getInventory().equals(this.getInventory())) {
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
            if (evt.getSlot() == apparence$color$slot) {
              if (player.hasPermission("role.mvpplus")) {
                EnumSound.CLICK.play(player, 1.0F, 2.0F);
                new MenuSelect<>(profile, LangAPI.getTranslatedText("menu$apparence$title", profile), MvpColor.class);
              } else {
                EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 2.0F);
              }
            } else if (evt.getSlot() == apparence$punch$slot){
              if (player.hasPermission("role.mvpplus")) {
                EnumSound.CLICK.play(player, 1.0F, 2.0F);
                new MenuSelect<>(profile, LangAPI.getTranslatedText("menu$apparence$title", profile), PunchMessage.class);
              } else {
                EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 2.0F);
              }
            } else if (evt.getSlot() == apparence$status$slot){
              EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 2.0F);
            } else if (evt.getSlot() == 31){
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