package me.joaomanoel.d4rkk.dev.nms.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import me.joaomanoel.d4rkk.dev.nms.NMS_Interface;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R4.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class NPC_20_R2 extends EntityPlayer implements NpcEntity {

    private final NMS_Interface nms;
    private final Location location;
    private boolean copySkin;

    public NPC_20_R2(NMS_Interface nms, Location location, GameProfile gp) {
        super(MinecraftServer.getServer(), ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle(), gp, ClientInformation.a());
        this.nms = nms;
        this.location = location;
        this.copySkin = false;
        this.getBukkitEntity().setRemoveWhenFarAway(false);
    }

    @Override
    public void setName(String text) {
        this.getBukkitEntity().setCustomName(text);
    }

    @Override
    public void kill() {
        this.bq();
        for (Player online : Bukkit.getOnlinePlayers()) {
            nms.sendTabListRemove(online, getBukkitEntity());
        }
    }

    @Override
    public void setLocation(World world, double x, double y, double z) {
        this.b(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
    }

    @Override
    public void spawn() {
        this.nms.addToWorld(this.location.getWorld(), getBukkitEntity(), CreatureSpawnEvent.SpawnReason.CUSTOM);
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(this);
        for (Player online : Bukkit.getOnlinePlayers()) {
            this.nms.sendTabListAdd(online, getBukkitEntity());
            ((CraftPlayer) online).getHandle().transferCookieConnection.sendPacket(packet);
        }
    }

    @Override
    public void setItemInHand(ItemStack item) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            CraftPlayer.TransferCookieConnection connection = ((CraftPlayer) online).getHandle().transferCookieConnection;
            connection.sendPacket(new PacketPlayOutEntityEquipment(getBukkitEntity().getEntityId(), List.of(new Pair<>(EnumItemSlot.a, CraftItemStack.asNMSCopy(item)))));
        }
    }

    @Override
    public void setShowNick(boolean showNick) {
        Player npc = this.getBukkitEntity();
        try {
            if (!showNick) {
                ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), npc.getName());
                team.a(ScoreboardTeamBase.EnumNameTagVisibility.b);
                Class<?> innerEnumClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam$a");
                Object enumValue = Enum.valueOf((Class<Enum>) innerEnumClass, "ADD");
                for (Player online : Bukkit.getOnlinePlayers()) {
                    CraftPlayer.TransferCookieConnection connection = ((CraftPlayer) online).getHandle().transferCookieConnection;
                    connection.sendPacket(PacketPlayOutScoreboardTeam.a(team, true));
                    connection.sendPacket(PacketPlayOutScoreboardTeam.a(team, npc.getName(), (PacketPlayOutScoreboardTeam.a) enumValue));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
