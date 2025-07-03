package me.joaomanoel.d4rkk.dev.utils.particles;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedParticle;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public enum ParticleEffect {
  
  EXPLOSION_NORMAL("explode"),
  EXPLOSION_LARGE("largeexplode"),
  FIREWORKS_SPARK("fireworksSpark"),
  WATER_BUBBLE("bubble"),
  WATER_SPLASH("splash"),
  WATER_WAKE("wake"),
  SUSPENDED("suspended"),
  SUSPENDED_DEPTH("depthSuspend"),
  CRIT("crit"),
  CRIT_MAGIC("magicCrit"),
  SMOKE_NORMAL("smoke"),
  SMOKE_LARGE("largesmoke"),
  SPELL("spell"),
  SPELL_INSTANT("instantSpell"),
  SPELL_MOB("mobSpell"),
  SPELL_MOB_AMBIENT("mobSpellAmbient"),
  SPELL_WITCH("witchMagic"),
  DRIP_WATER("dripWater"),
  DRIP_LAVA("dripLava"),
  VILLAGER_ANGRY("angryVillager"),
  VILLAGER_HAPPY("happyVillager"),
  TOWN_AURA("townaura"),
  NOTE("note"),
  PORTAL("portal"),
  ENCHANTMENT_TABLE("enchantmenttable"),
  FLAME("flame"),
  LAVA("lava"),
  FOOTSTEP("footstep"),
  CLOUD("cloud"),
  REDSTONE("reddust"),
  SNOWBALL("snowballpoof"),
  SNOW_SHOVEL("snowshovel"),
  SLIME("slime"),
  HEART("heart"),
  BARRIER("barrier"),
  ITEM_CRACK("iconcrack"),
  BLOCK_CRACK("blockcrack"),
  BLOCK_DUST("blockdust"),
  WATER_DROP("droplet"),
  ITEM_TAKE("take"),
  MOB_APPEARANCE("mobappearance");

  private final String name;
  
  ParticleEffect(String name) {
    this.name = name;
  }

  public void display(Player viewer, boolean isFar, float x, float y, float z, float offSetX, float offSetY, float offSetZ, float speed, int count) {
    BukkitUtils.displayParticle(viewer, this.name, isFar, x, y, z, offSetX, offSetY, offSetZ, speed, count);
  }

  public static ParticleEffect fromName(String name) {
      return Arrays.stream(values()).filter(e -> e.name.equals(name)).findFirst().orElse(null);
  }

}
