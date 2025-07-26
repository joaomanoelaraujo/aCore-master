package me.joaomanoel.d4rkk.dev.menus.apparence;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cosmetic.types.GlowCosmetic;
import me.joaomanoel.d4rkk.dev.cosmetic.types.MvpColor;
import me.joaomanoel.d4rkk.dev.cosmetic.types.PunchMessage;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.MenuProfile;
import me.joaomanoel.d4rkk.dev.menus.apparence.select.MenuSelect;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;


public class MenuApparence extends PlayerMenu {


  public MenuApparence(Profile profile) {
    super(profile.getPlayer(), LanguageAPI.getConfig(profile).getString("menu.apparence.title"), LanguageAPI.getConfig(profile).getInt("menu.apparence.rows"));

    this.setItem(LanguageAPI.getConfig(profile).getInt("apparence.color.slot"), BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.apparence.color")));
    this.setItem(LanguageAPI.getConfig(profile).getInt("apparence.punch.slot"), BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.apparence.punch")));
    this.setItem(LanguageAPI.getConfig(profile).getInt("apparence.status.slot"), BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.apparence.status")));
    this.setItem(0, BukkitUtils.deserializeItemStack("PRISMARINE_CRYSTALS : 1 : name>§aGlow : desc>§eClique para selecionar"));

    this.setItem(31, BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.back")));

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
            if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("apparence.color.slot")) {
              if (player.hasPermission("role.mvpplus")) {
                EnumSound.CLICK.play(player, 1.0F, 2.0F);
                new MenuSelect<>(profile, LanguageAPI.getConfig(profile).getString("menu.apparence.title"), MvpColor.class);
              } else {
                EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 2.0F);
              }
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("apparence.punch.slot")){
              if (player.hasPermission("role.mvpplus")) {
                EnumSound.CLICK.play(player, 1.0F, 2.0F);
                new MenuSelect<>(profile, LanguageAPI.getConfig(profile).getString("menu.apparence.title"), PunchMessage.class);
              } else {
                EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 2.0F);
              }
            } else if (evt.getSlot() == 0){
                EnumSound.CLICK.play(player, 1.0F, 2.0F);
                new MenuSelect<>(profile, "Glow", GlowCosmetic.class);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("apparence.status.slot")){
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