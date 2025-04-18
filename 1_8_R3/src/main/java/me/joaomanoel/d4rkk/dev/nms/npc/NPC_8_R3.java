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
    private boolean showNick;

    public NPC_8_R3(Location location, NMS_Interface nms, GameProfile gp) {
        super(MinecraftServer.getServer(), ((CraftWorld) location.getWorld()).getHandle(), gp, new PlayerInteractManager(((CraftWorld) location.getWorld()).getHandle()));
        this.location = location;
        this.nms = nms;
        this.copySkin = false;
        this.showNick = true;
        this.getBukkitEntity().setRemoveWhenFarAway(false);
        this.nms.addToWorld(this.location.getWorld(), getBukkitEntity(), CreatureSpawnEvent.SpawnReason.CUSTOM);
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
    public void kill(Player player) {
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(getPlayer().getEntityId());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroyPacket);
    }

    @Override
    public void setLocation(World world, double x, double y, double z) {
        this.setLocation(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
    }

    @Override
    public void spawn() {
        for (Player online : Bukkit.getOnlinePlayers()) spawn(online);
    }

    @Override
    public void spawn(Player player) {
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(this);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        player.hidePlayer(this.getPlayer());
        player.showPlayer(this.getPlayer());
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
        this.showNick = showNick;
        for (Player online : Bukkit.getOnlinePlayers()) {
            setShowNick(online);
        }
    }

    @Override
    public void setShowNick(Player player) {
        Player npc = this.getBukkitEntity();
        if (this.showNick) {
            nms.sendTabListAdd(player, npc);
            return;
        }

        ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), npc.getName());
        team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        ArrayList<String> playerToAdd = new ArrayList<>();
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
        connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
        playerToAdd.add(npc.getName());
        connection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
        nms.sendTabListRemove(player, npc);
    }

    @Override
    public void setPlayerCopySkin(boolean playerCopySkin) {
        this.copySkin = playerCopySkin;
    }

    @Override
    public void interactAtPlayer(Player player) {
        Bukkit.getPluginManager().callEvent(new PlayerInteractAtNPCEvent(player, this));
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

