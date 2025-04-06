/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.WrappedGameProfile
 *  com.comphenix.protocol.wrappers.WrappedSignedProperty
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.bukkit.scheduler.BukkitRunnable;

public class Recorder {
    private final List<String> players = new ArrayList<String>();
    private final Replay replay;
    private final ReplayData data = new ReplayData();
    private BukkitRunnable run;
    private int currentTick;
    private PacketRecorder packetRecorder;

    public Recorder(Replay replay, List<Player> players) {
        this.replay = replay;
        HashMap<String, PlayerWatcher> tmpWatchers = new HashMap<String, PlayerWatcher>();
        for (Player player : players) {
            this.players.add(player.getName());
            tmpWatchers.put(player.getName(), new PlayerWatcher(player.getName()));
        }
        this.data.setWatchers(tmpWatchers);
    }

    public void start() {
        this.packetRecorder = new PacketRecorder(this);
        this.packetRecorder.register();
        for (String names : this.players) {
            if (Bukkit.getPlayer(names) == null) continue;
            Player player = Bukkit.getPlayer(names);
            this.createSpawnAction(player, player.getLocation(), true);
        }
        this.run = new BukkitRunnable(){

            public void run() {
                HashMap<String, List<PacketData>> tmpMap = new HashMap<String, List<PacketData>>(Recorder.this.packetRecorder.getPacketData());
                for (String name : tmpMap.keySet()) {
                    List<PacketData> list = new ArrayList<>(tmpMap.get(name)); // Verifique o tipo aqui
                    for (PacketData packetData : list) {
                        EntityItemData data;
                        if (packetData instanceof BlockChangeData && !ConfigManager.RECORD_BLOCKS || packetData instanceof EntityItemData && (data = (EntityItemData)packetData).getAction() != 2 && !ConfigManager.RECORD_ITEMS || (packetData instanceof EntityData || packetData instanceof EntityMovingData || packetData instanceof EntityAnimationData) && !ConfigManager.RECORD_ENTITIES || packetData instanceof ChatData && !ConfigManager.RECORD_CHAT) continue;
                        ActionData actionData = new ActionData(Recorder.this.currentTick, ActionType.PACKET, name, packetData);
                        Recorder.this.addData(Recorder.this.currentTick, actionData);
                    }
                }
                Recorder.this.packetRecorder.getPacketData().keySet().removeAll(tmpMap.keySet());
                if (ReplayAPI.getInstance().getHookManager().isRegistered()) {
                    for (IReplayHook hook : ReplayAPI.getInstance().getHookManager().getHooks()) {
                        for (String names : Recorder.this.players) {
                            List<PacketData> customList = hook.onRecord(names);
                            customList.stream().filter(Objects::nonNull).forEach(customData -> {
                                ActionData customAction = new ActionData(Recorder.this.currentTick, ActionType.CUSTOM, names, customData);
                                Recorder.this.addData(Recorder.this.currentTick, customAction);
                            });
                        }
                    }
                }
                Recorder.this.currentTick++;
                if (Recorder.this.currentTick / 20 >= ConfigManager.MAX_LENGTH) {
                    Recorder.this.stop(ConfigManager.SAVE_STOP);
                }
            }
        };
        this.run.runTaskTimerAsynchronously(Core.getInstance(), 1L, 1L);
    }

    public void addData(int tick, ActionData actionData) {
        List<ActionData> list = new ArrayList<ActionData>();
        if (this.data.getActions().containsKey(tick)) {
            list = this.data.getActions().get(tick);
        }
        list.add(actionData);
        this.data.getActions().put(tick, list);
    }

    public void stop(boolean save) {
        this.packetRecorder.unregister();
        this.run.cancel();
        if (save) {
            for (String playera : this.players) {
                if (Bukkit.getPlayer(playera) == null) continue;
                Player player = Bukkit.getPlayer(playera);
                this.data.setDuration(this.currentTick);
                this.data.setCreator(this.players);
                this.data.setWatchers(new HashMap<>());
                this.replay.setData(this.data);
                this.replay.setReplayInfo(new ReplayInfo(this.replay.getId(), this.players, System.currentTimeMillis(), this.currentTick));
                ReplaySaver.save(this.replay);
            }
        } else {
            this.data.getActions().clear();
        }
        this.replay.setRecording(false);
        ReplayManager.activeReplays.remove(this.replay.getId());
    }

    public void createSpawnAction(final Player player, final Location loc, final boolean first) {
        final SignatureData[] signArr = new SignatureData[1];
        if (!Bukkit.getOnlineMode() && ConfigManager.USE_OFFLINE_SKINS) {
            new BukkitRunnable(){

                public void run() {
                    PlayerInfo info = (PlayerInfo)WebsiteFetcher.getJson("https://api.mojang.com/users/profiles/minecraft/" + player.getName(), true, new JsonData(true, new PlayerInfo()));
                    if (info != null) {
                        SkinInfo skin = (SkinInfo)WebsiteFetcher.getJson("https://sessionserver.mojang.com/session/minecraft/profile/" + info.getId() + "?unsigned=false", true, new JsonData(true, new SkinInfo()));
                        Map<String, String> props = skin.getProperties().get(0);
                        signArr[0] = new SignatureData(props.get("name"), props.get("value"), props.get("signature"));
                    }
                    ActionData spawnData = new ActionData(0, ActionType.SPAWN, player.getName(), new SpawnData(player.getUniqueId(), LocationData.fromLocation(loc), signArr[0]));
                    Recorder.this.addData(first ? 0 : Recorder.this.currentTick, spawnData);
                    ActionData invData = new ActionData(0, ActionType.PACKET, player.getName(), NPCManager.copyFromPlayer(player, true, true));
                    Recorder.this.addData(first ? 0 : Recorder.this.currentTick, invData);
                }
            }.runTaskAsynchronously(Core.getInstance());
        }
        Multimap<String, WrappedSignedProperty> map = WrappedGameProfile.fromPlayer(player).getProperties();
        for (String prop : map.asMap().keySet()) {
            for (WrappedSignedProperty sp : map.get(prop)) {
                signArr[0] = new SignatureData(sp.getName(), sp.getValue(), sp.getSignature());
            }
        }
        if (!ConfigManager.USE_OFFLINE_SKINS || Bukkit.getOnlineMode()) {
            ActionData spawnData = new ActionData(0, ActionType.SPAWN, player.getName(), new SpawnData(player.getUniqueId(), LocationData.fromLocation(loc), signArr[0]));
            this.addData(this.currentTick, spawnData);
            ActionData invData = new ActionData(this.currentTick, ActionType.PACKET, player.getName(), NPCManager.copyFromPlayer(player, true, true));
            this.addData(this.currentTick, invData);
        }
    }

    public List<String> getPlayers() {
        return this.players;
    }

    public ReplayData getData() {
        return this.data;
    }

    public int getCurrentTick() {
        return this.currentTick;
    }
}

