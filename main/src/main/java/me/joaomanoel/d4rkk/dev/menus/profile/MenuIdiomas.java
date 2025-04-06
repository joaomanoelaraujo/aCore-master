package me.joaomanoel.d4rkk.dev.menus.profile;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.libraries.menu.PagedPlayerMenu;
import me.joaomanoel.d4rkk.dev.menus.MenuProfile;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
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

public class MenuIdiomas extends PagedPlayerMenu {

    private Map<ItemStack, String> IDIOMAS = new HashMap<>();

    public MenuIdiomas(Profile profile) {
        super(profile.getPlayer(), "Selecionar Idiomas", 4);
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19);
        this.removeSlotsWith(BukkitUtils.deserializeItemStack(LanguageAPI.getConfig(profile).getString("menu.back")), 31);
        List<ItemStack> items = new ArrayList<>();
        for (String key : LanguageAPI.listAllKeys()) {
            String color = profile.getLanguageContainer().getLanguage().equals(key) ? "&6" : "&e";
            ItemStack icon = BukkitUtils.deserializeItemStack("DIAMOND : 1 : name>" + color + key);
            IDIOMAS.put(icon, key);
            items.add(icon);
        }

        this.setItems(items);
        this.register(Core.getInstance());
        this.open();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getInventory().equals(this.getCurrentInventory())) {
            evt.setCancelled(true);

            if (evt.getWhoClicked().equals(this.player)) {
                Profile profile = Profile.getProfile(this.player.getName());
                if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
                    ItemStack item = evt.getCurrentItem();
                    if (item != null && item.getType() != Material.AIR) {
                        if (evt.getSlot() == 31) {
                            new MenuProfile(profile);
                            EnumSound.CLICK.play(this.player, 0.5F, 1.0F);
                            return;
                        }

                        String key = IDIOMAS.getOrDefault(item, null);
                        if (key != null) {
                            profile.getLanguageContainer().changeLanguage(key);
                            new MenuIdiomas(profile);
                        }
                    }
                }
            }
        }
    }

    public void cancel() {
        IDIOMAS.clear();
        IDIOMAS = null;
        HandlerList.unregisterAll(this);
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
