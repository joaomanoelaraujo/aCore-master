package me.joaomanoel.d4rkk.dev.menus.others;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.api.StatsAPI;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuOtherProfile extends PlayerMenu {

  private final Player player2;

  public MenuOtherProfile(Profile profile, Player player2) {
    super(profile.getPlayer(), player2.getName(), 6);
    this.player2 = player2;
    StatsAPI levelAPI = new StatsAPI();
    Profile profile1 = Profile.getProfile(player2.getName());

    String levelInfo = String.format(LanguageAPI.getConfig(profile).getString("profileo.menu.levelInfo"),
            Role.getPrefixed(player2.getName()), levelAPI.getLevel(profile1), levelAPI.getGuild(),
            Role.getRoleByName(profile1.getDataContainer("aCoreProfile", "role").getAsString()).getName());

    this.setItem(0, BukkitUtils.putProfileOnSkull(player2, BukkitUtils.deserializeItemStack(levelInfo)));

    for (int i = 9; i < 18; i++) {
      this.getInventory().setItem(i, BukkitUtils.deserializeItemStack("GLASS"));
    }

    this.setItem(49, BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profileo.menu.close")));

    this.setItem(21, BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profileo.menu.socialMedia")));
    this.setItem(23, BukkitUtils.deserializeItemStack(String.format(LanguageAPI.getConfig(profile).getString("profileo.menu.statistics"),
            Role.getColored(player2.getName()))));
    this.setItem(30, BukkitUtils.deserializeItemStack(String.format(LanguageAPI.getConfig(profile).getString("profileo.menu.partyInvite"),
            Role.getColored(player2.getName()))));
    this.setItem(31, BukkitUtils.deserializeItemStack(String.format(LanguageAPI.getConfig(profile).getString("profileo.menu.friendInvite"),
            Role.getColored(player2.getName()))));
    this.setItem(32, BukkitUtils.deserializeItemStack(String.format(LanguageAPI.getConfig(profile).getString("profileo.menu.blockPlayer"),
            Role.getColored(player2.getName()))));
    this.setItem(39, BukkitUtils.deserializeItemStack(String.format(LanguageAPI.getConfig(profile).getString("profileo.menu.guildInvite"),
            Role.getColored(player2.getName()))));
    this.setItem(40, BukkitUtils.deserializeItemStack(String.format(LanguageAPI.getConfig(profile).getString("profileo.menu.leadershipPass"),
            Role.getColored(player2.getName()))));

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
            if (evt.getSlot() == 49) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              player.closeInventory();
            } else if (evt.getSlot() == 21) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == 23) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == 30) {
              EnumSound.BAT_LOOP.play(this.player, 1.5F, 1.5F);
              profile.getPlayer().performCommand("party invite " + player2.getName());
            } else if (evt.getSlot() == 31) {
              EnumSound.BAT_LOOP.play(this.player, 1.5F, 1.5F);
              profile.getPlayer().performCommand("friend add " + player2.getName());
            } else if (evt.getSlot() == 32) {
              EnumSound.ANVIL_BREAK.play(this.player, 0.5F, 2.0F);
              profile.getPlayer().performCommand("friend block " + player2.getName());
            } else if (evt.getSlot() == 39) { //Guild invite command
              EnumSound.VILLAGER_NO.play(this.player, 1.0F, 2.0F);
            } else if (evt.getSlot() == 40) {
              EnumSound.LEVEL_UP.play(this.player, 0.5F, 2.0F);
              profile.getPlayer().performCommand("party transfer " + player2.getName());
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
