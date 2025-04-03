package hypedmc.com.br.maincore.v1_20_R3.entity;

import com.mojang.authlib.GameProfile;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.libraries.npclib.NPCLibrary;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC;
import me.joaomanoel.d4rkk.dev.libraries.npclib.npc.AbstractEntityController;
import me.joaomanoel.d4rkk.dev.libraries.npclib.npc.skin.SkinnableEntity;
import me.joaomanoel.d4rkk.dev.nms.NMS;
import me.joaomanoel.d4rkk.dev.nms.v1_8_R3.entity.EntityNPCPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

public class HumanController extends AbstractEntityController {
  
  @Override
  protected Entity createEntity(Location location, NPC npc) {
    WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
    UUID uuid = npc.getUUID();
    GameProfile profile = new GameProfile(uuid, npc.getName().substring(0, Math.min(npc.getName().length(), 16)));
    
    EntityNPCPlayer handle = new EntityNPCPlayer(nmsWorld.getMinecraftServer(), nmsWorld, profile, new PlayerInteractManager(nmsWorld), npc);
    
    handle.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    Bukkit.getScheduler().scheduleSyncDelayedTask(NPCLibrary.getPlugin(), () -> {
      if (getBukkitEntity() != null && getBukkitEntity().isValid()) {
        NMS.removeFromPlayerList(handle.getBukkitEntity());
      }
    }, 20);
    handle.getBukkitEntity().setMetadata("NPC", new FixedMetadataValue(Core.getInstance(), true));
    handle.getBukkitEntity().setSleepingIgnored(true);
    
    return handle.getBukkitEntity();
  }
  
  @Override
  public Player getBukkitEntity() {
    return (Player) super.getBukkitEntity();
  }
  
  @Override
  public void remove() {
    Player entity = getBukkitEntity();
    if (entity != null) {
      NMS.removeFromWorld(entity);
      SkinnableEntity skinnable = NMS.getSkinnable(entity);
      skinnable.getSkinTracker().onRemoveNPC();
    }
    
    super.remove();
  }
}
