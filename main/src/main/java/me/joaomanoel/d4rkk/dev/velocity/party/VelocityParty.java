package me.joaomanoel.d4rkk.dev.velocity.party;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.joaomanoel.d4rkk.dev.party.Party;
import me.joaomanoel.d4rkk.dev.party.PartyPlayer;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.velocity.LanguageVelocity;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import me.joaomanoel.d4rkk.dev.velocity.channels.VelocityChannels;
import me.joaomanoel.d4rkk.dev.velocity.manager.VelocityManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

import static me.joaomanoel.d4rkk.dev.party.PartyRole.LEADER;

@SuppressWarnings("unchecked")
public class VelocityParty extends Party {

    private final Map<String, String> memberServers = new HashMap<>();
    private boolean leaderInGame = false;
    private String leaderGameServer = null;

    public VelocityParty(String leader, int slots) {
        super(leader, slots);
        this.sendData();
    }

    @Override
    public void delete() {
        this.sendData("delete", "true");
        VelocityPartyManager.listParties().remove(this);
        this.destroy();
    }

    @Override
    public void transfer(String name) {
        PartyPlayer newLeader = this.getPlayer(name);
        this.sendData("newLeader", newLeader.getName());
        this.leader.setRole(newLeader.getRole());
        newLeader.setRole(LEADER);
        this.leader = newLeader;

        this.broadcast(LanguageVelocity.party$transfer_leader
                .replace("{player}", Role.getPrefixed(newLeader.getName())));
    }

    @Override
    public void join(String member) {
        super.join(member);
        this.sendData();

        // Se o líder já está em jogo, puxa o novo membro também
        if (leaderInGame && leaderGameServer != null) {
            Player player = VelocityManager.getPlayer(member);
            if (player != null) {
                Optional<RegisteredServer> targetServer = VelocityPlugin.getInstance()
                        .getServer()
                        .getServer(leaderGameServer);

                if (targetServer.isPresent()) {
                    player.createConnectionRequest(targetServer.get()).fireAndForget();
                    Component message = LegacyComponentSerializer.legacySection()
                            .deserialize("§aVocê foi puxado para o jogo do líder da party!");
                    player.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void leave(String member) {
        String leader = this.getLeader();
        if (this.members.size() == 1) {
            this.delete();
            return;
        }

        this.members.removeIf(pp -> pp.getName().equalsIgnoreCase(member));
        this.memberServers.remove(member);
        this.sendData("remove", member);

        if (leader.equals(member)) {
            this.sendData("newLeader", this.members.get(0).getName());
            this.leader = this.members.get(0);
            this.leader.setRole(LEADER);

            this.broadcast(LanguageVelocity.party$new_leader
                    .replace("{player}", Role.getPrefixed(this.leader.getName())));
        }

        this.broadcast(LanguageVelocity.party$member_left
                .replace("{player}", Role.getPrefixed(member)));
    }

    @Override
    public void kick(String member) {
        super.kick(member);
        this.memberServers.remove(member);
        this.sendData("remove", member);

        this.broadcast(LanguageVelocity.party$member_kicked
                .replace("{player}", Role.getPrefixed(member)));
    }

    public void onLeaderServerChange(String serverName) {
        memberServers.put(this.getLeader(), serverName);
    }

    public void onLeaderJoinGame(String serverName) {
        this.leaderInGame = true;
        this.leaderGameServer = serverName;

        Optional<RegisteredServer> targetServer = VelocityPlugin.getInstance()
                .getServer()
                .getServer(serverName);

        if (targetServer.isPresent()) {
            for (PartyPlayer member : this.members) {
                if (!member.equals(this.leader)) {
                    Player player = VelocityManager.getPlayer(member.getName());
                    if (player != null) {
                        String currentServer = player.getCurrentServer()
                                .map(s -> s.getServerInfo().getName())
                                .orElse(null);

                        // Só puxa se não estiver no mesmo servidor
                        if (currentServer == null || !currentServer.equals(serverName)) {
                            player.createConnectionRequest(targetServer.get()).fireAndForget();
                            Component message = LegacyComponentSerializer.legacySection()
                                    .deserialize("§a" + Role.getPrefixed(this.getLeader()) +
                                            " §aentrou em um jogo! Você foi puxado automaticamente.");
                            player.sendMessage(message);
                        }
                    }
                }
            }
        }
    }

    public void onLeaderLeaveGame() {
        this.leaderInGame = false;
        this.leaderGameServer = null;
    }

    public boolean isLeaderInGame() {
        return this.leaderInGame;
    }

    public void sendData(RegisteredServer server) {
        this.sendData(null, null, Collections.singleton(server));
    }

    private void sendData() {
        this.sendData(null, null);
    }

    private void sendData(String extraKey, String extraValue) {
        this.sendData(extraKey, extraValue,
                VelocityPlugin.getInstance().getServer().getAllServers());
    }

    private void sendData(String extraKey, String extraValue, Collection<RegisteredServer> servers) {
        JSONObject changes = new JSONObject();
        changes.put("leader", this.leader.getName());
        if (extraKey != null) {
            changes.put(extraKey, extraValue);
        }
        JSONArray members = new JSONArray();
        listMembers().forEach(member -> members.add(member.getName()));
        changes.put("members", members);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PARTY");
        out.writeUTF(changes.toString());

        byte[] data = out.toByteArray();
        servers.forEach(server -> {
            server.sendPluginMessage(
                    VelocityChannels.MAIN_CHANNEL,
                    data
            );
        });
    }

    public void summonMembers(RegisteredServer serverInfo) {
        this.summonMembers(serverInfo,
                this.members.stream().map(PartyPlayer::getName).collect(Collectors.toList()),
                true);
    }

    private void summonMembers(RegisteredServer serverInfo, Collection<String> members, boolean warn) {
        if (serverInfo == null) {
            Player leader = VelocityManager.getPlayer(this.getLeader());
            if (leader != null && leader.getCurrentServer().isPresent()) {
                serverInfo = leader.getCurrentServer().get().getServer();
            }
        }

        if (serverInfo != null) {
            String leader = Role.getPrefixed(this.getLeader());
            RegisteredServer finalServerInfo = serverInfo;

            members.forEach(member -> {
                Player player = VelocityManager.getPlayer(member);
                if (player != null) {
                    String currentServer = player.getCurrentServer()
                            .map(s -> s.getServerInfo().getName())
                            .orElse(null);

                    if (currentServer == null || !currentServer.equals(finalServerInfo.getServerInfo().getName())) {
                        player.createConnectionRequest(finalServerInfo).fireAndForget();

                        if (warn) {
                            Component message = LegacyComponentSerializer.legacySection()
                                    .deserialize(LanguageVelocity.party$pull_members
                                            .replace("{leader}", leader));
                            player.sendMessage(message);
                        }
                    }
                }
            });
        }
    }
}