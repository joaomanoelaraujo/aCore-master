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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;


public class CompListener
extends AbstractListener {
    private PacketRecorder packetRecorder;

    public CompListener(PacketRecorder packetRecorder) {
        this.packetRecorder = packetRecorder;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (this.packetRecorder.getRecorder().getPlayers().contains(p.getName())) {
            // Aqui você pode acessar os itens principais e secundários
            org.bukkit.inventory.ItemStack mainHandItem = p.getInventory().getItemInHand();
            ItemStack offHandItem = p.getInventory().getItemInHand();

            InvData data = NPCManager.copyFromPlayer(p, true, true);
            data.setMainHand(NPCManager.fromItemStack(mainHandItem));
            data.setOffHand(NPCManager.fromItemStack(offHandItem));
            this.packetRecorder.addData(p.getName(), data);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();
        PlayerWatcher watcher = this.packetRecorder.getRecorder().getData().getWatcher(p.getName());
        if (this.packetRecorder.getRecorder().getPlayers().contains(p.getName())) {
            // Atualize a lógica para refletir se o jogador está voando
            boolean isFlying = p.isFlying();
            this.packetRecorder.addData(p.getName(), new MetadataUpdate(watcher.isBurning(), watcher.isBlocking(), isFlying));
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

