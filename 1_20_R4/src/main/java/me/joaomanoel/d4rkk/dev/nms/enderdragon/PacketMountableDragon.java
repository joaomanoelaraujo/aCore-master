package me.joaomanoel.d4rkk.dev.nms.enderdragon;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.PacketType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicInteger;


public class PacketMountableDragon {

    private static final ProtocolManager PROTO = ProtocolLibrary.getProtocolManager();
    private static final AtomicInteger NEXT_ID = new AtomicInteger(10_000);

    private final Player viewer;
    private final int dragonId;
    private final int playerId;
    private final double spawnY;

    public PacketMountableDragon(JavaPlugin plugin, Player viewer, Location start) {
        this.viewer   = viewer;
        this.playerId = viewer.getEntityId();
        this.dragonId = NEXT_ID.getAndIncrement();
        this.spawnY   = start.getY();

        spawnDragon(start);
        mountPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!viewer.isOnline()) {
                    destroy();
                    cancel();
                    return;
                }
                if (!isMounted()) {
                    destroy();
                    cancel();
                    return;
                }
                Location loc = viewer.getLocation();
                if (loc.getY() > spawnY + 2) {
                    loc.setY(spawnY + 2);
                }
                Block target = viewer.getTargetBlockExact(20);
                if (target != null) loc = target.getLocation().add(.5, 0, .5);
                teleport(loc, viewer.getLocation().getYaw(), viewer.getLocation().getPitch());
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    private void spawnDragon(Location loc) {
        PacketContainer spawn = PROTO.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        spawn.getIntegers().write(0, dragonId);
        spawn.getIntegers().write(1, 63);
        spawn.getIntegers().write(2, 0);
        spawn.getDoubles()
                .write(0, loc.getX())
                .write(1, loc.getY())
                .write(2, loc.getZ());
        spawn.getBytes()
                .write(0, (byte)(loc.getYaw()   * 256f/360f))
                .write(1, (byte)(loc.getPitch() * 256f/360f));
        PROTO.sendServerPacket(viewer, spawn);

        PacketContainer vel = PROTO.createPacket(PacketType.Play.Server.ENTITY_VELOCITY);
        vel.getIntegers()
                .write(0, dragonId)
                .write(1, 0)
                .write(2, 0)
                .write(3, 0);
        PROTO.sendServerPacket(viewer, vel);

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcherObject flags =
                new WrappedDataWatcherObject(0, Registry.get(Byte.class));
        watcher.setObject(flags, (byte)0);  // sem invisibilidade, sem nada
        PacketContainer meta = PROTO.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        meta.getIntegers().write(0, dragonId);
        meta.getWatchableCollectionModifier()
                .write(0, watcher.getWatchableObjects());
        PROTO.sendServerPacket(viewer, meta);
    }



    private void mountPlayer() {
        PacketContainer mount = PROTO.createPacket(PacketType.Play.Server.MOUNT);
        mount.getIntegers().write(0, dragonId);
        mount.getIntegerArrays().write(0, new int[]{ playerId });
        PROTO.sendServerPacket(viewer, mount);
    }

    private boolean isMounted() {

        return true;
    }

    private void teleport(Location loc, float yaw, float pitch) {
        PacketContainer tp = PROTO.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        tp.getIntegers().write(0, dragonId);
        tp.getDoubles()
                .write(0, loc.getX())
                .write(1, loc.getY())
                .write(2, loc.getZ());
        tp.getBytes()
                .write(0, (byte)(yaw  * 256f / 360f))
                .write(1, (byte)(pitch* 256f / 360f));
        PROTO.sendServerPacket(viewer, tp);
    }

    private void destroy() {
        PacketContainer destroy = PROTO.createPacket(PacketType.Play.Server.ENTITY_DESTROY);


        destroy.getIntegers().write(0, 1);
        destroy.getIntegers().write(1, dragonId);

        PROTO.sendServerPacket(viewer, destroy);
    }
}
