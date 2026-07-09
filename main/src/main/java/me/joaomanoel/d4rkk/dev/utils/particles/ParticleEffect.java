package me.joaomanoel.d4rkk.dev.utils.particles;

import java.util.Arrays;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

  private ParticleEffect(String name) {
    this.name = name;
  }

  public void display(Player player, Location loc) {
    this.display(player, false, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
  }

  /**
   * Exibe a partícula em uma Location para um único jogador ("viewer").
   */
  public void display(float offsetX, float offsetY, float offsetZ, float speed, int count, Location loc, Player viewer) {
    this.display(viewer, false, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), offsetX, offsetY, offsetZ, speed, count);
  }

  /**
   * Exibe a partícula em uma Location para todos os jogadores dentro do raio informado (em blocos).
   */
  public void display(float offsetX, float offsetY, float offsetZ, float speed, int count, Location loc, double radius) {
    double radiusSquared = radius * radius;
    for (Player viewer : loc.getWorld().getPlayers()) {
      if (viewer.getLocation().distanceSquared(loc) <= radiusSquared) {
        this.display(viewer, false, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), offsetX, offsetY, offsetZ, speed, count);
      }
    }
  }

  public void display(Player viewer, boolean isFar, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float speed, int count) {
    BukkitUtils.displayParticle(viewer, this.name, isFar, x, y, z, offsetX, offsetY, offsetZ, speed, count);
  }

  public static ParticleEffect fromName(String name) {
    return Arrays.stream(values()).filter((e) -> e.name.equals(name)).findFirst().orElse(null);
  }
}