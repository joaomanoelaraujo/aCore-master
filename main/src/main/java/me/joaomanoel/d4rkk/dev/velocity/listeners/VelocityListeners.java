package me.joaomanoel.d4rkk.dev.velocity.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import me.joaomanoel.d4rkk.dev.velocity.party.VelocityParty;
import me.joaomanoel.d4rkk.dev.velocity.party.VelocityPartyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.HashMap;
import java.util.Map;

public class VelocityListeners {

    private static final Map<String, Long> PROTECTION_LOBBY = new HashMap<>();
    private static final Map<String, Boolean> TELL_CACHE = new HashMap<>();
    private static final Map<String, Boolean> PROTECTION_CACHE = new HashMap<>();

    private final VelocityPlugin plugin;

    public VelocityListeners(VelocityPlugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        String name = event.getPlayer().getUsername().toLowerCase();
        TELL_CACHE.remove(name);
        PROTECTION_CACHE.remove(name);
        PROTECTION_LOBBY.remove(name);
    }

    @Subscribe(order = PostOrder.EARLY)
    public void onPluginMessage(PluginMessageEvent event) {
        if (!(event.getSource() instanceof ServerConnection)) return;
        if (!(event.getTarget() instanceof Player)) return;

        if (!event.getIdentifier().getId().equals("acore:main")) return;

        Player player = (Player) event.getTarget();
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();

        if (subChannel.equalsIgnoreCase("PARTY_LEADER_JOIN_GAME")) {
            VelocityParty party = VelocityPartyManager.getLeaderParty(player.getUsername());
            if (party != null) {
                String serverName = player.getCurrentServer()
                        .map(s -> s.getServerInfo().getName())
                        .orElse(null);
                if (serverName != null) {
                    party.onLeaderJoinGame(serverName);
                }
            }
        } else if (subChannel.equalsIgnoreCase("PARTY_LEADER_LEAVE_GAME")) {
            VelocityParty party = VelocityPartyManager.getLeaderParty(player.getUsername());
            if (party != null) {
                party.onLeaderLeaveGame();
            }
        }
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onServerConnected(ServerPostConnectEvent event) {
        Player player = event.getPlayer();

        // Notifica a party sobre mudança de servidor do líder
        VelocityParty party = VelocityPartyManager.getLeaderParty(player.getUsername());
        if (party != null && player.getCurrentServer().isPresent()) {
            String serverName = player.getCurrentServer().get().getServerInfo().getName();
            party.sendData(player.getCurrentServer().get().getServer());
            party.onLeaderServerChange(serverName);
        }

        // Sistema de fake (você precisará implementar a lógica de skin)
        if (VelocityPlugin.isFake(player.getUsername())) {
            // Velocity não suporta modificação de skin diretamente
            // Você precisará usar um plugin de skin como SkinsRestorer
            plugin.getLogger().warn("Sistema de fake skin requer plugin adicional no Velocity");
        }
    }

    @Subscribe(order = PostOrder.EARLY)
    public void onCommand(CommandExecuteEvent event) {
        if (!(event.getCommandSource() instanceof Player)) return;

        Player player = (Player) event.getCommandSource();
        String command = event.getCommand();
        String[] args = command.split(" ");
        String cmd = args[0].toLowerCase();

        if (cmd.equals("lobby") && hasProtectionLobby(player.getUsername().toLowerCase())) {
            long last = PROTECTION_LOBBY.getOrDefault(player.getUsername().toLowerCase(), 0L);
            if (last > System.currentTimeMillis()) {
                PROTECTION_LOBBY.remove(player.getUsername().toLowerCase());
                return;
            }

            event.setResult(CommandExecuteEvent.CommandResult.denied());
            PROTECTION_LOBBY.put(player.getUsername().toLowerCase(), System.currentTimeMillis() + 3000);

            Component message = LegacyComponentSerializer.legacySection()
                    .deserialize("§aAre you sure? Use /lobby again to go back to the lobby.");
            player.sendMessage(message);
        } else if (cmd.equals("tell") && args.length > 1 && !args[1].equalsIgnoreCase(player.getUsername())) {
            if (!canReceiveTell(args[1].toLowerCase())) {
                event.setResult(CommandExecuteEvent.CommandResult.denied());

                Component message = LegacyComponentSerializer.legacySection()
                        .deserialize("§cThis user has disabled private messages.");
                player.sendMessage(message);
            }
        }
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