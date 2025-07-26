package me.joaomanoel.d4rkk.dev.cmd;

import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.bukkit.BukkitParty;
import me.joaomanoel.d4rkk.dev.bukkit.BukkitPartyManager;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.party.PartyRole;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PartyCommand extends Commands {

  public PartyCommand() {
    super("party", "p");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(LanguageAPI.getConfig().getString("party.only_players"));
      return;
    }

    Player player = (Player) sender;
    Profile profile = Profile.getProfile(player.getName());
    if (label.equalsIgnoreCase("p")) {
      if (args.length == 0) {
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.chat_usage"));
        return;
      }

      BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_in_party"));
        return;
      }

      party.broadcast(LanguageAPI.getConfig(profile).getString("party.chat_format")
              .replace("{prefix}", Role.getPrefixed(player.getName()))
              .replace("{message}", StringUtils.join(args, " ")));
    } else {
      if (args.length == 0) {
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.help_message"));
        return;
      }

      String action = args[0];
      if (action.equalsIgnoreCase("open")) {
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_in_party"));
          return;
        }

        if (!party.isLeader(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_leader"));
          return;
        }

        if (party.isOpen()) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.already_public"));
          return;
        }

        party.setIsOpen(true);
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.opened"));
      } else if (action.equalsIgnoreCase("close")) {
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_in_party"));
          return;
        }

        if (!party.isLeader(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_leader"));
          return;
        }

        if (!party.isOpen()) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.already_private"));
          return;
        }

        party.setIsOpen(false);
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.closed"));
      } else if (action.equalsIgnoreCase("join")) {
        if (args.length == 1) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.join_usage"));
          return;
        }

        String target = args[1];
        if (target.equalsIgnoreCase(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.cant_join_own"));
          return;
        }

        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party != null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.already_in_party"));
          return;
        }

        party = BukkitPartyManager.getLeaderParty(target);
        if (party == null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_leader_target").replace("{player}", Manager.getCurrent(target)));
          return;
        }

        target = party.getName(target);
        if (!party.isOpen()) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.closed_party").replace("{player}", Manager.getCurrent(target)));
          return;
        }

        if (!party.canJoin()) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.full_party").replace("{player}", Manager.getCurrent(target)));
          return;
        }

        party.join(player.getName());
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.joined").replace("{prefix}", Role.getPrefixed(target)));
      } else if (action.equalsIgnoreCase("accept")) {
        if (args.length == 1) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.accept_usage"));
          return;
        }

        String target = args[1];
        if (target.equalsIgnoreCase(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.cant_accept_own"));
          return;
        }

        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party != null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.already_in_party"));
          return;
        }

        party = BukkitPartyManager.getLeaderParty(target);
        if (party == null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_leader_target").replace("{player}", Manager.getCurrent(target)));
          return;
        }

        target = party.getName(target);
        if (!party.isInvited(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.no_invite").replace("{player}", Manager.getCurrent(target)));
          return;
        }

        if (!party.canJoin()) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.full_party").replace("{player}", Manager.getCurrent(target)));
          return;
        }

        party.join(player.getName());
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.joined").replace("{prefix}", Role.getPrefixed(target)));
      } else if (action.equalsIgnoreCase("help")) {
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.help_message"));
      } else if (action.equalsIgnoreCase("delete")) {
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_in_party"));
          return;
        }

        if (!party.isLeader(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_leader"));
          return;
        }

        party.broadcast(LanguageAPI.getConfig(profile).getString("party.deleted_broadcast").replace("{prefix}", Role.getPrefixed(player.getName())), true);
        party.delete();
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.deleted"));
      } else if (action.equalsIgnoreCase("kick")) {
        if (args.length == 1) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.kick_usage"));
          return;
        }

        BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());
        if (party == null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_party_leader"));
          return;
        }

        String target = args[1];
        if (target.equalsIgnoreCase(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.cant_kick_self"));
          return;
        }

        if (!party.isMember(target)) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_in_your_party"));
          return;
        }

        target = party.getName(target);
        party.kick(target);
        party.broadcast(LanguageAPI.getConfig(profile).getString("party.kicked_broadcast")
                .replace("{kicker}", Role.getPrefixed(player.getName()))
                .replace("{kicked}", Role.getPrefixed(target)));
      } else if (action.equalsIgnoreCase("info")) {
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_in_party"));
          return;
        }

        List<String> members = party.listMembers().stream()
                .filter(pp -> pp.getRole() != PartyRole.LEADER)
                .map(pp -> (pp.isOnline() ? "§a" : "§c") + pp.getName())
                .collect(Collectors.toList());

        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.info_format")
                .replace("{leader}", Role.getPrefixed(party.getLeader()))
                .replace("{public}", party.isOpen() ? "§aYes" : "§cNo")
                .replace("{members}", String.valueOf(party.listMembers().size()))
                .replace("{slots}", String.valueOf(party.getSlots()))
                .replace("{member_list}", StringUtils.join(members, "§7, ")));
      } else if (action.equalsIgnoreCase("deny")) {
        if (args.length == 1) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.deny_usage"));
          return;
        }

        String target = args[1];
        if (target.equalsIgnoreCase(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.cant_deny_self"));
          return;
        }

        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party != null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.already_in_party"));
          return;
        }

        party = BukkitPartyManager.getLeaderParty(target);
        if (party == null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_leader_target").replace("{player}", Manager.getCurrent(target)));
          return;
        }

        target = party.getName(target);
        if (!party.isInvited(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.no_invite").replace("{player}", Manager.getCurrent(target)));
          return;
        }

        party.reject(player.getName());
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.denied").replace("{prefix}", Role.getPrefixed(target)));
      } else if (action.equalsIgnoreCase("leave")) {
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_in_party"));
          return;
        }

        party.leave(player.getName());
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.left"));
      } else if (action.equalsIgnoreCase("transfer")) {
        if (args.length == 1) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.transfer_usage"));
          return;
        }

        BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());
        if (party == null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_party_leader"));
          return;
        }

        String target = args[1];
        if (target.equalsIgnoreCase(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.cant_transfer_self"));
          return;
        }

        if (!party.isMember(target)) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.not_in_your_party"));
          return;
        }

        target = party.getName(target);
        party.transfer(target);
        party.broadcast(LanguageAPI.getConfig(profile).getString("party.transferred_broadcast")
                .replace("{old_leader}", Role.getPrefixed(player.getName()))
                .replace("{new_leader}", Role.getPrefixed(target)));
      } else {
        if (action.equalsIgnoreCase("invite")) {
          if (args.length == 1) {
            player.sendMessage(LanguageAPI.getConfig(profile).getString("party.invite_usage"));
            return;
          }
          action = args[1];
        }

        Player target = Bukkit.getPlayerExact(action);
        if (target == null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.player_not_found"));
          return;
        }

        action = target.getName();
        if (action.equalsIgnoreCase(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.cant_invite_self"));
          return;
        }

        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          party = BukkitPartyManager.createParty(player);
        }

        if (!party.isLeader(player.getName())) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.only_leader_invite"));
          return;
        }

        if (!party.canJoin()) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.full_party").replace("{player}", Manager.getCurrent(action)));
          return;
        }

        if (party.isInvited(action)) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.already_invited").replace("{player}", Manager.getCurrent(action)));
          return;
        }

        if (BukkitPartyManager.getMemberParty(action) != null) {
          player.sendMessage(LanguageAPI.getConfig(profile).getString("party.already_in_party"));
          return;
        }

        party.invite(target);
        player.sendMessage(LanguageAPI.getConfig(profile).getString("party.invited").replace("{prefix}", Role.getPrefixed(action)));
      }
    }
  }
}