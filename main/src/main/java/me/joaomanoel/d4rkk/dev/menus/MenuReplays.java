package me.joaomanoel.d4rkk.dev.menus;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.libraries.menu.PagedPlayerMenu;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.replay.ReplayInfo;
import me.joaomanoel.d4rkk.dev.replay.ReplaySaver;
import me.joaomanoel.d4rkk.dev.replay.Replays;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuReplays extends PagedPlayerMenu {
    private final Map<ItemStack, String> replayrs = new HashMap<ItemStack, String>();

    public MenuReplays(Profile profile, String name) {
        super(profile.getPlayer(), name, 5);
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29);
        this.previousPage = this.rows * 9 - 9;
        this.nextPage = this.rows * 9 - 1;
        ArrayList<ItemStack> item = new ArrayList<ItemStack>();
        if (Core.minigame.equals("Bed Wars")) {
            for (ReplayInfo list : Objects.requireNonNull(Replays.getReplays())) {
                for (String playerName : list.getCreator()) {
                    if (Arrays.stream(playerName.replace("[", "").replace("]", "").split(", ")).filter(nome -> nome.contains(profile.getPlayer().getName())).findAny().orElse(null) == null) continue;
                    ItemStack icon = BukkitUtils.deserializeItemStack("PAPER : 1 : name>&aReplay #" + list.getID() + " : desc>&7\n&7Duração: &e" + list.getDuration() / 20 + "\n\n&eClique para assistir!");
                    item.add(icon);
                    this.replayrs.put(icon, icon.getItemMeta().getDisplayName());
                }
            }

                    this.removeSlotsWith(BukkitUtils.deserializeItemStack("351:1 : 1 : name>§cVoltar"), this.rows * 9 - 5);
            }

        this.setItems(item);
        item.clear();
        this.register(Core.getInstance());
        this.open();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getInventory().equals(this.getCurrentInventory())) {
            evt.setCancelled(true);
            if (evt.getWhoClicked().equals(this.player)) {
                ItemStack item;
                Profile profile = Profile.getProfile(this.player.getName());
                if (profile == null) {
                    this.player.closeInventory();
                    return;
                }
                if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory()) && (item = evt.getCurrentItem()) != null && item.getType() != Material.AIR) {
                    if (evt.getSlot() == this.previousPage) {
                        EnumSound.CLICK.play(this.player, 0.5f, 2.0f);
                        this.openPrevious();
                    } else if (evt.getSlot() == this.nextPage) {
                        EnumSound.CLICK.play(this.player, 0.5f, 2.0f);
                        this.openNext();
                    } else if (evt.getSlot() == this.rows * 9 - 5) {
                        EnumSound.CLICK.play(this.player, 0.5f, 2.0f);
                       new MenuProfile(profile);
                    }
                    String replayName = this.replayrs.get(item).replace("Replay #", "").replace("&a", "").replace("§a", "");
                    this.player.closeInventory();
                    this.join(replayName);
                }
            }
        }
    }

    public void join(String replay) {
        if (ReplaySaver.exists(replay)) {
            this.player.sendMessage("§aCarregando...");
            try {
                Profile.getProfile(this.player.getName()).setHotbar(null);
                ReplaySaver.load(replay, replaya -> replaya.play(this.player));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel() {
        HandlerList.unregisterAll((Listener)this);
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

