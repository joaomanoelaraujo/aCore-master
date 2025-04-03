package me.joaomanoel.d4rkk.dev.menus.profile;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.languages.LangAPI;
import me.joaomanoel.d4rkk.dev.booster.Booster;
import me.joaomanoel.d4rkk.dev.booster.NetworkBooster;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.MenuProfile;
import me.joaomanoel.d4rkk.dev.menus.profile.boosters.MenuBoostersList;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.TimeUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuBoosters extends PlayerMenu {

  public MenuBoosters(Profile profile) {
    super(profile.getPlayer(), LangAPI.getTranslatedText("menu$boosters$title", profile), 4);

    this.setItem(12, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$boosters$personal", profile)));
    this.setItem(14, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$boosters$global", profile)));

    String booster = profile.getBoostersContainer().getEnabled();
    StringBuilder result = new StringBuilder(), network = new StringBuilder();
    for (int index = 0; index < Core.minigames.size(); index++) {
      String minigame = Core.minigames.get(index);
      NetworkBooster nb = Booster.getNetworkBooster(minigame);
      String status = nb == null ? LangAPI.getTranslatedText("menu$boosters$networkDisabled", profile)
              : LangAPI.getTranslatedText("menu$boosters$networkActive", profile)
              .replace("{multiplier}", String.valueOf(nb.getMultiplier()))
              .replace("{player}", Role.getColored(nb.getBooster()))
              .replace("{time}", TimeUtils.getTimeUntil(nb.getExpires()));
      network.append(LangAPI.getTranslatedText("menu$boosters$network", profile)
                      .replace("{minigame}", minigame).replace("{status}", status))
              .append(index + 1 == Core.minigames.size() ? "" : "\n");
    }
    result.append(LangAPI.getTranslatedText("menu$boosters$active", profile)
            .replace("{activeMultiplier}", booster == null ? LangAPI.getTranslatedText("menu$boosters$none", profile)
                    : LangAPI.getTranslatedText("menu$boosters$calculation", profile)
                    .replace("{multiplier}", booster.split(":")[0])
                    .replace("{time}", TimeUtils.getTimeUntil(Long.parseLong(booster.split(":")[1])))
                    .replace("{total}", String.valueOf((int) (50.0 * Double.parseDouble(booster.split(":")[0]))))));

    this.setItem(30, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$back", profile)));
    this.setItem(31, BukkitUtils.deserializeItemStack(
            LangAPI.getTranslatedText("menu$boosters$credits", profile)
                    .replace("{network}", network.toString()).replace("{result}", result.toString())));

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
            if (evt.getSlot() == 12) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              new MenuBoostersList<>(profile, LangAPI.getTranslatedText("booster$type2", profile), Booster.BoosterType.PRIVATE);
            } else if (evt.getSlot() == 14) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              new MenuBoostersList<>(profile, LangAPI.getTranslatedText("booster$type", profile), Booster.BoosterType.NETWORK);
            } else if (evt.getSlot() == 30) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuProfile(profile);
            } else if (evt.getSlot() == 31) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
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
