package me.joaomanoel.d4rkk.dev.bungee.cmd;

import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.bungee.LanguageBungee;
import me.joaomanoel.d4rkk.dev.bungee.party.BungeeParty;
import me.joaomanoel.d4rkk.dev.bungee.party.BungeePartyManager;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.stream.Collectors;

import static me.joaomanoel.d4rkk.dev.party.PartyRole.LEADER;

public class PartyCommand extends Commands {

  public PartyCommand() {
    super("party");
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText(LanguageBungee.general$only_players));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$help));
      return;
    }

    String action = args[0];
    if (action.equalsIgnoreCase("open")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_in_party));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_leader));
        return;
      }

      if (party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$already_public));
        return;
      }

      party.setIsOpen(true);
      player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$opened));

    } else if (action.equalsIgnoreCase("close")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_in_party));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_leader));
        return;
      }

      if (!party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$already_private));
        return;
      }

      party.setIsOpen(false);
      player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$closed));

    } else if (action.equalsIgnoreCase("join")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$usage_join));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$cannot_join_self));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$already_in_party));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        String message = LanguageBungee.party$target_not_leader.replace("{player}", Manager.getCurrent(target));
        player.sendMessage(TextComponent.fromLegacyText(message));
        return;
      }

      target = party.getName(target);
      if (!party.isOpen()) {
        String message = LanguageBungee.party$party_closed.replace("{player}", Manager.getCurrent(target));
        player.sendMessage(TextComponent.fromLegacyText(message));
        return;
      }

      if (!party.canJoin()) {
        String message = LanguageBungee.party$party_full.replace("{player}", Manager.getCurrent(target));
        player.sendMessage(TextComponent.fromLegacyText(message));
        return;
      }

      party.join(player.getName());
      String message = LanguageBungee.party$joined_party.replace("{player}", Role.getPrefixed(target));
      player.sendMessage(TextComponent.fromLegacyText(message));

    } else if (action.equalsIgnoreCase("accept")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$usage_accept));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$cannot_accept_self));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$already_in_party));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        String message = LanguageBungee.party$target_not_leader.replace("{player}", Manager.getCurrent(target));
        player.sendMessage(TextComponent.fromLegacyText(message));
        return;
      }

      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        String message = LanguageBungee.party$not_invited.replace("{player}", Manager.getCurrent(target));
        player.sendMessage(TextComponent.fromLegacyText(message));
        return;
      }

      if (!party.canJoin()) {
        String message = LanguageBungee.party$party_full.replace("{player}", Manager.getCurrent(target));
        player.sendMessage(TextComponent.fromLegacyText(message));
        return;
      }

      party.join(player.getName());
      String message = LanguageBungee.party$joined_party.replace("{player}", Role.getPrefixed(target));
      player.sendMessage(TextComponent.fromLegacyText(message));

    } else if (action.equalsIgnoreCase("help")) {
      player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$help));

    } else if (action.equalsIgnoreCase("summon")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_in_party));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_leader));
        return;
      }

      party.summonMembers(player.getServer().getInfo());
      player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$summoned));

    } else if (action.equalsIgnoreCase("delete")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_in_party));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_leader));
        return;
      }

      String message = LanguageBungee.party$deleted.replace("{leader}", Role.getPrefixed(player.getName()));
      party.broadcast(message, true);
      party.delete();
      player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$party_deleted));

    } else if (action.equalsIgnoreCase("kick")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$usage_kick));
        return;
      }

      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_party_leader));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$cannot_kick_self));
        return;
      }

      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$player_not_in_party));
        return;
      }

      target = party.getName(target);
      party.kick(target);
      String message = LanguageBungee.party$player_kicked
              .replace("{leader}", Role.getPrefixed(player.getName()))
              .replace("{player}", Role.getPrefixed(target));
      party.broadcast(message);

    } else if (action.equalsIgnoreCase("info")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_in_party));
        return;
      }

      List<String> members = party.listMembers().stream()
              .filter(pp -> pp.getRole() != LEADER)
              .map(pp -> (pp.isOnline() ? "§a" : "§c") + pp.getName())
              .collect(Collectors.toList());

      String message = LanguageBungee.party$info
              .replace("{leader}", Role.getPrefixed(party.getLeader()))
              .replace("{public}", party.isOpen() ? "§aYes" : "§cNo")
              .replace("{current}", String.valueOf(party.listMembers().size()))
              .replace("{max}", String.valueOf(party.getSlots()))
              .replace("{members}", StringUtils.join(members, "§7, "));
      player.sendMessage(TextComponent.fromLegacyText(message));

    } else if (action.equalsIgnoreCase("deny")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$usage_deny));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$cannot_deny_self));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$already_in_party));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        String message = LanguageBungee.party$target_not_leader.replace("{player}", Manager.getCurrent(target));
        player.sendMessage(TextComponent.fromLegacyText(message));
        return;
      }

      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        String message = LanguageBungee.party$not_invited.replace("{player}", Manager.getCurrent(target));
        player.sendMessage(TextComponent.fromLegacyText(message));
        return;
      }

      party.reject(player.getName());
      String message = LanguageBungee.party$denied_invitation.replace("{player}", Role.getPrefixed(target));
      player.sendMessage(TextComponent.fromLegacyText(message));

    } else if (action.equalsIgnoreCase("leave")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_in_party));
        return;
      }

      party.leave(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$left_party));

    } else if (action.equalsIgnoreCase("transfer")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$usage_transfer));
        return;
      }

      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$not_party_leader));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$cannot_transfer_self));
        return;
      }

      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$player_not_in_party));
        return;
      }

      target = party.getName(target);
      party.transfer(target);
      String message = LanguageBungee.party$leadership_transferred
              .replace("{old_leader}", Role.getPrefixed(player.getName()))
              .replace("{new_leader}", Role.getPrefixed(target));
      party.broadcast(message);

    } else {
      if (action.equalsIgnoreCase("invite")) {
        if (args.length == 1) {
          player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$usage_invite));
          return;
        }
        action = args[1];
      }

      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(action);
      if (target == null) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.general$user_not_found));
        return;
      }

      action = target.getName();
      if (action.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$cannot_invite_self));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        party = BungeePartyManager.createParty(player);
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$only_leader_invite));
        return;
      }

      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$party_full.replace("{player}", "Your Party")));
        return;
      }

      if (party.isInvited(action)) {
        String message = LanguageBungee.party$already_invited.replace("{player}", Manager.getCurrent(action));
        player.sendMessage(TextComponent.fromLegacyText(message));
        return;
      }

      if (BungeePartyManager.getMemberParty(action) != null) {
        String message = LanguageBungee.party$target_in_party.replace("{player}", Manager.getCurrent(action));
        player.sendMessage(TextComponent.fromLegacyText(message));
        return;
      }

      party.invite(target);
      String message = LanguageBungee.party$invitation_sent.replace("{player}", Role.getPrefixed(action));
      player.sendMessage(TextComponent.fromLegacyText(message));
    }
  }
}