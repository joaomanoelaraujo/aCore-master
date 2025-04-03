package me.joaomanoel.d4rkk.dev.menus.party;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.bukkit.BukkitParty;
import me.joaomanoel.d4rkk.dev.bukkit.BukkitPartyManager;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import me.joaomanoel.d4rkk.dev.languages.LangAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static me.joaomanoel.d4rkk.dev.languages.translates.EN_US.*;

public class MenuKick extends PlayerMenu {

  public MenuKick(Profile profile) {
    super(profile.getPlayer(), LangAPI.getTranslatedText("kick$title$menu", profile), menu$kcrows);
    this.register(Core.getInstance());
    this.open();
    displayPartyMembers(profile);
  }

  // Exibe os membros da party no menu
  private void displayPartyMembers(Profile profile) {
    Player player = this.player;
    BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());

    if (party == null) {
      return;
    }

    int slot = 10; // Começa a exibição na slot 10
    for (String memberName : party.getMembers()) {
      if (memberName.equals(player.getName())) {
        continue; // Ignora o líder, não exibindo sua cabeça
      }

      Player memberPlayer = Bukkit.getPlayerExact(memberName);
      String status = (memberPlayer != null && memberPlayer.isOnline()) ? LangAPI.getTranslatedText("party$online", profile) : LangAPI.getTranslatedText("party$offline", profile);
      String role = party.isLeader(memberName) ? LangAPI.getTranslatedText("party$role", profile) : LangAPI.getTranslatedText("party$rmember", profile);
      String guild = LangAPI.getTranslatedText("party$guild", profile);
      String itemData = LangAPI.getTranslatedText("party$member", profile)
              .replace("%name%", memberName)      // Substitui %name% pelo nome do jogador
              .replace("%role%", role) // Substitui %role% pelo prefixo do cargo
              .replace("%status%", status)        // Substitui %status% pelo status do jogador
              .replace("%guild%", guild);

      this.setItem(slot, BukkitUtils.deserializeItemStack(itemData));
      slot++;

      if (slot > 53) break; // Evita ultrapassar o limite do inventário
    }
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
            String memberName = item.getItemMeta().getDisplayName().replace("§a", "");
            BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());

            if (party != null && party.isLeader(player.getName())) {
              party.kick(memberName);
              player.sendMessage(LangAPI.getTranslatedText("party$kickm", profile).replace("{member}", memberName));
              new MenuParty(profile); // Volta para o menu principal da party
            }

            EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F); // Toca um som de confirmação
          }
        }
      }
    }
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

  public void cancel() {
    HandlerList.unregisterAll(this);
  }
}
