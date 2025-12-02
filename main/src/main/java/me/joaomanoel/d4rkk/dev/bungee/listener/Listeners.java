package me.joaomanoel.d4rkk.dev.bungee.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.joaomanoel.d4rkk.dev.bungee.Bungee;
import me.joaomanoel.d4rkk.dev.bungee.party.BungeeParty;
import me.joaomanoel.d4rkk.dev.bungee.party.BungeePartyManager;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.reflection.Accessors;
import me.joaomanoel.d4rkk.dev.reflection.acessors.FieldAccessor;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.Property;

import java.util.*;

public class Listeners implements Listener {

  private static final Map<String, Property[]> PROPERTY_BACKUP = new HashMap<>();
  private static final Map<String, Long> PROTECTION_LOBBY = new HashMap<>();
  private static final Map<String, Boolean> TELL_CACHE = new HashMap<>(), PROTECTION_CACHE = new HashMap<>();
  private static final FieldAccessor<Map> COMMAND_MAP = Accessors.getField(PluginManager.class, "commandMap", Map.class);

  @EventHandler
  public void onPlayerDisconnect(PlayerDisconnectEvent evt) {
    TELL_CACHE.remove(evt.getPlayer().getName().toLowerCase());
    PROTECTION_CACHE.remove(evt.getPlayer().getName().toLowerCase());
    PROTECTION_LOBBY.remove(evt.getPlayer().getName().toLowerCase());
    PROPERTY_BACKUP.remove(evt.getPlayer().getName().toLowerCase());
  }

  @EventHandler
  public void onPostLogin(PostLoginEvent evt) {
    TELL_CACHE.remove(evt.getPlayer().getName().toLowerCase());
    PROTECTION_CACHE.remove(evt.getPlayer().getName().toLowerCase());
  }

  @EventHandler
  public void onPluginMessage(PluginMessageEvent evt) {
    if (evt.getSender() instanceof ServerConnection && evt.getReceiver() instanceof ProxiedPlayer) {
      if (evt.getTag().equalsIgnoreCase("acore:main")) {
        ProxiedPlayer player = (ProxiedPlayer) evt.getReceiver();

        ByteArrayDataInput in = ByteStreams.newDataInput(evt.getData());
        String subChannel = in.readUTF();
        if (subChannel.equalsIgnoreCase("PARTY_LEADER_JOIN_GAME")) {
          BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
          if (party != null) {
            String serverName = player.getServer().getInfo().getName();
            party.onLeaderJoinGame(serverName);
          }
        }
        // ✅ NOVO: Detecta quando o líder sai de um jogo
        else if (subChannel.equalsIgnoreCase("PARTY_LEADER_LEAVE_GAME")) {
          BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
          if (party != null) {
            party.onLeaderLeaveGame();
          }
        }
        if (subChannel.equalsIgnoreCase("FAKE_SKIN")) {
          LoginResult profile = ((InitialHandler) player.getPendingConnection()).getLoginProfile();
          if (profile != null) {
            try {
              String[] data = in.readUTF().split(":");
              PROPERTY_BACKUP.put(player.getName().toLowerCase(), profile.getProperties());
              this.modifyProperties(profile, data);
            } catch (Exception ex) {
              Property[] properties = PROPERTY_BACKUP.remove(player.getName().toLowerCase());
              if (properties != null) {
                profile.setProperties(properties);
              }
            }
          }
        }
      }
    }
  }

  @EventHandler(priority = (byte) 128)
  public void onServerConnected(ServerConnectedEvent evt) {
    ProxiedPlayer player = evt.getPlayer();

    // ✅ NOVO: Notifica a party sobre mudança de servidor do líder
    BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
    if (party != null) {
      party.sendData(evt.getServer().getInfo());
      party.onLeaderServerChange(evt.getServer().getInfo().getName());
    }

    if (Bungee.isFake(player.getName())) {
      String skin = Bungee.getSkin(player.getName());
      // Send data of this player using a fake to the server to process.
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF("FAKE");
      out.writeUTF(player.getName());
      out.writeUTF(Bungee.getFake(player.getName()));
      out.writeUTF(StringUtils.stripColors(Bungee.getRole(player.getName()).getName()));
      out.writeUTF(skin);
      evt.getServer().sendData("acore:main", out.toByteArray());


      LoginResult profile = ((InitialHandler) player.getPendingConnection()).getLoginProfile();
      if (profile != null) {
        this.modifyProperties(profile, skin.split(":"));
      }
    }
  }

  @EventHandler(priority = (byte) 128)
  public void onChat(ChatEvent evt) {
    if (evt.getSender() instanceof ProxiedPlayer) {
      if (evt.isCommand()) {
        ProxiedPlayer player = (UserConnection) evt.getSender();
        String[] args = evt.getMessage().replace("/", "").split(" ");

        String command = args[0];
        if (COMMAND_MAP.get(ProxyServer.getInstance().getPluginManager()).containsKey("lobby") && command.equals("lobby") && this
                .hasProtectionLobby(player.getName().toLowerCase())) {
          long last = PROTECTION_LOBBY.getOrDefault(player.getName().toLowerCase(), 0L);
          if (last > System.currentTimeMillis()) {
            PROTECTION_LOBBY.remove(player.getName().toLowerCase());
            return;
          }

          evt.setCancelled(true);
          PROTECTION_LOBBY.put(player.getName().toLowerCase(), System.currentTimeMillis() + 3000);
          player.sendMessage(TextComponent.fromLegacyText("§aAre you sure? Use /lobby again to go back to the lobby."));
        } else if (COMMAND_MAP.get(ProxyServer.getInstance().getPluginManager()).containsKey("tell") && args.length > 1 && command.equals("tell") && !args[1]
                .equalsIgnoreCase(player.getName())) {
          if (!this.canReceiveTell(args[1].toLowerCase())) {
            evt.setCancelled(true);
            player.sendMessage(TextComponent.fromLegacyText("§cThis user has disabled private messages."));
          }
        }
      }
    }
  }


  private void modifyProperties(LoginResult profile, String[] data) {
    List<Property> properties = new ArrayList<>();
    for (Property property : profile.getProperties() == null ? new ArrayList<Property>() : Arrays.asList(profile.getProperties())) {
      if (property.getName().equalsIgnoreCase("textures")) {
        continue;
      }

      properties.add(property);
    }

    properties.add(new Property("textures", data[0], data[1]));
    profile.setProperties(properties.toArray(new Property[0]));
  }

  private boolean canReceiveTell(String name) {
    if (TELL_CACHE.containsKey(name)) {
      return TELL_CACHE.get(name);
    }

    boolean canReceiveTell = Database.getInstance().getPreference(name, "pm", true);
    TELL_CACHE.put(name, canReceiveTell);
    return canReceiveTell;
  }

  private boolean hasProtectionLobby(String name) {
    if (PROTECTION_CACHE.containsKey(name)) {
      return PROTECTION_CACHE.get(name);
    }

    boolean hasProtectionLobby = Database.getInstance().getPreference(name, "pl", true);
    PROTECTION_CACHE.put(name, hasProtectionLobby);
    return hasProtectionLobby;
  }
}
