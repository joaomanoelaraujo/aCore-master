/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers$TitleAction
 *  com.comphenix.protocol.wrappers.WrappedChatComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.replay.ItemConfig;
import me.joaomanoel.d4rkk.dev.replay.ItemConfigOption;
import me.joaomanoel.d4rkk.dev.replay.ItemConfigType;
import me.joaomanoel.d4rkk.dev.replay.ReflectionHelper;
import me.joaomanoel.d4rkk.dev.replay.Replayer;
import me.joaomanoel.d4rkk.dev.replay.VersionUtil;
import com.comphenix.packetwrapper.WrapperPlayServerTitle;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ReplayHelper {
    public static HashMap<String, Replayer> replaySessions = new HashMap<>();

    public static ItemStack createItem(ItemConfigOption option) {
        String displayName = ChatColor.translateAlternateColorCodes('&', option.getName());
        ItemStack stack = ReplayHelper.createItem(option.getMaterial(), displayName, option.getData());
        if (option.getOwner() != null && stack.getItemMeta() instanceof SkullMeta) {
            SkullMeta meta = (SkullMeta)stack.getItemMeta();
            meta.setOwner(option.getOwner());
            meta.setDisplayName(displayName);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    public static ItemStack createItem(Material material, String name, int data) {
        ItemStack stack = new ItemStack(material, 1, (byte)data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getPauseItem() {
        return ReplayHelper.createItem(ItemConfig.getItem(ItemConfigType.PAUSE));
    }

    public static ItemStack getResumeItem() {
        return ReplayHelper.createItem(ItemConfig.getItem(ItemConfigType.RESUME));
    }

    public static void createTeleporter(Player player, Replayer replayer) {
        Inventory inv = Bukkit.createInventory(null, replayer.getNPCList().size() / 9 > 0 ? (int)Math.floor((double) replayer.getNPCList().size() / 9) * 9 : 9, "§7Teleporter");
        int index = 0;
        for (String name : replayer.getNPCList().keySet()) {
            ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta)stack.getItemMeta();
            meta.setDisplayName("§6" + name);
            meta.setOwner(name);
            stack.setItemMeta(meta);
            inv.setItem(index, stack);
            ++index;
        }
        player.openInventory(inv);
    }

    public static void sendTitle(Player player, String title, String subTitle, int stay) {
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_20)) {
            ReflectionHelper.getInstance().sendTitle(player, title, subTitle, 0, stay, 20);
            return;
        }

        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17)) {
            ReflectionHelper.getInstance().sendTitle(player, title, subTitle, 0, stay, 20);
            return;
        }

        try {
            WrapperPlayServerTitle packet = new WrapperPlayServerTitle();
            packet.setAction(EnumWrappers.TitleAction.TIMES);
            packet.setStay(stay);
            packet.setFadeIn(0);
            packet.setFadeOut(20);
            packet.sendPacket(player);

            if (subTitle != null) {
                WrapperPlayServerTitle sub = new WrapperPlayServerTitle();
                sub.setAction(EnumWrappers.TitleAction.SUBTITLE);
                sub.setTitle(WrappedChatComponent.fromText(subTitle));
                sub.sendPacket(player);
            }

            WrapperPlayServerTitle titlePacket = new WrapperPlayServerTitle();
            titlePacket.setAction(EnumWrappers.TitleAction.TITLE);
            titlePacket.setTitle(title != null ? WrappedChatComponent.fromText(title) : WrappedChatComponent.fromText(""));
            titlePacket.sendPacket(player);
        } catch (Exception e) {
            ReflectionHelper.getInstance().sendTitle(player, title, subTitle, 0, stay, 20);
        }
    }


    public static boolean isInRange(Location loc1, Location loc2) {
        return loc1.getWorld().getName().equals(loc2.getWorld().getName()) && loc1.distance(loc2) <= 48.0;
    }
}

