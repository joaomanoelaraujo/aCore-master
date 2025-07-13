package me.joaomanoel.d4rkk.dev.utils.particles;

import com.comphenix.protocol.ProtocolLibrary;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.nms.particle.ParticleOptions;
import me.joaomanoel.d4rkk.dev.nms.particle.ParticleOptionsItem;
import org.bukkit.entity.Player;

import java.util.Arrays;

public enum ParticleEffect {

  EXPLOSION_NORMAL("EXPLOSION"),
  EXPLOSION_LARGE("EXPLOSION_EMITTER"),
  FIREWORKS_SPARK("FIREWORK"),
  WATER_BUBBLE("BUBBLE"),
  WATER_SPLASH("SPLASH"),
  WATER_WAKE("UNDERWATER"),
  SUSPENDED("SUSPENDED"),
  SUSPENDED_DEPTH("DEPTH_SUSPEND"),
  CRIT("CRIT"),
  CRIT_MAGIC("MAGIC_CRIT"),
  SMOKE_NORMAL("SMOKE"),
  SMOKE_LARGE("LARGE_SMOKE"),
  SPELL("SPELL"),
  SPELL_INSTANT("INSTANT_EFFECT"),
  SPELL_MOB("MOB_SPELL"),
  SPELL_MOB_AMBIENT("MOB_SPELL_AMBIENT"),
  SPELL_WITCH("WITCH"),
  DRIP_WATER("DRIPPING_WATER"),
  DRIP_LAVA("DRIPPING_LAVA"),
  VILLAGER_ANGRY("ANGRY_VILLAGER"),
  VILLAGER_HAPPY("HAPPY_VILLAGER"),
  TOWN_AURA("EFFECT"),
  NOTE("NOTE"),
  PORTAL("PORTAL"),
  ENCHANTMENT_TABLE("ENCHANT"),
  FLAME("FLAME"),
  LAVA("LAVA"),
  FOOTSTEP("POOF"),
  CLOUD("CLOUD"),
  REDSTONE("DUST"),
  SNOWBALL("SNOWBALL_POOF"),
  SNOW_SHOVEL("POOF"),
  SLIME("ITEM_SLIME"),
  HEART("HEART"),
  BARRIER("BARRIER"),
  ITEM_CRACK("ITEM"),
  BLOCK_CRACK("FALLING_DUST"),
  BLOCK_DUST("FALLING_DUST"),
  WATER_DROP("UNDERWATER"),
  ITEM_TAKE("ITEM_SNOWBALL"),
  MOB_APPEARANCE("ENTITY_EFFECT");

  private final String newName;

  ParticleEffect(String newName) {
    this.newName = newName;
  }

  public void display(Player viewer, boolean isFar, float x, float y, float z, float offSetX, float offSetY, float offSetZ, float speed, int count) {
    String finalName = ProtocolLibrary.getProtocolManager().getMinecraftVersion().getVersion().contains("1.8") ? this.name() : newName;
    BukkitUtils.displayParticle(viewer, finalName, isFar, x, y, z, offSetX, offSetY, offSetZ, speed, count);
  }

  public void display(Player viewer, ParticleOptions option, boolean isFar, float x, float y, float z, float offSetX, float offSetY, float offSetZ, float speed, int count) {
    BukkitUtils.displayParticle(viewer, option, isFar, x, y, z, offSetX, offSetY, offSetZ, speed, count);
  }

  public static ParticleEffect fromName(String name) {
      return Arrays.stream(values()).filter(e -> e.name().equals(name)).findFirst().orElse(null);
  }

}
