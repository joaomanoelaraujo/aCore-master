package me.joaomanoel.d4rkk.dev.libraries.npc;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.nms.NMSManager;
import me.joaomanoel.d4rkk.dev.nms.npc.NpcEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCLibrary implements Listener {

    private static final List<NpcEntity> NPCS = new ArrayList<>();

    public static void setupNPCManager() {
        Bukkit.getPluginManager().registerEvents(new NPCLibrary(), Core.getInstance());
    }

    public static NpcEntity createNPC(Location location, String name) {
        return createNPC(location, name, "", "");
    }

    public static NpcEntity createNPC(Location location, String name, String value, String signature) {
        NpcEntity npc = NMSManager.createNPC(location, name, value, signature);
        npc.setData("yaw",   String.valueOf(location.getYaw()));
        npc.setData("pitch", String.valueOf(location.getPitch()));
        NPCS.add(npc);
        return npc;
    }

    public static NpcEntity findByUUID(UUID uuid) {
        return NPCS.stream().filter(npcEntity -> npcEntity.getPlayer().getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    public static boolean isNPC(Entity entity) {
        return NPCS.stream().anyMatch(npcEntity -> npcEntity.getPlayer().getUniqueId().equals(entity.getUniqueId()));
    }

    public static void removeNPC(NpcEntity npc) {
        npc.kill();
        NPCS.remove(npc);
    }

    public static List<NpcEntity> listNPCs() {
        return NPCS;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity clickEntity = event.getRightClicked();
        if (clickEntity instanceof Player) {
            NPCS.stream().filter(npc1 -> npc1.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().ifPresent(npc -> npc.interactAtPlayer(player));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player viewer = event.getPlayer();
        for (NpcEntity npc : NPCS) {
            npc.spawn(viewer);
            Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> npc.setShowNick(viewer), 20L);
            Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
                float yaw   = Float.parseFloat(npc.getData("yaw"));
                float pitch = Float.parseFloat(npc.getData("pitch"));
                NMSManager.look(npc.getPlayer(), yaw, pitch);
                NMSManager.setHeadYaw(npc.getPlayer(), yaw);
            }, 2L);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        NPCS.forEach(npc -> npc.kill(player));
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        Player viewer = event.getPlayer();
        for (NpcEntity npc : NPCS) {
            npc.spawn(viewer);
            Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> npc.setShowNick(viewer), 20L);
            Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
                float yaw   = Float.parseFloat(npc.getData("yaw"));
                float pitch = Float.parseFloat(npc.getData("pitch"));
                NMSManager.look(npc.getPlayer(), yaw, pitch);
                NMSManager.setHeadYaw(npc.getPlayer(), yaw);

            }, 2L);

        }

    }
    }
