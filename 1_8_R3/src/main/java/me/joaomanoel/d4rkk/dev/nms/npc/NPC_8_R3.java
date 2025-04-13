package me.joaomanoel.d4rkk.dev.nms.npc;

import com.mojang.authlib.GameProfile;
import me.joaomanoel.d4rkk.dev.nms.NMS_Interface;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class NPC_8_R3 extends EntityPlayer implements NpcEntity {

    private final Location location;
    private final NMS_Interface nms;
    private boolean copySkin;

    public NPC_8_R3(Location location, NMS_Interface nms, GameProfile gp) {
        super(MinecraftServer.getServer(), ((CraftWorld) location.getWorld()).getHandle(), gp, new PlayerInteractManager(((CraftWorld) location.getWorld()).getHandle()));
        this.location = location;
        this.nms = nms;
        this.copySkin = false;
        this.getBukkitEntity().setRemoveWhenFarAway(false);
    }

    @Override
    public void setName(String text) {
        this.setCustomName(text);
    }

    @Override
    public void kill() {
        super.die();
        for (Player online : Bukkit.getOnlinePlayers()) {
            this.nms.sendTabListRemove(online, getBukkitEntity());
        }
    }

    @Override
    public void setLocation(World world, double x, double y, double z) {
        this.setLocation(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
    }

    @Override
    public void spawn() {
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(this);
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.isOnline()) {
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                nms.sendTabListAdd(online, getBukkitEntity());
                online.hidePlayer(this.getPlayer());
                online.showPlayer(this.getPlayer());
            }
        }
    }

    @Override
    public void setItemInHand(ItemStack item) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOnline()) {
                PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
                connection.sendPacket(new PacketPlayOutEntityEquipment(getId(), 0, CraftItemStack.asNMSCopy(item)));
            }
        }
    }

    @Override
    public void setShowNick(boolean showNick) {
        Player npc = this.getBukkitEntity();
        if (!showNick) {
            ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), npc.getName());
            team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
            ArrayList<String> playerToAdd = new ArrayList<>();
            for (Player online : Bukkit.getOnlinePlayers()) {
                PlayerConnection connection = ((CraftPlayer) online).getHandle().playerConnection;
                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
                playerToAdd.add(npc.getName());
                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
            }
        }
    }

    @Override
    public void setPlayerCopySkin(boolean playerCopySkin) {
        this.copySkin = playerCopySkin;
    }

    @Override
    public boolean isCopySkin() {
        return this.copySkin;
    }

    @Override
    public Player getPlayer() {
        return this.getBukkitEntity();
    }

}

