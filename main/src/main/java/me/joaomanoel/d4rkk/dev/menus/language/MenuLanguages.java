package me.joaomanoel.d4rkk.dev.menus.language;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.cosmetic.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.libraries.menu.PagedPlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.MenuProfile;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuLanguages<T extends Cosmetic> extends PagedPlayerMenu {
  private static final int ITEMS_PER_ROW = 7;
  private static final int MENU_ROWS = 6;
  private static final int[] VALID_SLOTS = {
          10, 11, 12, 13, 14, 15, 16,
          19, 20, 21, 22, 23, 24, 25,
          28, 29, 30, 31, 32, 33, 34
  };

  private final Class<T> cosmeticClass;
  private final Map<ItemStack, T> cosmetics;
  private final Profile profile;

  public MenuLanguages(Profile profile, String name, Class<T> cosmeticClass) {
    super(profile.getPlayer(), name, calculateRows(Cosmetic.listByType(cosmeticClass).size()));
    this.profile = profile;
    this.cosmeticClass = cosmeticClass;
    this.cosmetics = new HashMap<>();

    setupNavigationButtons();
    setupMenuItems();

    this.register(Core.getInstance());
    this.open();
  }

  private static int calculateRows(int itemCount) {
    return Math.min(MENU_ROWS, (itemCount / ITEMS_PER_ROW) + 4);
  }

  private void setupNavigationButtons() {
    this.previousPage = (this.rows * 9) - 9;
    this.nextPage = (this.rows * 9) - 1;
    this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

    // Back button
    ItemStack backButton = BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu$cosmetic$back")
    );
    this.removeSlotsWith(backButton, (this.rows * 9) - 5);
  }

  private void setupMenuItems() {
    List<ItemStack> items = new ArrayList<>();
    List<T> cosmeticsList = Cosmetic.listByType(cosmeticClass);

    for (T cosmetic : cosmeticsList) {
      if (!cosmetic.has(profile)) {
        cosmetic.give(profile);
      }

      ItemStack icon = createCosmeticIcon(cosmetic);
      items.add(icon);
      this.cosmetics.put(icon, cosmetic);
    }

    this.setItems(items);
  }

  private ItemStack createCosmeticIcon(T cosmetic) {
    ItemStack icon = cosmetic.getIcon(profile);

    if (cosmetic.isSelected(profile)) {
      BukkitUtils.putGlowEnchantment(icon);
    }

    return icon;
  }


  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (!evt.getInventory().equals(this.getCurrentInventory())) return;
    evt.setCancelled(true);

    if (!evt.getWhoClicked().equals(this.player)) return;

    Profile profile = Profile.getProfile(this.player.getName());
    if (profile == null) {
      this.player.closeInventory();
      return;
    }

    if (evt.getClickedInventory() == null || !evt.getClickedInventory().equals(this.getCurrentInventory())) return;

    ItemStack item = evt.getCurrentItem();
    if (item == null || item.getType() == Material.AIR) return;

    handleMenuClick(evt.getSlot(), item, profile);
  }

  private void handleMenuClick(int slot, ItemStack item, Profile profile) {
    if (slot == this.previousPage) {
      playClickSound();
      this.openPrevious();
    } else if (slot == this.nextPage) {
      playClickSound();
      this.openNext();
    } else if (slot == (this.rows * 9) - 5) {
      playClickSound();
      new MenuProfile(profile);
    } else {
      handleCosmeticSelection(item, profile);
    }
  }

  private void handleCosmeticSelection(ItemStack item, Profile profile) {
    T cosmetic = this.cosmetics.get(item);
    if (cosmetic == null) return;

    playPickupSound();

    SelectedContainer selectedContainer = profile.getAbstractContainer(
            "aCoreProfile",
            "cselected",
            SelectedContainer.class
    );

    if (cosmetic.isSelected(profile)) {
      selectedContainer.setSelected(cosmetic.getType(), 1);
    } else {
      selectedContainer.setSelected(cosmetic);
    }

    // Refresh menu
    new MenuLanguages<>(profile, this.name, this.cosmeticClass);
  }

  private void playClickSound() {
    EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
  }

  private void playPickupSound() {
    EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
  }

  public void cancel() {
    HandlerList.unregisterAll(this);
    this.cosmetics.clear();
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
