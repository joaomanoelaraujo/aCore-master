package me.joaomanoel.d4rkk.dev.menus.profile;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.languages.LangAPI;
import me.joaomanoel.d4rkk.dev.achievements.Achievement;
import me.joaomanoel.d4rkk.dev.achievements.types.BedWarsAchievement;
import me.joaomanoel.d4rkk.dev.achievements.types.ThePitAchievement;
import me.joaomanoel.d4rkk.dev.achievements.types.SkyWarsAchievement;
import me.joaomanoel.d4rkk.dev.achievements.types.TheBridgeAchievement;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.MenuProfile;
import me.joaomanoel.d4rkk.dev.menus.profile.achievements.MenuAchievementsList;
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

import java.util.List;

public class MenuAchievements extends PlayerMenu {

  public MenuAchievements(Profile profile) {
    super(profile.getPlayer(), LangAPI.getTranslatedText("menu$achievements$title", profile), 4);

    // Sky Wars achievements
    List<SkyWarsAchievement> skywars = Achievement.listAchievements(SkyWarsAchievement.class);
    long max = skywars.size();
    long completed = skywars.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    String color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    skywars.clear();
    this.setItem(10, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$achievements$skywars", profile)
            .replace("{color}", color)
            .replace("{completed}", String.valueOf(completed))
            .replace("{max}", String.valueOf(max))));

    // Bed Wars achievements
    List<BedWarsAchievement> bedwars = Achievement.listAchievements(BedWarsAchievement.class);
    max = bedwars.size();
    completed = bedwars.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    bedwars.clear();
    this.setItem(12, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$achievements$bedwars", profile)
            .replace("{color}", color)
            .replace("{completed}", String.valueOf(completed))
            .replace("{max}", String.valueOf(max))));

    // The Bridge achievements
    List<TheBridgeAchievement> thebridge = Achievement.listAchievements(TheBridgeAchievement.class);
    max = thebridge.size();
    completed = thebridge.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    thebridge.clear();
    this.setItem(14, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$achievements$thebridge", profile)
            .replace("{color}", color)
            .replace("{completed}", String.valueOf(completed))
            .replace("{max}", String.valueOf(max))));

    // The Pit achievements
    List<ThePitAchievement> thepit = Achievement.listAchievements(ThePitAchievement.class);
    max = thepit.size();
    completed = thepit.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    thepit.clear();
    this.setItem(16, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$achievements$thepit", profile)
            .replace("{color}", color)
            .replace("{completed}", String.valueOf(completed))
            .replace("{max}", String.valueOf(max))));

    // Back button
    this.setItem(31, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("menu$back", profile)));

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
            if (evt.getSlot() == 10) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, LangAPI.getTranslatedText("menu$achievements$skywars", profile), SkyWarsAchievement.class);
            } else if (evt.getSlot() == 12){
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, LangAPI.getTranslatedText("menu$achievements$bedwars", profile), BedWarsAchievement.class);
            } else if (evt.getSlot() == 14){
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, LangAPI.getTranslatedText("menu$achievements$thebridge", profile), TheBridgeAchievement.class);
            } else if (evt.getSlot() == 16){
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, LangAPI.getTranslatedText("menu$achievements$thepit", profile), ThePitAchievement.class);
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
