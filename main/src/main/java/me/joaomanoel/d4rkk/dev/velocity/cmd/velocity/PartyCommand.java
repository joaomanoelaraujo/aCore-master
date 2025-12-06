package me.joaomanoel.d4rkk.dev.velocity.cmd.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.velocity.LanguageVelocity;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import me.joaomanoel.d4rkk.dev.velocity.manager.VelocityManager;
import me.joaomanoel.d4rkk.dev.velocity.party.VelocityParty;
import me.joaomanoel.d4rkk.dev.velocity.party.VelocityPartyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.stream.Collectors;

import static me.joaomanoel.d4rkk.dev.party.PartyRole.LEADER;

public class PartyCommand implements SimpleCommand {

    private final VelocityPlugin plugin;

    public PartyCommand(VelocityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!(sender instanceof Player)) {
            sender.sendMessage(deserialize(LanguageVelocity.general$only_players));
            return;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(deserialize(LanguageVelocity.party$help));
            return;
        }

        String action = args[0];

        if (action.equalsIgnoreCase("open")) {
            handleOpen(player);
        } else if (action.equalsIgnoreCase("close")) {
            handleClose(player);
        } else if (action.equalsIgnoreCase("join")) {
            handleJoin(player, args);
        } else if (action.equalsIgnoreCase("accept")) {
            handleAccept(player, args);
        } else if (action.equalsIgnoreCase("help")) {
            player.sendMessage(deserialize(LanguageVelocity.party$help));
        } else if (action.equalsIgnoreCase("summon")) {
            handleSummon(player);
        } else if (action.equalsIgnoreCase("delete")) {
            handleDelete(player);
        } else if (action.equalsIgnoreCase("kick")) {
            handleKick(player, args);
        } else if (action.equalsIgnoreCase("info")) {
            handleInfo(player);
        } else if (action.equalsIgnoreCase("deny")) {
            handleDeny(player, args);
        } else if (action.equalsIgnoreCase("leave")) {
            handleLeave(player);
        } else if (action.equalsIgnoreCase("transfer")) {
            handleTransfer(player, args);
        } else {
            handleInvite(player, action, args);
        }
    }

    private void handleOpen(Player player) {
        VelocityParty party = VelocityPartyManager.getMemberParty(player.getUsername());
        if (party == null) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_in_party));
            return;
        }

        if (!party.isLeader(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_leader));
            return;
        }

        if (party.isOpen()) {
            player.sendMessage(deserialize(LanguageVelocity.party$already_public));
            return;
        }

        party.setIsOpen(true);
        player.sendMessage(deserialize(LanguageVelocity.party$opened));
    }

    private void handleClose(Player player) {
        VelocityParty party = VelocityPartyManager.getMemberParty(player.getUsername());
        if (party == null) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_in_party));
            return;
        }

        if (!party.isLeader(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_leader));
            return;
        }

        if (!party.isOpen()) {
            player.sendMessage(deserialize(LanguageVelocity.party$already_private));
            return;
        }

        party.setIsOpen(false);
        player.sendMessage(deserialize(LanguageVelocity.party$closed));
    }

    private void handleJoin(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(deserialize(LanguageVelocity.party$usage_join));
            return;
        }

        String target = args[1];
        if (target.equalsIgnoreCase(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.party$cannot_join_self));
            return;
        }

        VelocityParty party = VelocityPartyManager.getMemberParty(player.getUsername());
        if (party != null) {
            player.sendMessage(deserialize(LanguageVelocity.party$already_in_party));
            return;
        }

        party = VelocityPartyManager.getLeaderParty(target);
        if (party == null) {
            String message = LanguageVelocity.party$target_not_leader
                    .replace("{player}", VelocityManager.getCurrent(target));
            player.sendMessage(deserialize(message));
            return;
        }

        target = party.getName(target);
        if (!party.isOpen()) {
            String message = LanguageVelocity.party$party_closed
                    .replace("{player}", VelocityManager.getCurrent(target));
            player.sendMessage(deserialize(message));
            return;
        }

        if (!party.canJoin()) {
            String message = LanguageVelocity.party$party_full
                    .replace("{player}", VelocityManager.getCurrent(target));
            player.sendMessage(deserialize(message));
            return;
        }

        party.join(player.getUsername());
        String message = LanguageVelocity.party$joined_party
                .replace("{player}", Role.getPrefixed(target));
        player.sendMessage(deserialize(message));
    }

    private void handleAccept(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(deserialize(LanguageVelocity.party$usage_accept));
            return;
        }

        String target = args[1];
        if (target.equalsIgnoreCase(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.party$cannot_accept_self));
            return;
        }

        VelocityParty party = VelocityPartyManager.getMemberParty(player.getUsername());
        if (party != null) {
            player.sendMessage(deserialize(LanguageVelocity.party$already_in_party));
            return;
        }

        party = VelocityPartyManager.getLeaderParty(target);
        if (party == null) {
            String message = LanguageVelocity.party$target_not_leader
                    .replace("{player}", VelocityManager.getCurrent(target));
            player.sendMessage(deserialize(message));
            return;
        }

        target = party.getName(target);
        if (!party.isInvited(player.getUsername())) {
            String message = LanguageVelocity.party$not_invited
                    .replace("{player}", VelocityManager.getCurrent(target));
            player.sendMessage(deserialize(message));
            return;
        }

        if (!party.canJoin()) {
            String message = LanguageVelocity.party$party_full
                    .replace("{player}", VelocityManager.getCurrent(target));
            player.sendMessage(deserialize(message));
            return;
        }

        party.join(player.getUsername());
        String message = LanguageVelocity.party$joined_party
                .replace("{player}", Role.getPrefixed(target));
        player.sendMessage(deserialize(message));
    }

    private void handleSummon(Player player) {
        VelocityParty party = VelocityPartyManager.getMemberParty(player.getUsername());
        if (party == null) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_in_party));
            return;
        }

        if (!party.isLeader(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_leader));
            return;
        }

        player.getCurrentServer().ifPresent(server -> {
            party.summonMembers(server.getServer());
            player.sendMessage(deserialize(LanguageVelocity.party$summoned));
        });
    }

    private void handleDelete(Player player) {
        VelocityParty party = VelocityPartyManager.getMemberParty(player.getUsername());
        if (party == null) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_in_party));
            return;
        }

        if (!party.isLeader(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_leader));
            return;
        }

        String message = LanguageVelocity.party$deleted
                .replace("{leader}", Role.getPrefixed(player.getUsername()));
        party.broadcast(message, true);
        party.delete();
        player.sendMessage(deserialize(LanguageVelocity.party$party_deleted));
    }

    private void handleKick(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(deserialize(LanguageVelocity.party$usage_kick));
            return;
        }

        VelocityParty party = VelocityPartyManager.getLeaderParty(player.getUsername());
        if (party == null) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_party_leader));
            return;
        }

        String target = args[1];
        if (target.equalsIgnoreCase(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.party$cannot_kick_self));
            return;
        }

        if (!party.isMember(target)) {
            player.sendMessage(deserialize(LanguageVelocity.party$player_not_in_party));
            return;
        }

        target = party.getName(target);
        party.kick(target);
        String message = LanguageVelocity.party$player_kicked
                .replace("{leader}", Role.getPrefixed(player.getUsername()))
                .replace("{player}", Role.getPrefixed(target));
        party.broadcast(message);
    }

    private void handleInfo(Player player) {
        VelocityParty party = VelocityPartyManager.getMemberParty(player.getUsername());
        if (party == null) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_in_party));
            return;
        }

        List<String> members = party.listMembers().stream()
                .filter(pp -> pp.getRole() != LEADER)
                .map(pp -> (pp.isOnline() ? "§a" : "§c") + pp.getName())
                .collect(Collectors.toList());

        String message = LanguageVelocity.party$info
                .replace("{leader}", Role.getPrefixed(party.getLeader()))
                .replace("{public}", party.isOpen() ? "§aYes" : "§cNo")
                .replace("{current}", String.valueOf(party.listMembers().size()))
                .replace("{max}", String.valueOf(party.getSlots()))
                .replace("{members}", StringUtils.join(members, "§7, "));
        player.sendMessage(deserialize(message));
    }

    private void handleDeny(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(deserialize(LanguageVelocity.party$usage_deny));
            return;
        }

        String target = args[1];
        if (target.equalsIgnoreCase(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.party$cannot_deny_self));
            return;
        }

        VelocityParty party = VelocityPartyManager.getMemberParty(player.getUsername());
        if (party != null) {
            player.sendMessage(deserialize(LanguageVelocity.party$already_in_party));
            return;
        }

        party = VelocityPartyManager.getLeaderParty(target);
        if (party == null) {
            String message = LanguageVelocity.party$target_not_leader
                    .replace("{player}", VelocityManager.getCurrent(target));
            player.sendMessage(deserialize(message));
            return;
        }

        target = party.getName(target);
        if (!party.isInvited(player.getUsername())) {
            String message = LanguageVelocity.party$not_invited
                    .replace("{player}", VelocityManager.getCurrent(target));
            player.sendMessage(deserialize(message));
            return;
        }

        party.reject(player.getUsername());
        String message = LanguageVelocity.party$denied_invitation
                .replace("{player}", Role.getPrefixed(target));
        player.sendMessage(deserialize(message));
    }

    private void handleLeave(Player player) {
        VelocityParty party = VelocityPartyManager.getMemberParty(player.getUsername());
        if (party == null) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_in_party));
            return;
        }

        party.leave(player.getUsername());
        player.sendMessage(deserialize(LanguageVelocity.party$left_party));
    }

    private void handleTransfer(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(deserialize(LanguageVelocity.party$usage_transfer));
            return;
        }

        VelocityParty party = VelocityPartyManager.getLeaderParty(player.getUsername());
        if (party == null) {
            player.sendMessage(deserialize(LanguageVelocity.party$not_party_leader));
            return;
        }

        String target = args[1];
        if (target.equalsIgnoreCase(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.party$cannot_transfer_self));
            return;
        }

        if (!party.isMember(target)) {
            player.sendMessage(deserialize(LanguageVelocity.party$player_not_in_party));
            return;
        }

        target = party.getName(target);
        party.transfer(target);
        String message = LanguageVelocity.party$leadership_transferred
                .replace("{old_leader}", Role.getPrefixed(player.getUsername()))
                .replace("{new_leader}", Role.getPrefixed(target));
        party.broadcast(message);
    }

    private void handleInvite(Player player, String action, String[] args) {
        if (action.equalsIgnoreCase("invite")) {
            if (args.length == 1) {
                player.sendMessage(deserialize(LanguageVelocity.party$usage_invite));
                return;
            }
            action = args[1];
        }

        Player target = plugin.getServer().getPlayer(action).orElse(null);
        if (target == null) {
            player.sendMessage(deserialize(LanguageVelocity.general$user_not_found));
            return;
        }

        action = target.getUsername();
        if (action.equalsIgnoreCase(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.party$cannot_invite_self));
            return;
        }

        VelocityParty party = VelocityPartyManager.getMemberParty(player.getUsername());
        if (party == null) {
            party = VelocityPartyManager.createParty(player);
        }

        if (!party.isLeader(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.party$only_leader_invite));
            return;
        }

        if (!party.canJoin()) {
            player.sendMessage(deserialize(LanguageVelocity.party$party_full
                    .replace("{player}", "Your Party")));
            return;
        }

        if (party.isInvited(action)) {
            String message = LanguageVelocity.party$already_invited
                    .replace("{player}", VelocityManager.getCurrent(action));
            player.sendMessage(deserialize(message));
            return;
        }

        if (VelocityPartyManager.getMemberParty(action) != null) {
            String message = LanguageVelocity.party$target_in_party
                    .replace("{player}", VelocityManager.getCurrent(action));
            player.sendMessage(deserialize(message));
            return;
        }

        party.invite(target);
        String message = LanguageVelocity.party$invitation_sent
                .replace("{player}", Role.getPrefixed(action));
        player.sendMessage(deserialize(message));
    }

    private Component deserialize(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text);
    }
}