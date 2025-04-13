package me.joaomanoel.d4rkk.dev.menus.profile;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.database.data.container.PreferencesContainer;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.MenuProfile;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.enums.*;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuPreferences extends PlayerMenu {

  public MenuPreferences(Profile profile) {
    super(profile.getPlayer(), LanguageAPI.getConfig(profile).getString("menu.preferences.title"), 5);

    PreferencesContainer pc = profile.getPreferencesContainer();

    PlayerVisibility pv = pc.getPlayerVisibility();
    this.setItem(11, BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.preferences.players")));
    this.setItem(20, BukkitUtils.deserializeItemStack(
            LanguageAPI.getConfig(profile).getString("menu.preferences.state")
                    .replace("{inkSack}", pv.getInkSack())
                    .replace("{name}", pv.getName())
                    .replace("{state}", StringUtils.stripColors(pv.getName()))));

    PrivateMessages pm = pc.getPrivateMessages();
    this.setItem(12, BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.preferences.privateMessages")));
    this.setItem(21, BukkitUtils.deserializeItemStack(
            LanguageAPI.getConfig(profile).getString("menu.preferences.state")
                    .replace("{inkSack}", pm.getInkSack())
                    .replace("{name}", pm.getName())
                    .replace("{state}", StringUtils.stripColors(pm.getName()))));

    BloodAndGore bg = pc.getBloodAndGore();
    this.setItem(14, BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.preferences.violence")));
    this.setItem(23, BukkitUtils.deserializeItemStack(
            LanguageAPI.getConfig(profile).getString("menu.preferences.state")
                    .replace("{inkSack}", bg.getInkSack())
                    .replace("{name}", bg.getName())
                    .replace("{state}", StringUtils.stripColors(bg.getName()))));

    ProtectionLobby pl = pc.getProtectionLobby();
    this.setItem(15, BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.preferences.protectionLobby")));
    this.setItem(24, BukkitUtils.deserializeItemStack(
            LanguageAPI.getConfig(profile).getString("menu.preferences.state")
                    .replace("{inkSack}", pl.getInkSack())
                    .replace("{name}", pl.getName())
                    .replace("{state}", StringUtils.stripColors(pl.getName()))));

    ChatMention cm = pc.getChatMention();
    this.setItem(13, BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.preferences.chatMention")));
    this.setItem(22, BukkitUtils.deserializeItemStack(
            LanguageAPI.getConfig(profile).getString("menu.preferences.state")
                    .replace("{inkSack}", cm.getInkSack())
                    .replace("{name}", cm.getName())
                    .replace("{state}", StringUtils.stripColors(cm.getName()))));
    this.setItem(40, BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.back")));

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
            if (evt.getSlot() == 10 || evt.getSlot() == 11 || evt.getSlot() == 12 || evt.getSlot() == 14 || evt.getSlot() == 15 || evt.getSlot() == 16) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == 20) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changePlayerVisibility();
              if (!profile.playingGame()) {
                profile.refreshPlayers();
              }
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 21) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changePrivateMessages();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 23) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeBloodAndGore();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 24) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeProtectionLobby();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 22) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeChatMention();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 40) {
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
