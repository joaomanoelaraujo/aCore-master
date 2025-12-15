/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityToggleGlideEvent
 *  org.bukkit.event.player.PlayerSwapHandItemsEvent
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class CompListener
extends AbstractListener {
    private final PacketRecorder packetRecorder;

    public CompListener(PacketRecorder packetRecorder) {
        this.packetRecorder = packetRecorder;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        if (this.packetRecorder.getRecorder().getPlayers().contains(p.getName())) {
            InvData data = NPCManager.copyFromPlayer(p, true, true);
            data.setMainHand(NPCManager.fromItemStack(e.getMainHandItem()));
            data.setOffHand(NPCManager.fromItemStack(e.getOffHandItem()));
            this.packetRecorder.addData(p.getName(), data);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onGlide(EntityToggleGlideEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            PlayerWatcher watcher = this.packetRecorder.getRecorder().getData().getWatcher(p.getName());
            if (this.packetRecorder.getRecorder().getPlayers().contains(p.getName())) {
                this.packetRecorder.addData(p.getName(), new MetadataUpdate(watcher.isBurning(), watcher.isBlocking(), watcher.isElytra()));
            }
        }
    }

    public void onSwim(Event e) {
        Class<?> swimEvent = e.getClass();
        try {
            Entity en = (Entity)swimEvent.getMethod("getEntity", new Class[0]).invoke(e, new Object[0]);
            if (en instanceof Player) {
                Player p = (Player)en;
                PlayerWatcher watcher = this.packetRecorder.getRecorder().getData().getWatcher(p.getName());
                if (this.packetRecorder.getRecorder().getPlayers().contains(p.getName())) {
                    boolean isSwimming = (Boolean)swimEvent.getMethod("isSwimming", new Class[0]).invoke(e, new Object[0]);
                    watcher.setSwimming(isSwimming);
                    this.packetRecorder.addData(p.getName(), MetadataUpdate.fromWatcher(watcher));
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void register() {
        super.register();
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_13)) {
            ReflectionHelper.getInstance().registerEvent(ReflectionHelper.getInstance().getSwimEvent(), this, this::onSwim);
        }
    }
}

