/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers$ItemSlot
 *  com.comphenix.protocol.wrappers.Pair
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package me.joaomanoel.d4rkk.dev.replay;



import com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment;
import com.comphenix.protocol.wrappers.EnumWrappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class NPCManager {
    public static List<String> names = new ArrayList<String>();
    private static final List<Material> ARMOR = Arrays.asList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS);

    public static List<com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment> updateEquipmentv16(int id, InvData data) {
/*        ArrayList<Pair> items = new ArrayList<>();
        items.add(new Pair(EnumWrappers.ItemSlot.HEAD, NPCManager.fromID(data.getHead())));
        items.add(new Pair(EnumWrappers.ItemSlot.CHEST, NPCManager.fromID(data.getChest())));
        items.add(new Pair(EnumWrappers.ItemSlot.LEGS, NPCManager.fromID(data.getLeg())));
        items.add(new Pair(EnumWrappers.ItemSlot.FEET, NPCManager.fromID(data.getBoots())));
        items.add(new Pair(EnumWrappers.ItemSlot.MAINHAND, NPCManager.fromID(data.getMainHand())));
        items.add(new Pair(EnumWrappers.ItemSlot.OFFHAND, NPCManager.fromID(data.getOffHand())));*/
        WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment();
        packet.setEntityID(id);

        return Collections.singletonList(packet);
    }

    public static List<WrapperPlayServerEntityEquipment> updateEquipment(int id, InvData data) {
        ArrayList<WrapperPlayServerEntityEquipment> list = new ArrayList<WrapperPlayServerEntityEquipment>();
        WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment();
        packet.setEntityID(id);
        packet.setSlot(EnumWrappers.ItemSlot.HEAD.ordinal());
        packet.setItem(NPCManager.fromID(data.getHead()));
        list.add(packet);
        WrapperPlayServerEntityEquipment packet1 = new WrapperPlayServerEntityEquipment();
        packet1.setEntityID(id);
        packet1.setSlot(EnumWrappers.ItemSlot.CHEST.ordinal());
        packet1.setItem(NPCManager.fromID(data.getChest()));
        list.add(packet1);
        WrapperPlayServerEntityEquipment packet2 = new WrapperPlayServerEntityEquipment();
        packet2.setEntityID(id);
        packet2.setSlot(EnumWrappers.ItemSlot.LEGS.ordinal());
        packet2.setItem(NPCManager.fromID(data.getLeg()));
        list.add(packet2);
        WrapperPlayServerEntityEquipment packet3 = new WrapperPlayServerEntityEquipment();
        packet3.setEntityID(id);
        packet3.setSlot(EnumWrappers.ItemSlot.FEET.ordinal());
        packet3.setItem(NPCManager.fromID(data.getBoots()));
        list.add(packet3);
        WrapperPlayServerEntityEquipment packet4 = new WrapperPlayServerEntityEquipment();
        packet4.setEntityID(id);
        packet4.setSlot(EnumWrappers.ItemSlot.MAINHAND.ordinal());
        packet4.setItem(NPCManager.fromID(data.getMainHand()));
        list.add(packet4);
        if (!VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            WrapperPlayServerEntityEquipment packet5 = new WrapperPlayServerEntityEquipment();
            packet5.setEntityID(id);
            packet5.setSlot(EnumWrappers.ItemSlot.OFFHAND.ordinal());
            packet5.setItem(NPCManager.fromID(data.getOffHand()));
            list.add(packet5);
        }
        return list;
    }

    public static List<com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment> updateEquipmentOld(int id, InvData data) {
        ArrayList<com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment> list = new ArrayList<com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment>();
        com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment packet = new com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment();
        packet.setEntityID(id);
        packet.setSlot(4);
        packet.setItem(NPCManager.fromID(data.getHead()));
        list.add(packet);
        com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment packet1 = new com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment();
        packet1.setEntityID(id);
        packet1.setSlot(3);
        packet1.setItem(NPCManager.fromID(data.getChest()));
        list.add(packet1);
        com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment packet2 = new com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment();
        packet2.setEntityID(id);
        packet2.setSlot(2);
        packet2.setItem(NPCManager.fromID(data.getLeg()));
        list.add(packet2);
        com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment packet3 = new com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment();
        packet3.setEntityID(id);
        packet3.setSlot(1);
        packet3.setItem(NPCManager.fromID(data.getBoots()));
        list.add(packet3);
        com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment packet4 = new com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment();
        packet4.setEntityID(id);
        packet4.setSlot(0);
        packet4.setItem(NPCManager.fromID(data.getMainHand()));
        list.add(packet4);
        return list;
    }

    public static ItemStack fromID(ItemData data) {
        if (data == null) {
            return new ItemStack(Material.AIR);
        }
        if (data.getItemStack() != null) {
            return data.getItemStack().toItemStack();
        }
        return new ItemStack(MaterialBridge.fromID(data.getId()), 1, (short)data.getSubId());
    }

    public static ItemData fromItemStack(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        return new ItemData(SerializableItemStack.fromItemStack(stack));
    }

    public static InvData copyFromPlayer(Player player, boolean armor, boolean off) {
        InvData data = new InvData();
        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            data.setMainHand(NPCManager.fromItemStack(player.getItemInHand()));
        } else {
            data.setMainHand(NPCManager.fromItemStack(player.getInventory().getItemInHand()));
            if (off) {
                data.setOffHand(NPCManager.fromItemStack(player.getInventory().getItemInHand()));
            }
        }
        if (armor) {
            data.setHead(NPCManager.fromItemStack(player.getInventory().getHelmet()));
            data.setChest(NPCManager.fromItemStack(player.getInventory().getChestplate()));
            data.setLeg(NPCManager.fromItemStack(player.getInventory().getLeggings()));
            data.setBoots(NPCManager.fromItemStack(player.getInventory().getBoots()));
        }
        return data;
    }

    public static boolean isArmor(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        return ARMOR.contains(stack.getType()) || NPCManager.isNetheriteArmor(stack);
    }

    public static boolean isNetheriteArmor(ItemStack stack) {
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_16)) {
            String material = NPCManager.getMaterialName(stack);
            return material.equals("NETHERITE_HELMET") || material.equals("NETHERITE_CHESTPLATE") || material.equals("NETHERITE_LEGGINGS") || material.equals("NETHERITE_BOOTS");
        }
        return false;
    }

    public static String getMaterialName(ItemStack stack) {
        return (String)stack.serialize().get("type");
    }

    public static String getArmorType(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        if (NPCManager.getMaterialName(stack).contains("HELMET")) {
            return "head";
        }
        if (NPCManager.getMaterialName(stack).contains("CHESTPLATE")) {
            return "chest";
        }
        if (NPCManager.getMaterialName(stack).contains("LEGGINGS")) {
            return "leg";
        }
        if (NPCManager.getMaterialName(stack).contains("BOOTS")) {
            return "boots";
        }
        return null;
    }

    public static boolean wearsArmor(Player p, String type) {
        PlayerInventory inv = p.getInventory();
        if (type == null) {
            return false;
        }
        if (type.equals("head") && NPCManager.isArmor(inv.getHelmet())) {
            return true;
        }
        if (type.equals("chest") && NPCManager.isArmor(inv.getChestplate())) {
            return true;
        }
        if (type.equals("leg") && NPCManager.isArmor(inv.getLeggings())) {
            return true;
        }
        return type.equals("boots") && NPCManager.isArmor(inv.getBoots());
    }
}

