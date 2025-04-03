/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.GameMode
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;
import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import com.comphenix.packetwrapper.WrapperPlayServerCamera;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerGameStateChange;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ReplayPacketListener
extends AbstractListener {
    private PacketAdapter packetAdapter;
    private Replayer replayer;
    private int previous;
    private HashMap<Player, Integer> spectating;

    public ReplayPacketListener(Replayer replayer) {
        this.replayer = replayer;
        this.spectating = new HashMap();
        this.previous = -1;
        if (!this.isRegistered()) {
            this.register();
        }
    }

    @Override
    public void register() {
        this.packetAdapter = new PacketAdapter((Plugin)Core.getInstance(), ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Client.USE_ENTITY, PacketType.Play.Server.ENTITY_DESTROY}){

            public void onPacketReceiving(PacketEvent event) {
                WrapperPlayClientUseEntity packet = new WrapperPlayClientUseEntity(event.getPacket());
                Player p = event.getPlayer();
                if (packet.getType() == EnumWrappers.EntityUseAction.ATTACK && ReplayHelper.replaySessions.containsKey(p.getName()) && ReplayPacketListener.this.replayer.getNPCList().values().stream().anyMatch(ent -> packet.getTargetID() == ent.getId())) {
                    if (p.getGameMode() != GameMode.SPECTATOR) {
                        ReplayPacketListener.this.previous = p.getGameMode().getValue();
                    }
                    ReplayPacketListener.this.setCamera(p, packet.getTargetID(), 3.0f);
                }
            }

            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy(event.getPacket());
                Player player = event.getPlayer();

                // Verifica se o jogador está em uma sessão de replay e se está assistindo
                if (ReplayHelper.replaySessions.containsKey(player.getName()) && isSpectating(player)) {
                    // Obtém a lista de IDs de entidades dependendo da versão do Minecraft
                    List<Integer> entityIds;
                    if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17)) {
                        // Para versões acima de 1.17, ajuste conforme o método correto para acessar IDs
                        // Aqui, assumimos que getEntityIDs() pode ser usado para essas versões
                        entityIds = IntStream.of(packet.getEntityIDs()).boxed().collect(Collectors.toList());
                    } else {
                        // Para versões abaixo de 1.17, usa getEntityIDs()
                        entityIds = IntStream.of(packet.getEntityIDs()).boxed().collect(Collectors.toList());
                    }

                    // Itera sobre a lista de IDs e verifica se o ID corresponde ao do jogador que está sendo assistido
                    for (int id : entityIds) {
                        if (id == (int) ReplayPacketListener.this.spectating.get(player)) {
                            // Ajusta a câmera do jogador se o ID corresponder
                            setCamera(player, player.getEntityId(), ReplayPacketListener.this.previous);
                            break; // Encerra o loop após encontrar a correspondência
                        }
                    }
                }
                }
        };
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)this.packetAdapter);
    }

    @Override
    public void unregister() {
        ProtocolLibrary.getProtocolManager().removePacketListener((PacketListener)this.packetAdapter);
    }

    public boolean isRegistered() {
        return this.packetAdapter != null;
    }

    public int getPrevious() {
        return this.previous;
    }

    public boolean isSpectating(Player p) {
        return this.spectating.containsKey(p);
    }

    public void setCamera(Player p, int entityID, float gamemode) {
        WrapperPlayServerCamera camera = new WrapperPlayServerCamera();
        camera.setCameraId(entityID);

        WrapperPlayServerGameStateChange state = new WrapperPlayServerGameStateChange();

        // Ajusta o estado do jogo conforme a versão
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_16)) {
            // Usando um método alternativo para definir o estado do jogo
            state.setReason(3); // Configura o motivo como 3
        } else {
            state.setReason(3);
        }

        state.setValue(gamemode < 0.0f ? 0.0f : gamemode); // Ajusta o valor do estado do jogo
        state.sendPacket(p);
        camera.sendPacket(p);

        if (gamemode == 3.0f) {
            this.spectating.put(p, entityID);
        } else if (this.spectating.containsKey(p)) {
            this.spectating.remove(p);
        }
    }

}

