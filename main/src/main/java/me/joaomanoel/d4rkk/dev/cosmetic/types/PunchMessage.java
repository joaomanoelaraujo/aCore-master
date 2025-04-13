package me.joaomanoel.d4rkk.dev.cosmetic.types;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.cosmetic.CosmeticType;
import me.joaomanoel.d4rkk.dev.cosmetic.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PunchMessage extends Cosmetic {

  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger()).getModule("PUNCH");
  private final String name;
  private final String icon;
  private final List<String> messages;

  public PunchMessage(long id, String permission, String name, String icon, List<String> messages) {
    super(id, CosmeticType.PUNCH, permission);
    this.name = name;
    this.icon = icon;
    this.messages = messages;
  }

  public static void setupPunchMessages() {
    KConfig config = Core.getInstance().getConfig("cosmetics", "punchmessages");

    for (String key : config.getKeys(false)) {
      long id = config.getInt(key + ".id");
      if (!config.contains(key + ".cash")) {
        config.set(key + ".cash", getAbsentProperty("punchmessages", key + ".cash"));
      }
      String permission = config.getString(key + ".permission");
      String name = config.getString(key + ".name");
      String icon = config.getString(key + ".icon");

      List<String> sound = config.getStringList(key + ".messages");

      new PunchMessage(id, permission, name, icon, sound);
    }
  }

  @Override
  public String getName() {
    return this.name;
  }

  public List<String> getMessages() {
    return messages;
  }

  public String getRandomMessage() {
    return StringUtils.formatColors(messages.get(ThreadLocalRandom.current().nextInt(messages.size())));
  }

  @Override
  public ItemStack getIcon(Profile profile) {
    boolean has = this.has(profile);
    boolean isSelected = this.isSelected(profile);

    if (isSelected && !has) {
      isSelected = false;
      profile.getAbstractContainer("aCoreProfile", "cselected", SelectedContainer.class).setSelected(getType(), 0);
    }

    Role role = Role.getRoleByPermission(this.getPermission());
    String color = has ?
            (isSelected ? LanguageAPI.getConfig(profile).getString("cosmetics.color.selected") : LanguageAPI.getConfig(profile).getString("cosmetics.color.unlocked")) :
            LanguageAPI.getConfig(profile).getString("cosmetics.color.locked");

    String desc = has ?
            LanguageAPI.getConfig(profile).getString("cosmetics.punchmessage.icon.has_desc.start").replace("{has_desc_status}", isSelected ? LanguageAPI.getConfig(profile).getString("cosmetics.icon.has_desc.selected") : LanguageAPI.getConfig(profile).getString("cosmetics.icon.has_desc.select")) :
            LanguageAPI.getConfig(profile).getString("cosmetics.punchmessage.icon.perm_desc.start")
                    .replace("{perm_desc_status}", (role == null ? LanguageAPI.getConfig(profile).getString("cosmetics.icon.perm_desc.common") : LanguageAPI.getConfig(profile).getString("cosmetics.icon.perm_desc.role").replace("{role}", role.getName())));

    desc = desc.replace("{name}", this.name);

    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + " : name>" + color + this.name + " : desc>" + desc);

    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }

    return item;
  }
}
