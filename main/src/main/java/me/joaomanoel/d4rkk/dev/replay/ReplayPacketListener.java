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
import me.joaomanoel.d4rkk.dev.replay.AbstractListener;
import me.joaomanoel.d4rkk.dev.replay.ReplayHelper;
import me.joaomanoel.d4rkk.dev.replay.Replayer;
import me.joaomanoel.d4rkk.dev.replay.VersionUtil;
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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ReplayPacketListener
extends AbstractListener {
    private PacketAdapter packetAdapter;
    private final Replayer replayer;
    private int previous;
    private final HashMap<Player, Integer> spectating;

    public ReplayPacketListener(Replayer replayer) {
        this.replayer = replayer;
        this.spectating = new HashMap<>();
        this.previous = -1;
        if (!this.isRegistered()) {
            this.register();
        }
    }

    @Override
    public void register() {
        this.packetAdapter = new PacketAdapter(Core.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY, PacketType.Play.Server.ENTITY_DESTROY){

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
                Player p = event.getPlayer();
                if (ReplayHelper.replaySessions.containsKey(p.getName()) && ReplayPacketListener.this.isSpectating(p)) {
                    List entityIds = VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17) ? packet.getHandle().getIntLists().read(0) : IntStream.of(packet.getEntityIDs()).boxed().collect(Collectors.toList());
                    for (Object entityId : entityIds) {
                        int id = (Integer) entityId;
                        if (id != ReplayPacketListener.this.spectating.get(p)) continue;
                        ReplayPacketListener.this.setCamera(p, p.getEntityId(), ReplayPacketListener.this.previous);
                    }
                }
            }
        };
        ProtocolLibrary.getProtocolManager().addPacketListener(this.packetAdapter);
    }

    @Override
    public void unregister() {
        ProtocolLibrary.getProtocolManager().removePacketListener(this.packetAdapter);
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
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_16)) {
            state.getHandle().getGameStateIDs().write(0, 3);
        } else {
            state.setReason(3);
        }
        state.setValue(Math.max(gamemode, 0.0f));
        state.sendPacket(p);
        camera.sendPacket(p);
        if (gamemode == 3.0f) {
            this.spectating.put(p, entityID);
        } else this.spectating.remove(p);
    }
}

