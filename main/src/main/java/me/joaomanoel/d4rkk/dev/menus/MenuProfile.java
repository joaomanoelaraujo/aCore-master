package me.joaomanoel.d4rkk.dev.menus;


import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cosmetic.types.JoinMessage;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.apparence.MenuApparence;
import me.joaomanoel.d4rkk.dev.menus.language.MenuLanguages;
import me.joaomanoel.d4rkk.dev.menus.party.MenuParty;
import me.joaomanoel.d4rkk.dev.menus.profile.*;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MenuProfile extends PlayerMenu {

  private static final SimpleDateFormat SDF_EN = new SimpleDateFormat("MMMM d, yyyy 'at' HH:mm", Locale.ENGLISH);
  private static final SimpleDateFormat SDF_PT = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'às' HH:mm", new Locale("pt", "BR"));

  public MenuProfile(Profile profile) {
    super(profile.getPlayer(), LanguageAPI.getConfig(profile).getString("profile.title"), LanguageAPI.getConfig(profile).getInt("menu.prows"));

    SimpleDateFormat dateFormat = SDF_EN;

    String profileInfo = LanguageAPI.getConfig(profile).getString("profile.menu.profile")
            .replace("{rank}", Role.getRoleByName(profile.getDataContainer("aCoreProfile", "role").getAsString()).getName())
            .replace("{cash}", StringUtils.formatNumber(profile.getStats("aCoreProfile", "cash")))
            .replace("{created}", dateFormat.format(profile.getDataContainer("aCoreProfile", "created").getAsLong()))
            .replace("{last}", dateFormat.format(profile.getDataContainer("aCoreProfile", "lastlogin").getAsLong()));

    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.slot"), BukkitUtils.putProfileOnSkull(this.player, BukkitUtils.deserializeItemStack(profileInfo)));

    if (LanguageAPI.getConfig(profile).getBoolean("menuprofile.glass")) {
      for (int i = 9; i <= 17; i++) {
        this.setItem(i, BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menuprofile.color")));
      }
    }

    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.statistics.slot1"),
            BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profile.menu.statistics")));

    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.cosmetics.slot4"),
            BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profile.menu.cosmetics")));

    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.fslot"),
            BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profile.mfriends")));

    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.pslot"),
            BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profile.mparty")));

    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.aslot"),
            BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profile.apparence")));

    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.sslot"),
            BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profile.status")));

    String languages = LanguageAPI.getConfig(profile).getString("profile.language").replace("{languages}", getLanguageMessage());
    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.slot.lang"), BukkitUtils.deserializeItemStack(languages));

    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.prslot2"),
            BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profile.menu.preferences")));

    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.tslot3"),
            BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profile.menu.titles")));

    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.boosters.slot4"),
            BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profile.menu.boosters")));

    this.setItem(LanguageAPI.getConfig(profile).getInt("profile.challenges.slot5"),
            BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("profile.menu.challenges")));

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
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.statistics.slot1")) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuStatistics(profile);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.aslot")){
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuApparence(profile);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.slot.lang")){
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuIdiomas(profile);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.prslot2")) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuPreferences(profile);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.fslot")) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              if (Core.aFriends){
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                //new MenuFriends(profile);
              } else {
                EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 2.0F);
              }
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.pslot")) {
              //todo: fazer o menu de party.
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuParty(profile);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.tslot3")) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuTitles(profile);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.boosters.slot4")) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuBoosters(profile);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.cosmetics.slot4")) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetic<>(profile, LanguageAPI.getConfig(profile).getString("cosmetic.join_message_name"), JoinMessage.class);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.challenges.slot5")) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievements(profile);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.aslot")) {
              EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 2.0F);
            } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("profile.sslot")){
              EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 2.0F);
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

  private String getLanguageMessage() {
    StringBuilder sb = new StringBuilder();
    for (String key : LanguageAPI.listAllKeys()) {
      sb.append("\n    " + "§8§l• §f").append(key);
    }

    return sb.toString();
  }
}
