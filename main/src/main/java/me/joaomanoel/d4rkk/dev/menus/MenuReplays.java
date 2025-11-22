package me.joaomanoel.d4rkk.dev.menus;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.libraries.menu.PagedPlayerMenu;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.replay.ConfigManager;
import me.joaomanoel.d4rkk.dev.replay.MySQLDatabase;
import me.joaomanoel.d4rkk.dev.replay.ReplaySaver;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

public class MenuReplays extends PagedPlayerMenu {
    private final Map<ItemStack, String> replayIds = new HashMap<>();

    public MenuReplays(Profile profile) {
        super(profile.getPlayer(), "Recent Games", 5);
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29);
        this.previousPage = this.rows * 9 - 9;
        this.nextPage = this.rows * 9 - 1;

        this.removeSlotsWith(BukkitUtils.deserializeItemStack("351:1 : 1 : name>§cVoltar"), this.rows * 9 - 5);

        List<ItemStack> items = getItemsFromDatabase(profile.getPlayer());
        this.setItems(items);


        this.register(Core.getInstance());
        this.open();
    }

    private List<ItemStack> getItemsFromDatabase(Player player) {
        List<ItemStack> items = new ArrayList<>();
        try {
            MySQLDatabase database = ConfigManager.getMySQLDatabase();
            if (database == null || database.getConnection() == null) {
//                Core.getInstance().getLogger().warning("Banco de dados não inicializado!");
                return items;
            }

            PreparedStatement pst = database.getConnection().prepareStatement(
                    "SELECT id, creator, duration, time, game_name, mode, map, server, players FROM replays"
            );

            ResultSet rs = database.query(pst);
            while (rs.next()) {
                String id2 = Objects.equals(rs.getString("game_name"), "bw") ? "Bed Wars" : Objects.equals(rs.getString("game_name"), "dl") ? "Duels" : "Sky Wars";
                String id = rs.getString("id");
                String creator = rs.getString("creator");
                int duration = rs.getInt("duration");
                long time = rs.getLong("time");
                String gameName = rs.getString("game_name");
                String mode = rs.getString("mode");
                String map = rs.getString("map");
                String server = rs.getString("server");
                int players = rs.getInt("players");

                if (!creator.contains(player.getName())) continue;

                Material icon = getIconForGame(gameName);
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(time));

                ItemStack item = BukkitUtils.deserializeItemStack(
                        icon.name() + " : 1 : name>&a" + id2 + " : desc>§8" + formattedDate + " | §8" + duration + "\n\n§7Mode: §a" + mode + "\n§7Map: §a" + map +
                                "\n\n§7Server: §a" + server + "\n§7Players: §a" + players + "\n\n§eClick to view replay!");

                items.add(item);
                replayIds.put(item, id);
            }

            pst.close();
        } catch (Exception e) {
            Core.getInstance().getLogger().warning("Erro ao carregar replays: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

    private Material getIconForGame(String gameName) {
        if (gameName == null) return Material.BOOK;
        String name = gameName.toLowerCase();

        if (name.contains("bw")) return Material.BED;
        if (name.contains("sw")) return Material.EYE_OF_ENDER;
        if (name.contains("dl")) return Material.DIAMOND_SWORD;

        return Material.BOOK;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (!evt.getInventory().equals(this.getCurrentInventory())) return;
        evt.setCancelled(true);

        if (!evt.getWhoClicked().equals(this.player)) return;

        ItemStack item = evt.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
            this.player.closeInventory();
            return;
        }

        int slot = evt.getSlot();
        if (slot == this.previousPage) {
            EnumSound.CLICK.play(this.player, 0.5f, 2.0f);
            this.openPrevious();
            return;
        }

        if (slot == this.nextPage) {
            EnumSound.CLICK.play(this.player, 0.5f, 2.0f);
            this.openNext();
            return;
        }

        if (slot == this.rows * 9 - 5) {
            EnumSound.CLICK.play(this.player, 0.5f, 2.0f);
            new MenuProfile(profile);
            return;
        }

        // Quando clicar em um replay
        String replayId = replayIds.get(item);
        if (replayId != null) {
            this.player.closeInventory();
            join(replayId);
        }
    }

    private void join(String replay) {
        if (ReplaySaver.exists(replay)) {
            this.player.sendMessage("§aCarregando replay #" + replay + "...");
            try {
                Profile.getProfile(this.player.getName()).setHotbar(null);
                ReplaySaver.load(replay, r -> r.play(this.player));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.player.sendMessage("§cReplay não encontrado!");
        }
    }

    public void cancel() {
        HandlerList.unregisterAll((Listener) this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        if (evt.getPlayer().equals(this.player)) this.cancel();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory()))
            this.cancel();
    }
}
