package me.joaomanoel.d4rkk.dev.utils.enums;

import me.joaomanoel.d4rkk.dev.utils.StringUtils;

import java.util.concurrent.ThreadLocalRandom;

public enum EnumRarity {
    神聖("§b神聖", 10),
    史詩("§6史詩", 25),
    稀有度("§d稀有度", 50),
    常見的("§9常見的", 100),
    
    DIVINE("§bDivine", 10),
    EPIC("§6Epic", 25),
    RARE("§dRare", 50),
    COMMON("§9Common", 100),
    DIVINO("§bDivino", 10),
  EPICO("§6Épico", 25),
  RARO("§dRaro", 50),
  COMUM("§9Comum", 100);

  private static final EnumRarity[] VALUES = values();
  private final String name;
  private final int percentage;

  private EnumRarity(String name, int percentage) {
    this.name = name;
    this.percentage = percentage;
  }

  public static EnumRarity getRandomRarity() {
    int random = ThreadLocalRandom.current().nextInt(100);
      for (EnumRarity rarity : VALUES) {
          if (random <= rarity.percentage) {
              return rarity;
          }
      }

    return COMUM;
  }

  public static EnumRarity fromName(String name) {
      for (EnumRarity rarity : VALUES) {
          if (rarity.name().equalsIgnoreCase(name)) {
              return rarity;
          }
      }

    return COMUM;
  }

  public String getName() {
    return this.name;
  }

  public String getColor() {
    return StringUtils.getFirstColor(this.getName());
  }

  public String getTagged() {
    return this.getColor() + "[" + StringUtils.stripColors(this.getName()) + "]";
  }

  private static EnumRarity[] $values() {
    return new EnumRarity[]{DIVINO, EPICO, RARO, COMUM};
  }
}
