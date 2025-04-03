package me.joaomanoel.d4rkk.dev.languages;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.cosmetic.CosmeticType;
import me.joaomanoel.d4rkk.dev.languages.translates.EN_US;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GLanguage extends Cosmetic {

  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger()).getModule("LANGUAGES");
  private final String name;
  private final String icon;
  private final String description;

  public GLanguage(long id, String permission, String name, String icon, String description) {
    super(id, CosmeticType.LANGUAGE, permission);
    this.name = name;
    this.icon = icon;
    this.description = description;
  }

  public static void setupLanguage() {
    KConfig config = Core.getInstance().getConfig("LANGUAGES", "languages");

    for (String key : config.getKeys(false)) {
      long id = config.getInt(key + ".id");
      String permission = config.getString(key + ".permission");
      String name = config.getString(key + ".name");
      String icon = config.getString(key + ".icon");
      String description = config.getString(key + ".description");

//      LOGGER.info("Carregando idioma: " + name + " (ID: " + id + ")");

      new GLanguage(id, permission, name, icon, description);
    }
  }

  @Override
  public String getName() {
    return this.name;
  }



  @Override
  public ItemStack getIcon(Profile profile) {
    boolean isSelected = this.isSelected(profile);

//    if (isSelected) {
//      LOGGER.info("Idioma selecionado: " + this.name);
//    }

    KConfig config = Core.getInstance().getConfig("LANGUAGES", "languages");

    String languageKey = null;
    for (String key : config.getKeys(false)) {
      if (config.getInt(key + ".id") == this.getId()) {  // Garantindo o idioma específico
        languageKey = key;
        break;
      }
    }

    if (languageKey == null) {
//      LOGGER.warning("Idioma não encontrado para o ID: " + this.getId() + ". Usando idioma padrão.");
      languageKey = "en-us";
    }

    String desc = config.getString(languageKey + ".description");

    if (isSelected) {
      desc = desc.replace("{has_desc_status}", EN_US.language$icon$has_desc$selected);
    } else {
      desc = desc.replace("{has_desc_status}", EN_US.language$icon$has_desc$select);
    }

    desc = desc.replace("{name}", this.name);

    List<String> compatibleMinigames = config.getStringList(languageKey + ".minigames");
    StringBuilder minigamesFormatted = new StringBuilder();
    for (String minigame : compatibleMinigames) {
      minigamesFormatted.append("   §f▪ ").append(minigame).append("\n");
    }
    String minigamesList = minigamesFormatted.length() > 0 ? minigamesFormatted.toString() : "§f▪ No compatible minigames\n";
    desc = desc.replace("{available}", "\n   " + minigamesList.trim());

    String color = EN_US.cosmetics$color$unlocked;

    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + " : name>" + color + this.name + " : desc>" + desc);

    //todo: Logger pra verificar se o idioma foi criado.
    //LOGGER.info("Item criado para o idioma: " + this.name);

    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }

    return item;
  }
}
