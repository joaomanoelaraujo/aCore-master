package me.joaomanoel.d4rkk.dev.menus;


import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.languages.LangAPI;
import me.joaomanoel.d4rkk.dev.languages.translates.EN_US;
import me.joaomanoel.d4rkk.dev.cosmetic.types.JoinMessage;
import me.joaomanoel.d4rkk.dev.languages.GLanguage;
import me.joaomanoel.d4rkk.dev.languages.translates.PT_BR;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.apparence.MenuApparence;
import me.joaomanoel.d4rkk.dev.menus.language.MenuLanguages;
import me.joaomanoel.d4rkk.dev.menus.party.MenuParty;
import me.joaomanoel.d4rkk.dev.menus.profile.*;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
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

import static me.joaomanoel.d4rkk.dev.languages.translates.EN_US.*;

public class MenuProfile extends PlayerMenu {

  private static final SimpleDateFormat SDF_EN = new SimpleDateFormat("MMMM d, yyyy 'at' HH:mm", Locale.ENGLISH);
  private static final SimpleDateFormat SDF_PT = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'às' HH:mm", new Locale("pt", "BR"));

  public MenuProfile(Profile profile) {
    super(profile.getPlayer(), LangAPI.getTranslatedText("profile$title", profile), menu$prows);

    // Continue com o restante do método
    GLanguage languageId = LangAPI.getLanguageId(profile);
    SimpleDateFormat dateFormat = (languageId.getId() == 1) ? SDF_EN : SDF_PT;

    String profileInfo = getTranslatedText("profile$menu$profile", profile)
            .replace("{rank}", Role.getRoleByName(profile.getDataContainer("aCoreProfile", "role").getAsString()).getName())
            .replace("{cash}", StringUtils.formatNumber(profile.getStats("aCoreProfile", "cash")))
            .replace("{created}", dateFormat.format(profile.getDataContainer("aCoreProfile", "created").getAsLong()))
            .replace("{last}", dateFormat.format(profile.getDataContainer("aCoreProfile", "lastlogin").getAsLong()));

    this.setItem(getTranslatedInt("profile$slot", profile), BukkitUtils.putProfileOnSkull(this.player, BukkitUtils.deserializeItemStack(profileInfo)));

    if (getTranslatedBoolean("menuprofile$glass", profile)) {
      for (int i = 9; i <= 17; i++) {
        this.setItem(i, BukkitUtils.deserializeItemStack(getTranslatedText("menuprofile$color", profile)));
      }
    }

    this.setItem(getTranslatedInt("profile$statistics$slot1", profile),
            BukkitUtils.deserializeItemStack(getTranslatedText("profile$menu$statistics", profile)));

    this.setItem(getTranslatedInt("profile$cosmetics$slot4", profile),
            BukkitUtils.deserializeItemStack(getTranslatedText("profile$menu$cosmetics", profile)));

    this.setItem(getTranslatedInt("profile$fslot", profile),
            BukkitUtils.deserializeItemStack(getTranslatedText("profile$mfriends", profile)));

    this.setItem(getTranslatedInt("profile$pslot", profile),
            BukkitUtils.deserializeItemStack(getTranslatedText("profile$mparty", profile)));

    this.setItem(getTranslatedInt("profile$aslot", profile),
            BukkitUtils.deserializeItemStack(getTranslatedText("profile$apparence", profile)));

    this.setItem(getTranslatedInt("profile$sslot", profile),
            BukkitUtils.deserializeItemStack(getTranslatedText("profile$status", profile)));

    String languages = getTranslatedText("profile$language", profile)
            .replace("{languages}", "\n    " + getAvailableLanguages());
    this.setItem(getTranslatedInt("profile$slot$lang", profile), BukkitUtils.deserializeItemStack(languages));

    this.setItem(getTranslatedInt("profile$prslot2", profile),
            BukkitUtils.deserializeItemStack(getTranslatedText("profile$menu$preferences", profile)));

    this.setItem(getTranslatedInt("profile$tslot3", profile),
            BukkitUtils.deserializeItemStack(getTranslatedText("profile$menu$titles", profile)));

    this.setItem(getTranslatedInt("profile$boosters$slot4", profile),
            BukkitUtils.deserializeItemStack(getTranslatedText("profile$menu$boosters", profile)));

    this.setItem(getTranslatedInt("profile$challenges$slot5", profile),
            BukkitUtils.deserializeItemStack(getTranslatedText("profile$menu$challenges", profile)));

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
            } else if (evt.getSlot() == profile$statistics$slot1) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuStatistics(profile);
            } else if (evt.getSlot() == profile$aslot){
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuApparence(profile);
            } else if (evt.getSlot() == profile$slot$lang){
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuLanguages<>(profile, "Languages", GLanguage.class);
            } else if (evt.getSlot() == profile$prslot2) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuPreferences(profile);
            } else if (evt.getSlot() == profile$fslot) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              if (Core.aFriends){
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                //new MenuFriends(profile);
              } else {
                EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 2.0F);
              }
            } else if (evt.getSlot() == profile$pslot) {
              //todo: fazer o menu de party.
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuParty(profile);
            } else if (evt.getSlot() == profile$tslot3) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuTitles(profile);
            } else if (evt.getSlot() == profile$boosters$slot4) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuBoosters(profile);
            } else if (evt.getSlot() == profile$cosmetics$slot4) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetic<>(profile, cosmetic$join_message_name, JoinMessage.class);
            } else if (evt.getSlot() == profile$challenges$slot5) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievements(profile);
            } else if (evt.getSlot() == profile$aslot) {
              EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 2.0F);
            } else if (evt.getSlot() == profile$sslot){
              EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 2.0F);
            }
          }
        }
      }
    }
  }
  private String getAvailableLanguages() {
    KConfig config = Core.getInstance().getConfig("LANGUAGES", "languages");

    // StringBuilder para criar a lista formatada
    StringBuilder languagesList = new StringBuilder();

    // Itera por todas as linguagens configuradas
    for (String key : config.getKeys(false)) {
      String languageName = config.getString(key + ".name");
      languagesList.append("    §f▪ ").append(languageName).append("\n");
    }

    // Retorna a lista formatada, removendo a última quebra de linha
    return languagesList.toString().trim();
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

  private String getTranslatedText(String key, Profile profile) {
    GLanguage languageId = LangAPI.getLanguageId(profile);
    return (languageId.getId() == 1) ? getFieldValue(EN_US.class, key) : getFieldValue(PT_BR.class, key);
  }

  private int getTranslatedInt(String key, Profile profile) {
    GLanguage languageId = LangAPI.getLanguageId(profile);
    return (languageId.getId() == 1) ? getFieldValueAsInt(EN_US.class, key) : getFieldValueAsInt(PT_BR.class, key);
  }
  private boolean getTranslatedBoolean(String key, Profile profile) {
    GLanguage languageId = LangAPI.getLanguageId(profile);
    return (languageId.getId() == 1) ? getFieldValueAsBoolean(EN_US.class, key) : getFieldValueAsBoolean(PT_BR.class, key);
  }

  private boolean getFieldValueAsBoolean(Class<?> clazz, String key) {
    try {
      return (boolean) clazz.getField(key).get(null);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  private String getFieldValue(Class<?> clazz, String key) {
    try {
      return (String) clazz.getField(key).get(null);
    } catch (Exception e) {
      e.printStackTrace();
      return "§cTranslation missing!";
    }
  }

  private int getFieldValueAsInt(Class<?> clazz, String key) {
    try {
      return (int) clazz.getField(key).get(null);
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }
}
