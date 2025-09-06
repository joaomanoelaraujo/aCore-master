package me.joaomanoel.d4rkk.dev.menus.others;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.cosmetic.CosmeticType;
import me.joaomanoel.d4rkk.dev.cosmetic.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.cosmetic.types.ColoredTag;
import me.joaomanoel.d4rkk.dev.cosmetic.types.JoinMessage;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.libraries.menu.PagedPlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.buy.MenuBuyCashCosmetic;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MenuCosmeticNoback<T extends Cosmetic> extends PagedPlayerMenu {

  private Class<T> cosmeticClass;
  private Map<ItemStack, T> cosmetics = new HashMap<>();
  public MenuCosmeticNoback(Profile profile, String name, Class<T> cosmeticClass) {
    super(profile.getPlayer(), name, (Cosmetic.listByType(cosmeticClass).size() / 7) + 6);
    this.cosmeticClass = cosmeticClass;
    this.previousPage = (this.rows * 9) - 9;
    this.nextPage = (this.rows * 9) - 1;
    this.onlySlots(19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

    this.removeSlotsWith(BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.cosmetic.back")), LanguageAPI.getConfig(profile).getInt("menu.cosmetic_slot.back"));


    String color = "§a";
    StringBuilder sb1 = new StringBuilder();
    Optional<JoinMessage> selectedCosmetic8 = Cosmetic.listByType(JoinMessage.class)
            .stream()
            .map(f -> profile.getAbstractContainer("aCoreProfile", "cselected", SelectedContainer.class).getSelected(f.getType(), JoinMessage.class))
            .filter(Objects::nonNull)
            .findFirst();
    selectedCosmetic8.ifPresent(cosmetic -> sb1.append("\n").append(" &a").append(cosmetic.getName()));


    List<JoinMessage> deathhologram = Cosmetic.listByType(JoinMessage.class);
    long max = deathhologram.size();
    long owned = deathhologram.stream().filter(deathholograms -> deathholograms.has(profile)).count();
    long percentage = max == 0 ? 100 : (owned * 100) / max;
    deathhologram.clear();
    String joinMessageDesc = LanguageAPI.getConfig(profile).getString("menucosmetics.join_message")
            .replace("{color}", color)
            .replace("{owned}", String.valueOf(owned))
            .replace("{max}", String.valueOf(max))
            .replace("{percentage}", String.valueOf(percentage))
            .replace("{selected}", sb1.toString().isEmpty() ? "\n &aNone" : sb1.toString());

    this.removeSlotsWith(BukkitUtils.deserializeItemStack(joinMessageDesc), 4);


    // Outros itens
    this.removeSlotsWith(BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menucosmetics.empty_slot")), 9);

    // Configuração de slots específicos com base nos cosméticos
    if (this.cosmeticClass.equals(JoinMessage.class)) {
      for (int i = 9; i <= 17; i++) {
        if (i == 13) {
          this.removeSlotsWith(BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menucosmetics.empty_slot").replace("160:7", "160:5")), i);
        } else {
          this.removeSlotsWith(BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menucosmetics.empty_slot")), i);
        }
      }
    } else if (this.cosmeticClass.equals(ColoredTag.class)) {
      for (int i = 9; i <= 17; i++) {
        if (i == 12) {
          this.removeSlotsWith(BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menucosmetics.empty_slot").replace("160:7", "160:5")), i);
        } else {
          this.removeSlotsWith(BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menucosmetics.empty_slot")), i);
        }
      }
    }

    List<ItemStack> items = new ArrayList<>();
    List<T> cosmetics = Cosmetic.listByType(cosmeticClass);
    for (T cosmetic : cosmetics) {
      ItemStack icon = cosmetic.getIcon(profile);
      items.add(icon);
      this.cosmetics.put(icon, cosmetic);
    }

    this.setItems(items);
    cosmetics.clear();
    items.clear();

    this.register(Core.getInstance());
    this.open();
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getCurrentInventory())) {
      evt.setCancelled(true);

      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }

        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
          ItemStack item = evt.getCurrentItem();

          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == this.previousPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openPrevious();
              } else if (evt.getSlot() == 4) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuCosmeticNoback<>(profile, LanguageAPI.getConfig(profile).getString("cosmetic.join_message_name"), JoinMessage.class);
            } else if (evt.getSlot() == 0) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              } else if (evt.getSlot() == LanguageAPI.getConfig(profile).getInt("menu.cosmetic_slot.back")) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                player.closeInventory();
            } else if (evt.getSlot() == this.nextPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openNext();
            }else {
              T cosmetic = this.cosmetics.get(item);
              if (cosmetic != null) {
                if (evt.isRightClick()) {
                  if (cosmetic.getType() == CosmeticType.JOIN_MESSAGE) {
                    player.sendMessage(LanguageAPI.getConfig(profile).getString("message.deathmessage"));
                    ((JoinMessage) cosmetic).getMessages().forEach(message -> player.sendMessage(" §8▪ " + StringUtils.formatColors(message.replace("{player}", "§7Player"))));
                    player.sendMessage("");
                    return;
                  } else if (cosmetic.getType() == CosmeticType.COLORED_TAG) {
                    String colorCode = ((ColoredTag) cosmetic).getColor();

                    // Replace the {color} placeholder in the message
                    String message = LanguageAPI.getConfig(profile).getString("message.coloredtag").replace("{color}", "").replace("{role}", Role.getPrefixed(player.getName(), colorCode));

                    // Send the message to the player
                    player.sendMessage(message);
                    return;
                  }
                }

                if (!cosmetic.has(profile)) {
                  if (!cosmetic.canBuy(this.player) ||  (CashManager.CASH && profile
                          .getStats("aCoreProfile", "cash") < cosmetic.getCash())) {
                    EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                    return;
                  }

                  EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                  if (CashManager.CASH || cosmetic.getCash() >= 0) {
                    new MenuBuyCashCosmetic<>(profile, this.name.replace("Profile - ", ""), cosmetic, this.cosmeticClass);
                  }
                  return;
                }

                if (!cosmetic.canBuy(this.player)) {
                  EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                  this.player.sendMessage(LanguageAPI.getConfig(profile).getString("message.nohaveperm"));
                  return;
                }

                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                if (cosmetic.isSelected(profile)) {
                  profile.getAbstractContainer("aCoreProfile", "cselected", SelectedContainer.class).setSelected(cosmetic.getType(), 0);
                } else {
                  profile.getAbstractContainer("aCoreProfile", "cselected", SelectedContainer.class).setSelected(cosmetic);
                }

                new MenuCosmeticNoback<>(profile, this.name.replace("Profile - ", ""), this.cosmeticClass);
              }
            }
          }
        }
      }
    }
  }

  public void cancel() {
    HandlerList.unregisterAll(this);
    this.cosmeticClass = null;
    this.cosmetics.clear();
    this.cosmetics = null;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
      this.cancel();
    }
  }
}
