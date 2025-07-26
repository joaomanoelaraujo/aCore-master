package me.joaomanoel.d4rkk.dev.libraries.npc;

import java.util.Collection;

import me.joaomanoel.d4rkk.dev.nms.npc.NpcEntity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCLookTask extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final Collection<NpcEntity> npcs;
    private final double rangeSq;

    public NPCLookTask(JavaPlugin plugin, Collection<NpcEntity> npcs, double range) {
        this.plugin = plugin;
        this.npcs = npcs;
        this.rangeSq = range * range;
    }

    public void start() {
        this.runTaskTimer(this.plugin, 0L, 1L);
    }

    @Override
    public void run() {
        for (NpcEntity npc : this.npcs) {
            if (npc.getPlayer() == null || !npc.getPlayer().isValid()) continue;

            Location npcLoc = npc.getPlayer().getLocation();
            Player nearest = findNearestPlayer(npcLoc);

            if (nearest != null) {
                face(npc, nearest);
            }
        }
    }

    private Player findNearestPlayer(Location at) {
        Player best = null;
        double bestDst = this.rangeSq;

        for (Player p : at.getWorld().getPlayers()) {
            if (p == null || !p.isOnline()) continue;

            boolean isNpc = this.npcs.stream()
                    .anyMatch(n -> n.getPlayer().getUniqueId().equals(p.getUniqueId()));
            if (isNpc) continue;

            double d2 = p.getLocation().distanceSquared(at);
            if (d2 < bestDst) {
                bestDst = d2;
                best = p;
            }
        }

        return best;
    }

    private void face(NpcEntity npc, Player target) {
        EntityPlayer handle = ((CraftPlayer) npc.getPlayer()).getHandle();
        Location from = npc.getPlayer().getEyeLocation();
        Location to = target.getEyeLocation();

        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();

        double distXZ = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        float pitch = (float) Math.toDegrees(-Math.atan2(dy, distXZ));

        yaw = (yaw + 360) % 360;
        pitch = Math.max(Math.min(pitch, 90), -90);

        handle.yaw = yaw;
        handle.pitch = pitch;
        handle.aJ = yaw;
        handle.aK = yaw;

        byte bYaw = (byte) (yaw * 256 / 360);
        byte bPitch = (byte) (pitch * 256 / 360);

        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket =
                new PacketPlayOutEntity.PacketPlayOutEntityLook(handle.getId(), bYaw, bPitch, true);
        PacketPlayOutEntityHeadRotation headPacket =
                new PacketPlayOutEntityHeadRotation(handle, bYaw);

        for (Player p : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(lookPacket);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(headPacket);
        }
    }
}