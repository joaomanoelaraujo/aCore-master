package me.joaomanoel.d4rkk.dev.libraries.npc;

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

import java.util.Collection;

public class NPCLookTask extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final Collection<NpcEntity> npcs;
    private final double rangeSq;

    /**
     * @param plugin your JavaPlugin instance
     * @param npcs   the NpcEntity collection you got from ShopkeeperNPC.getLookableNpcs()
     * @param range  max distance in blocks to “see” a player
     */
    public NPCLookTask(JavaPlugin plugin,
                       Collection<NpcEntity> npcs,
                       double range) {
        this.plugin  = plugin;
        this.npcs    = npcs;
        this.rangeSq = range * range;
    }

    /** Call this once after you spawned all your ShopkeeperNPCs */
    public void start() {
        runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    public void run() {
        for (NpcEntity npc : npcs) {
            Location npcLoc = npc.getPlayer().getLocation();
            Player nearest = findNearestHuman(npcLoc);
            if (nearest != null) face(npc, nearest);
        }
    }

    /** Busca o player humano mais próximo, ignorando os seus NPCs */
    private Player findNearestHuman(Location at) {
        Player best    = null;
        double bestDst = rangeSq;
        for (Player p : at.getWorld().getPlayers()) {
            // pula qualquer UUID que esteja na sua lista de npcs
            boolean isNpc = npcs.stream()
                    .anyMatch(n -> n.getPlayer().getUniqueId().equals(p.getUniqueId()));
            if (isNpc) continue;

            double d2 = p.getLocation().distanceSquared(at);
            if (d2 < bestDst) {
                bestDst = d2;
                best    = p;
            }
        }
        return best;
    }

    /**
     * Gira a cabeça do NPC para olhar no olho do target.
     * Aqui usamos NMS para só mandar o pacote de LOOK + HEAD_ROTATION.
     */
    private void face(NpcEntity npc, Player target) {
        EntityPlayer handle = ((CraftPlayer) npc.getPlayer()).getHandle();
        Location from       = npc.getPlayer().getLocation();
        Location toEye      = target.getEyeLocation();

        double dx     = toEye.getX() - from.getX();
        double dy     = toEye.getY() - from.getY();
        double dz     = toEye.getZ() - from.getZ();
        double distXZ = Math.sqrt(dx*dx + dz*dz);

        float yaw   = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, distXZ)));

        // Atualiza o estado interno (yaw/pitch) do EntityPlayer
        handle.yaw   = yaw;
        handle.pitch = pitch;
        handle.aJ    = yaw;  // head yaw

        // Converte pra byte (0–255)
        byte bYaw   = (byte)((yaw % 360)   * 256f / 360f);
        byte bPitch = (byte)((pitch % 360) * 256f / 360f);

        // Cria os pacotes NMS
        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket = new PacketPlayOutEntity.PacketPlayOutEntityLook(handle.getId(), bYaw, bPitch, true);
        PacketPlayOutEntityHeadRotation headPacket = new PacketPlayOutEntityHeadRotation(handle, bYaw);

        // Envia pra todos os players reais online
        for (Player p : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(lookPacket);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(headPacket);
        }
    }
}
