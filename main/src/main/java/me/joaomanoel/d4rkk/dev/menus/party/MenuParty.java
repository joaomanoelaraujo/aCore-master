package me.joaomanoel.d4rkk.dev.menus.party;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.languages.LangAPI;
import me.joaomanoel.d4rkk.dev.languages.translates.EN_US;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.bukkit.BukkitParty;
import me.joaomanoel.d4rkk.dev.bukkit.BukkitPartyManager;
import me.joaomanoel.d4rkk.dev.bungee.Bungee;
import me.joaomanoel.d4rkk.dev.bungee.party.BungeeParty;
import me.joaomanoel.d4rkk.dev.bungee.party.BungeePartyManager;
import me.joaomanoel.d4rkk.dev.libraries.menu.UpdatablePlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.MenuProfile;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static me.joaomanoel.d4rkk.dev.languages.translates.EN_US.*;

public class MenuParty extends UpdatablePlayerMenu {

  private static final SimpleDateFormat SDF = new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm",
          Locale.forLanguageTag("en-US"));

  public MenuParty(Profile profile) {
    super(profile.getPlayer(), LangAPI.getTranslatedText("menu$ptitle", profile), menu$prows);

    this.register(Core.getInstance(), 8);
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
            if (evt.getSlot() == party$cslot) {
              this.player.sendMessage(LangAPI.getTranslatedText("player$enter", profile));
              this.player.closeInventory();

              Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
                Listener chatListener = new Listener() {
                  @EventHandler
                  public void onPlayerChat(AsyncPlayerChatEvent chatEvent) {
                    if (chatEvent.getPlayer().equals(player)) {
                      String playerName = chatEvent.getMessage();
                      Player targetPlayer = Bukkit.getPlayer(playerName);
                      if (targetPlayer != null) {
                        invitePlayer(targetPlayer.getName());
                      } else {
                        player.sendMessage(LangAPI.getTranslatedText("party$player_not_found", profile));
                      }

                      chatEvent.setCancelled(true);
                      HandlerList.unregisterAll(this);
                    }
                  }
                };
                Bukkit.getPluginManager().registerEvents(chatListener, Core.getInstance());
              });
            } else if (evt.getSlot() == party$uslot){
              BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());
              if (party != null) {
                party.delete();
                player.closeInventory();
              }
            } else if (evt.getSlot() == party$pslot){
             checkAndExecute(player);
            } else if (evt.getSlot() == party$kslot) {
              BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());
              if (party == null) {
                return;
              }

              if (party.getLeader() == null) {
                return;
              }

              if (party.getLeader().equals(player.getName())) {
                new MenuKick(profile);
              }
            } else if (evt.getSlot() == profile$slot){
              new MenuProfile(profile);
            } else if (evt.getSlot() == party$inviteslot) {
              BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());
              if (party == null) {
                return;
              }

              if (party.getLeader() == null) {
                return;
              }

              if (party.getLeader().equals(player.getName())) {


                this.player.sendMessage(LangAPI.getTranslatedText("player$enter", profile));
                this.player.closeInventory();

                Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
                  Listener chatListener = new Listener() {
                    @EventHandler
                    public void onPlayerChat(AsyncPlayerChatEvent chatEvent) {
                      if (chatEvent.getPlayer().equals(player)) {
                        String playerName = chatEvent.getMessage();
                        Player targetPlayer = Bukkit.getPlayer(playerName);
                        if (targetPlayer != null) {
                          invitePlayer(targetPlayer.getName());
                        } else {
                          player.sendMessage(LangAPI.getTranslatedText("party$player_not_found", profile));
                        }

                        chatEvent.setCancelled(true);
                        HandlerList.unregisterAll(this);
                      }
                    }
                  };
                  Bukkit.getPluginManager().registerEvents(chatListener, Core.getInstance());
                });
              }
            }
          }
        }
      }
    }
  }

  public void cancel() {
    HandlerList.unregisterAll(this);
  }

  @Override
  public void update() {

    if (player == null || !player.isOnline()) {
      this.cancel();
      return;
    }

    Profile profile = Profile.getProfile(player.getName());
    if (profile == null) {
      this.cancel();
      return;
    }

    this.setItem(profile$slot, BukkitUtils.putProfileOnSkull(this.player,
            BukkitUtils.deserializeItemStack(EN_US.profile$menu$profile
                    .replace("{rank}", Role.getRoleByName(profile.getDataContainer("aCoreProfile", "role").getAsString()).getName())
                    .replace("{cash}", StringUtils.formatNumber(profile.getStats("aCoreProfile", "cash")))
                    .replace("{created}", SDF.format(profile.getDataContainer("aCoreProfile", "created").getAsLong()))
                    .replace("{last}", SDF.format(profile.getDataContainer("aCoreProfile", "lastlogin").getAsLong()))
            )
    ));
    this.setItem(profile$fslot, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("profile$mfriends", profile)));

    this.setItem(profile$pslot, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("profile$mparty", profile)));

    if (menuprofile$glass) {
      for (int i = 9; i <= 17; i++) {
        this.setItem(i, BukkitUtils.deserializeItemStack(menuprofile$color_party));
      }
    }

    //todo: if party is null

    BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());

    if (party == null) {
      party = BukkitPartyManager.getMemberParty(player.getName());
      if (party == null) {
        this.setItem(party$cslot, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("party$create", profile)));
      } else {
        displayPartyMembers();
      }
    } else if (party.getLeader().equals(player.getName())) {

      this.setItem(party$islot, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("party$invite", profile)));
      this.setItem(party$kslot, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("party$kick", profile)));
      this.setItem(party$pslot, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("party$push", profile)));
      this.setItem(party$uslot, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("party$undo", profile)));
      this.setItem(party$sslot, BukkitUtils.deserializeItemStack(LangAPI.getTranslatedText("party$search", profile)));

      displayPartyMembers();
    } else if (party.getMembers().contains(player.getName())) {
      displayPartyMembers();
    }
  }

  public void displayPartyMembers() {
    BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());

    if (party == null) {
      return;
    }

    int slot = party.isLeader(player.getName()) ? 27 : 18;

    for (String memberName : party.getMembers()) {
      Player memberPlayer = Bukkit.getPlayerExact(memberName);

      String status = (memberPlayer != null && memberPlayer.isOnline()) ? party$online : party$offline;
      String role = party.isLeader(memberName) ? party$role : party$rmember;
      String guild = party$guild;
      String itemData = party$member
              .replace("%name%", memberName)      // Substitui %name% pelo nome do jogador
              .replace("%role%", role) // Substitui %role% pelo prefixo do cargo
              .replace("%status%", status)        // Substitui %status% pelo status do jogador
              .replace("%guild%", guild);

      this.setItem(slot, BukkitUtils.deserializeItemStack(itemData));

      slot++;
      if (slot > 53) {
        break;
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

  public boolean isBungeeActive() {
    try {
      Class.forName("net.md_5.bungee.api.plugin.Plugin");
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  public void checkAndExecute(Player player) {
    if (isBungeeActive()) {
      if (!BukkitPartyManager.listParties().isEmpty()) {
        BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
        party.summonMembers(Bungee.getPlayerByName(player.getName()).getServer().getInfo());
      }
    }
  }

  private void invitePlayer(String targetName) {
    Player target = Bukkit.getPlayerExact(targetName);
    Profile profile = Profile.getProfile(player.getName());
    if (target == null) {
      player.sendMessage(LangAPI.getTranslatedText("party$player_not_found", profile));
      return;
    }

    if (targetName.equalsIgnoreCase(player.getName())) {
      player.sendMessage(LangAPI.getTranslatedText("party$cant_invite_self", profile));
      return;
    }

    BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
    if (party == null) {
      party = BukkitPartyManager.createParty(player);
    }

    if (!party.isLeader(player.getName())) {
      player.sendMessage(LangAPI.getTranslatedText("party$only_leader_invite", profile));
      return;
    }

    if (!party.canJoin()) {
      player.sendMessage(LangAPI.getTranslatedText("party$full_party", profile).replace("{player}", Manager.getCurrent(targetName)));
      return;
    }

    if (party.isInvited(targetName)) {
      player.sendMessage(LangAPI.getTranslatedText("party$already_invited", profile).replace("{player}", Manager.getCurrent(targetName)));
      return;
    }

    if (BukkitPartyManager.getMemberParty(targetName) != null) {
      player.sendMessage(LangAPI.getTranslatedText("party$already_in_party", profile));
      return;
    }

    party.invite(target);
    player.sendMessage(LangAPI.getTranslatedText("party$invited", profile).replace("{prefix}", Role.getPrefixed(targetName)));
  }

}