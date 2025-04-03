package me.joaomanoel.d4rkk.dev.menus.profile.boosters;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.languages.translates.EN_US;
import me.joaomanoel.d4rkk.dev.achievements.Achievement;
import me.joaomanoel.d4rkk.dev.booster.Booster;
import me.joaomanoel.d4rkk.dev.libraries.menu.PagedPlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.profile.MenuBoosters;
import me.joaomanoel.d4rkk.dev.player.Profile;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MenuBoostersList<T extends Achievement> extends PagedPlayerMenu {

  private Booster.BoosterType type;
  private Map<ItemStack, Booster> boosters = new HashMap<>();

  public MenuBoostersList(Profile profile, String name, Booster.BoosterType type) {
    super(profile.getPlayer(), "Boosters " + name, 5);
    this.type = type;
    this.previousPage = 36;
    this.nextPage = 44;
    this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25);

    this.removeSlotsWith(BukkitUtils.deserializeItemStack(EN_US.menu$back), 40);

    List<ItemStack> items = new ArrayList<>();
    List<Booster> boosters = profile.getBoostersContainer().getBoosters(type);
    for (Booster booster : boosters) {
      String boosterType = type == Booster.BoosterType.NETWORK ? EN_US.booster$type : EN_US.booster$type2;
      ItemStack icon = BukkitUtils.deserializeItemStack(
              EN_US.booster$list$item
                      .replace("{type}", type == Booster.BoosterType.NETWORK ? ":8232" : "")
                      .replace("{boosterType}", boosterType)
                      .replace("{multiplier}", String.valueOf(booster.getMultiplier()))
                      .replace("{duration}", TimeUtils.getTime(TimeUnit.HOURS.toMillis(booster.getHours()))));
      items.add(icon);
      this.boosters.put(icon, booster);
    }

    if (items.isEmpty()) {
      this.removeSlotsWith(BukkitUtils.deserializeItemStack(EN_US.booster$list$empty), 22);
    }
    this.setItems(items);
    boosters.clear();
    items.clear();

    this.register(Core.getInstance());
    this.open();
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getCurrentInventory())) {
      evt.setCancelled(true);

      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }

        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
          ItemStack item = evt.getCurrentItem();

          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == this.previousPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openPrevious();
            } else if (evt.getSlot() == this.nextPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openNext();
            } else if (evt.getSlot() == 40) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuBoosters(profile);
            } else {
              Booster booster = this.boosters.get(item);
              if (booster != null) {
                if (type == Booster.BoosterType.NETWORK) {
                  if (!Core.minigames.contains(Core.minigame)) {
                    this.player.sendMessage(EN_US.booster$network$notInMinigame);
                    return;
                  }

                  if (!Booster.setNetworkBooster(Core.minigame, profile, booster)) {
                    EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                    this.player.sendMessage(EN_US.booster$network$alreadyActive.replace("{minigame}", Core.minigame));
                    this.player.closeInventory();
                    return;
                  }

                  EnumSound.LEVEL_UP.play(this.player, 0.5F, 1.0F);
                  Profile.listProfiles().forEach(pf -> pf.getPlayer().sendMessage(EN_US.booster$network$activated
                          .replace("{player}", this.player.getName())
                          .replace("{multiplier}", String.valueOf(booster.getMultiplier()))
                          .replace("{minigame}", Core.minigame)));
                  this.player.closeInventory();
                } else {
                  if (!profile.getBoostersContainer().enable(booster)) {
                    EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                    this.player.sendMessage(EN_US.booster$personal$alreadyActive);
                    this.player.closeInventory();
                    return;
                  }

                  this.player.sendMessage(
                          EN_US.booster$personal$activated
                                  .replace("{multiplier}", String.valueOf(booster.getMultiplier()))
                                  .replace("{duration}", TimeUtils.getTime(TimeUnit.HOURS.toMillis(booster.getHours()))));
                  new MenuBoosters(profile);
                }
              }
            }
          }
        }
      }
    }
  }

  public void cancel() {
    this.type = null;
    this.boosters.values().forEach(Booster::gc);
    this.boosters.clear();
    this.boosters = null;
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
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
      this.cancel();
    }
  }
}
