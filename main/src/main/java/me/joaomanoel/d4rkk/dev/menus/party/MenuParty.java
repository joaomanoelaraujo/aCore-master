package me.joaomanoel.d4rkk.dev.menus.party;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.bukkit.BukkitParty;
import me.joaomanoel.d4rkk.dev.bukkit.BukkitPartyManager;
import me.joaomanoel.d4rkk.dev.bungee.Bungee;
import me.joaomanoel.d4rkk.dev.bungee.party.BungeeParty;
import me.joaomanoel.d4rkk.dev.bungee.party.BungeePartyManager;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.libraries.menu.UpdatablePlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.MenuProfile;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
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

public class MenuParty extends UpdatablePlayerMenu {

  private static final SimpleDateFormat SDF = new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm",
          Locale.forLanguageTag("en-US"));

  public MenuParty(Profile profile) {
    super(profile.getPlayer(), LanguageAPI.getConfig(profile).getString("menu.ptitle"),
            LanguageAPI.getConfig(profile).getInt("menu.prows"));

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
            KConfig config = LanguageAPI.getConfig(profile);

            if (evt.getSlot() == config.getInt("party.cslot")) {
              this.player.sendMessage(config.getString("player.enter"));
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
                        player.sendMessage(config.getString("party.player_not_found"));
                      }

                      chatEvent.setCancelled(true);
                      HandlerList.unregisterAll(this);
                    }
                  }
                };
                Bukkit.getPluginManager().registerEvents(chatListener, Core.getInstance());
              });
            } else if (evt.getSlot() == config.getInt("party.uslot")) {
              BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());
              if (party != null) {
                party.delete();
                player.closeInventory();
              }
            } else if (evt.getSlot() == config.getInt("party.pslot")) {
              checkAndExecute(player);
            } else if (evt.getSlot() == config.getInt("party.kslot")) {
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
            } else if (evt.getSlot() == config.getInt("profile.slot")) {
              new MenuProfile(profile);
            } else if (evt.getSlot() == config.getInt("party.inviteslot")) {
              BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());
              if (party == null) {
                return;
              }

              if (party.getLeader() == null) {
                return;
              }

              if (party.getLeader().equals(player.getName())) {
                this.player.sendMessage(config.getString("player.enter"));
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
                          player.sendMessage(config.getString("party.player_not_found"));
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

    KConfig config = LanguageAPI.getConfig(profile);

    this.setItem(config.getInt("profile.slot"), BukkitUtils.putProfileOnSkull(this.player,
            BukkitUtils.deserializeItemStack(config.getString("profile.menu.profile")
                    .replace("{rank}", Role.getRoleByName(profile.getDataContainer("aCoreProfile", "role").getAsString()).getName())
                    .replace("{cash}", StringUtils.formatNumber(profile.getStats("aCoreProfile", "cash")))
                    .replace("{created}", SDF.format(profile.getDataContainer("aCoreProfile", "created").getAsLong()))
                    .replace("{last}", SDF.format(profile.getDataContainer("aCoreProfile", "lastlogin").getAsLong()))
            )
    ));

    this.setItem(config.getInt("profile.fslot"),
            BukkitUtils.deserializeItemStack(config.getString("profile.mfriends")));

    this.setItem(config.getInt("profile.pslot"),
            BukkitUtils.deserializeItemStack(config.getString("profile.mparty")));

    if (config.getBoolean("menuprofile.glass")) {
      for (int i = 9; i <= 17; i++) {
        this.setItem(i, BukkitUtils.deserializeItemStack(config.getString("menuprofile.color_party")));
      }
    }

    BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());

    if (party == null) {
      party = BukkitPartyManager.getMemberParty(player.getName());
      if (party == null) {
        this.setItem(config.getInt("party.cslot"),
                BukkitUtils.deserializeItemStack(config.getString("party.create")));
      } else {
        displayPartyMembers(profile);
      }
    } else if (party.getLeader().equals(player.getName())) {
      this.setItem(config.getInt("party.islot"),
              BukkitUtils.deserializeItemStack(config.getString("party.invite")));
      this.setItem(config.getInt("party.kslot"),
              BukkitUtils.deserializeItemStack(config.getString("party.kick")));
      this.setItem(config.getInt("party.pslot"),
              BukkitUtils.deserializeItemStack(config.getString("party.push")));
      this.setItem(config.getInt("party.uslot"),
              BukkitUtils.deserializeItemStack(config.getString("party.undo")));
      this.setItem(config.getInt("party.sslot"),
              BukkitUtils.deserializeItemStack(config.getString("party.search")));

      displayPartyMembers(profile);
    } else if (party.getMembers().contains(player.getName())) {
      displayPartyMembers(profile);
    }
  }

  public void displayPartyMembers(Profile profile) {
    BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());

    if (party == null) {
      return;
    }

    KConfig config = LanguageAPI.getConfig(profile);
    int slot = party.isLeader(player.getName()) ? 27 : 18;

    for (String memberName : party.getMembers()) {
      Player memberPlayer = Bukkit.getPlayerExact(memberName);

      String status = (memberPlayer != null && memberPlayer.isOnline()) ?
              config.getString("party.online") : config.getString("party.offline");
      String role = party.isLeader(memberName) ?
              config.getString("party.role") : config.getString("party.rmember");
      String guild = config.getString("party.guild");

      String itemData = config.getString("party.member")
              .replace("%name%", memberName)
              .replace("%role%", role)
              .replace("%status%", status)
              .replace("%guild%", guild);

      this.setItem(slot, BukkitUtils.deserializeItemStack(itemData));

      slot++;
      if (slot > 53) {
        break;
      }
    }
  }

  private void invitePlayer(String targetName) {
    Player target = Bukkit.getPlayerExact(targetName);
    Profile profile = Profile.getProfile(player.getName());
    KConfig config = LanguageAPI.getConfig(profile);

    if (target == null) {
      player.sendMessage(config.getString("party.player_not_found"));
      return;
    }

    if (targetName.equalsIgnoreCase(player.getName())) {
      player.sendMessage(config.getString("party.cant_invite_self"));
      return;
    }

    BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
    if (party == null) {
      party = BukkitPartyManager.createParty(player);
    }

    if (!party.isLeader(player.getName())) {
      player.sendMessage(config.getString("party.only_leader_invite"));
      return;
    }

    if (!party.canJoin()) {
      player.sendMessage(config.getString("party.full_party")
              .replace("{player}", Manager.getCurrent(targetName)));
      return;
    }

    if (party.isInvited(targetName)) {
      player.sendMessage(config.getString("party.already_invited")
              .replace("{player}", Manager.getCurrent(targetName)));
      return;
    }

    if (BukkitPartyManager.getMemberParty(targetName) != null) {
      player.sendMessage(config.getString("party.already_in_party"));
      return;
    }

    party.invite(target);
    player.sendMessage(config.getString("party.invited")
            .replace("{prefix}", Role.getPrefixed(targetName)));
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
}
