package me.joaomanoel.d4rkk.dev.bungee.party;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.bungee.LanguageBungee;
import me.joaomanoel.d4rkk.dev.party.Party;
import me.joaomanoel.d4rkk.dev.party.PartyPlayer;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static me.joaomanoel.d4rkk.dev.party.PartyRole.LEADER;

@SuppressWarnings("unchecked")
public class BungeeParty extends Party {

  // ✅ NOVO: Rastreia em qual servidor cada membro está
  private final Map<String, String> memberServers = new HashMap<>();

  // ✅ NOVO: Rastreia se o líder está em jogo
  private boolean leaderInGame = false;
  private String leaderGameServer = null;

  public BungeeParty(String leader, int slots) {
    super(leader, slots);
    this.sendData();
  }

  @Override
  public void delete() {
    this.sendData("delete", "true");
    BungeePartyManager.listParties().remove(this);
    this.destroy();
  }

  @Override
  public void transfer(String name) {
    PartyPlayer newLeader = this.getPlayer(name);
    this.sendData("newLeader", newLeader.getName());
    this.leader.setRole(newLeader.getRole());
    newLeader.setRole(LEADER);
    this.leader = newLeader;

    this.broadcast(LanguageBungee.party$transfer_leader
            .replace("{player}", Role.getPrefixed(newLeader.getName())));
  }

  @Override
  public void join(String member) {
    super.join(member);
    this.sendData();

    // ✅ NOVO: Se o líder já está em jogo, puxa o novo membro também
    if (leaderInGame && leaderGameServer != null) {
      ProxiedPlayer player = (ProxiedPlayer) Manager.getPlayer(member);
      if (player != null) {
        ServerInfo targetServer = ProxyServer.getInstance().getServerInfo(leaderGameServer);
        if (targetServer != null) {
          player.connect(targetServer);
          player.sendMessage(TextComponent.fromLegacyText(
                  "§aVocê foi puxado para o jogo do líder da party!"
          ));
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
    this.memberServers.remove(member); // ✅ Remove do rastreamento
    this.sendData("remove", member);

    if (leader.equals(member)) {
      this.sendData("newLeader", this.members.get(0).getName());
      this.leader = this.members.get(0);
      this.leader.setRole(LEADER);

      this.broadcast(LanguageBungee.party$new_leader
              .replace("{player}", Role.getPrefixed(this.leader.getName())));
    }

    this.broadcast(LanguageBungee.party$member_left
            .replace("{player}", Role.getPrefixed(member)));
  }

  @Override
  public void kick(String member) {
    super.kick(member);
    this.memberServers.remove(member); // ✅ Remove do rastreamento
    this.sendData("remove", member);

    this.broadcast(LanguageBungee.party$member_kicked
            .replace("{player}", Role.getPrefixed(member)));
  }

  // ✅ NOVO: Método chamado quando o líder entra em um servidor
  public void onLeaderServerChange(String serverName) {
    memberServers.put(this.getLeader(), serverName);
  }

  // ✅ NOVO: Método chamado quando o líder entra em um jogo
  public void onLeaderJoinGame(String serverName) {
    this.leaderInGame = true;
    this.leaderGameServer = serverName;

    // Puxa todos os membros para o servidor do jogo
    ServerInfo targetServer = ProxyServer.getInstance().getServerInfo(serverName);
    if (targetServer != null) {
      for (PartyPlayer member : this.members) {
        if (!member.equals(this.leader)) {
          ProxiedPlayer player = (ProxiedPlayer) Manager.getPlayer(member.getName());
          if (player != null) {
            // Só puxa se não estiver no mesmo servidor
            if (player.getServer() == null || !player.getServer().getInfo().getName().equals(serverName)) {
              player.connect(targetServer);
              player.sendMessage(TextComponent.fromLegacyText(
                      "§a" + Role.getPrefixed(this.getLeader()) + " §aentrou em um jogo! Você foi puxado automaticamente."
              ));
            }
          }
        }
      }
    }
  }

  // ✅ NOVO: Método chamado quando o líder sai de um jogo
  public void onLeaderLeaveGame() {
    this.leaderInGame = false;
    this.leaderGameServer = null;
  }

  // ✅ NOVO: Verifica se o líder está em jogo
  public boolean isLeaderInGame() {
    return this.leaderInGame;
  }

  public void sendData(ServerInfo serverInfo) {
    this.sendData(null, null, Collections.singleton(serverInfo));
  }

  private void sendData() {
    this.sendData(null, null);
  }

  private void sendData(String extraKey, String extraValue) {
    this.sendData(extraKey, extraValue, ProxyServer.getInstance().getServers().values());
  }

  private void sendData(String extraKey, String extraValue, Collection<ServerInfo> serverInfos) {
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
    serverInfos.forEach(info -> info.sendData("aCore", out.toByteArray()));
  }

  public void summonMembers(ServerInfo serverInfo) {
    this.summonMembers(serverInfo, this.members.stream().map(PartyPlayer::getName).collect(Collectors.toList()), true);
  }

  private void summonMembers(ServerInfo serverInfo, Collection<String> members, boolean warn) {
    if (serverInfo == null) {
      ProxiedPlayer leader = (ProxiedPlayer) Manager.getPlayer(this.getLeader());
      serverInfo = leader != null && leader.getServer() != null ? leader.getServer().getInfo() : null;
    }

    if (serverInfo != null) {
      String leader = Role.getPrefixed(this.getLeader());
      ServerInfo finalServerInfo = serverInfo;
      members.forEach(member -> {
        ProxiedPlayer player = (ProxiedPlayer) Manager.getPlayer(member);
        if (player != null && (player.getServer() == null || !player.getServer().getInfo().getName().equals(finalServerInfo.getName()))) {
          player.connect(finalServerInfo);
          if (warn) {
            player.sendMessage(TextComponent.fromLegacyText(
                    LanguageBungee.party$pull_members.replace("{leader}", leader)
            ));
          }
        }
      });
    }
  }
}