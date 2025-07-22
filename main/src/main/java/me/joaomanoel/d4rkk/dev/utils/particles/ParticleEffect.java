package me.joaomanoel.d4rkk.dev.utils.particles;

import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
  REDSTONE("dust"),
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

  /**
   * Exibe a partícula na localização especificada,
   * usando valores padrão: isFar=false, count=1, offsets=0, speed=0
   */
  public void display(Player player, Location loc) {
    display(
            player,
            false,
            (float) loc.getX(),
            (float) loc.getY(),
            (float) loc.getZ(),
            0.0f,   // offsetX
            0.0f,   // offsetY
            0.0f,   // offsetZ
            0.0f,   // speed (extra)
            1       // count
    );
  }

  /**
   * Método original que envia o pacote via BukkitUtils (NMS/reflection)
   */
  public void display(Player viewer,
                      boolean isFar,
                      float x, float y, float z,
                      float offsetX, float offsetY, float offsetZ,
                      float speed,
                      int count) {
    BukkitUtils.displayParticle(
            viewer,
            this.name,
            isFar,
            x, y, z,
            offsetX, offsetY, offsetZ,
            speed,
            count
    );
  }

  /**
   * Recupera a enum a partir do nome configurado no YAML
   */
  public static ParticleEffect fromName(String name) {
    return Arrays.stream(values())
            .filter(e -> e.name.equals(name))
            .findFirst()
            .orElse(null);
  }

}
