package me.joaomanoel.d4rkk.dev.bungee.cmd;

import me.joaomanoel.d4rkk.dev.Manager;
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
      sender.sendMessage(TextComponent.fromLegacyText("§cOnly players can use this command."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText(
              " \n§6/p [message] §f- §7Communicate with party members.\n§6/party open §f- §7Make the party public.\n§6/party close §f- §7Make the party private.\n§6/party join [player] §f- §7Join a public party.\n§6/party accept [player] §f- §7Accept an invitation.\n§6/party help §f- §7Show this help message.\n§6/party invite [player] §f- §7Invite a player.\n§6/party delete §f- §7Delete the party.\n§6/party kick [player] §f- §7Kick a member.\n§6/party info §f- §7Information about your Party.\n§6/party deny [player] §f- §7Deny an invitation.\n§6/party leave §f- §7Leave the Party.\n§6/party transfer [player] §f- §7Transfer Party leadership to another member.\n "));
      return;
    }

    String action = args[0];
    if (action.equalsIgnoreCase("open")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou don't belong to a Party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not the Party Leader."));
        return;
      }

      if (party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§cYour party is already public."));
        return;
      }

      party.setIsOpen(true);
      player.sendMessage(TextComponent.fromLegacyText("§aYou've opened the party to any player."));
    } else if (action.equalsIgnoreCase("close")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou don't belong to a Party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not the Party Leader."));
        return;
      }

      if (!party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§cYour party is already private."));
        return;
      }

      party.setIsOpen(false);
      player.sendMessage(TextComponent.fromLegacyText("§cYou've closed the party to invited members only."));
    } else if (action.equalsIgnoreCase("join")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUse /party join [player]"));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou cannot join your own party."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou already belong to a Party."));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " is not a Party Leader."));
        return;
      }

      target = party.getName(target);
      if (!party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + "'s Party is closed to invited members only."));
        return;
      }

      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + "'s Party is full."));
        return;
      }

      party.join(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(" \n§aYou've joined " + Role.getPrefixed(target) + "'s Party!\n "));
    } else if (action.equalsIgnoreCase("accept")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUse /party accept [player]"));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou cannot accept invitations from yourself."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou already belong to a Party."));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " is not a Party Leader."));
        return;
      }

      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " did not invite you to their Party."));
        return;
      }

      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + "'s Party is full."));
        return;
      }

      party.join(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(" \n§aYou've joined " + Role.getPrefixed(target) + "'s Party!\n "));
    } else if (action.equalsIgnoreCase("help")) {
      player.sendMessage(TextComponent.fromLegacyText(
              " \n§6/p [message] §f- §7Communicate with party members.\n§6/party open §f- §7Make the party public.\n§6/party close §f- §7Make the party private.\n§6/party join [player] §f- §7Join a public party.\n§6/party accept [player] §f- §7Accept an invitation.\n§6/party help §f- §7Show this help message.\n§6/party invite [player] §f- §7Invite a player.\n§6/party delete §f- §7Delete the party.\n§6/party kick [player] §f- §7Kick a member.\n§6/party info §f- §7Information about your Party.\n§6/party deny [player] §f- §7Deny an invitation.\n§6/party leave §f- §7Leave the Party.\n§6/party transfer [player] §f- §7Transfer Party leadership to another member.\n "));
    } else if (action.equalsIgnoreCase("summon")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou don't belong to a Party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not the Party Leader."));
        return;
      }

      party.summonMembers(player.getServer().getInfo());
      player.sendMessage(TextComponent.fromLegacyText("§aYou've summoned all players to your server."));
    } else if (action.equalsIgnoreCase("delete")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou don't belong to a Party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not the Party Leader."));
        return;
      }

      party.broadcast(" \n" + Role.getPrefixed(player.getName()) + " §adeleted the Party!\n ", true);
      party.delete();
      player.sendMessage(TextComponent.fromLegacyText("§aYou've deleted the Party."));
    } else if (action.equalsIgnoreCase("kick")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUse /party kick [player]"));
        return;
      }

      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not a Party Leader."));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou cannot kick yourself."));
        return;
      }

      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§cThat player does not belong to your Party."));
        return;
      }

      target = party.getName(target);
      party.kick(target);
      party.broadcast(" \n" + Role.getPrefixed(player.getName()) + " §akicked " + Role.getPrefixed(target) + " §afrom the Party!\n ");
    } else if (action.equalsIgnoreCase("info")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou don't belong to a Party."));
        return;
      }

      List<String> members = party.listMembers().stream().filter(pp -> pp.getRole() != LEADER).map(pp -> (pp.isOnline() ? "§a" : "§c") + pp.getName()).collect(Collectors.toList());
      player.sendMessage(TextComponent.fromLegacyText(
              " \n§6Leader: " + Role.getPrefixed(party.getLeader()) + "\n§6Public: " + (party.isOpen() ? "§aYes" : "§cNo") + "\n§6Member Limit: §f" + party.listMembers()
                      .size() + "/" + party.getSlots() + "\n§6Members: " + StringUtils.join(members, "§7, ") + "\n "));
    } else if (action.equalsIgnoreCase("deny")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUse /party deny [player]"));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou cannot deny invitations from yourself."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou already belong to a Party."));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " is not a Party Leader."));
        return;
      }

      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " did not invite you to their Party."));
        return;
      }

      party.reject(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(" \n§aYou've denied the invitation from " + Role.getPrefixed(target) + "§a!\n "));
    } else if (action.equalsIgnoreCase("leave")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou don't belong to a Party."));
        return;
      }

      party.leave(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§aYou've left the Party!"));
    } else if (action.equalsIgnoreCase("transfer")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUse /party transfer [player]"));
        return;
      }

      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not a Party Leader."));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou cannot transfer Party leadership to yourself."));
        return;
      }

      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§cThat player does not belong to your Party."));
        return;
      }

      target = party.getName(target);
      party.transfer(target);
      party.broadcast(" \n" + Role.getPrefixed(player.getName()) + " §atransferred Party leadership to " + Role.getPrefixed(target) + "§a!\n ");
    } else {
      if (action.equalsIgnoreCase("invite")) {
        if (args.length == 1) {
          player.sendMessage(TextComponent.fromLegacyText("§cUse /party invite [player]"));
          return;
        }

        action = args[1];
      }

      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(action);
      if (target == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cUser not found."));
        return;
      }

      action = target.getName();
      if (action.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou cannot send invitations to yourself."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        party = BungeePartyManager.createParty(player);
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cOnly the Party Leader can send invitations!"));
        return;
      }

      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cYour Party is full."));
        return;
      }

      if (party.isInvited(action)) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou've already sent an invitation to " + Manager.getCurrent(action) + "."));
        return;
      }

      if (BungeePartyManager.getMemberParty(action) != null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(action) + " already belongs to a Party."));
        return;
      }

      party.invite(target);
      player.sendMessage(
              TextComponent.fromLegacyText(" \n" + Role.getPrefixed(action) + " §awas invited to the Party. They have 60 seconds to accept or deny this request.\n "));
    }
  }
}
