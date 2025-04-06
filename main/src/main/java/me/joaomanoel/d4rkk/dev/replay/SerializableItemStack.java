/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.io.Serializable;
import java.util.Map;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class SerializableItemStack
implements Serializable {
    private static final long serialVersionUID = 6849346246817837690L;
    private Map<String, Object> itemStack;
    private boolean hasEnchantment;
    private int color;
    private boolean hasColor;

    public SerializableItemStack() {
    }

    public SerializableItemStack(Map<String, Object> itemStack) {
        this.itemStack = itemStack;
    }

    public SerializableItemStack(Map<String, Object> itemStack, boolean hasEnchantment, int color, boolean hasColor) {
        this(itemStack);
        this.hasEnchantment = hasEnchantment;
        this.color = color;
        this.hasColor = hasColor;
    }

    public Map<String, Object> getItemStack() {
        return this.itemStack;
    }

    public int getColor() {
        return this.color;
    }

    public boolean hasEnchantment() {
        return this.hasEnchantment;
    }

    public boolean hasColor() {
        return this.hasColor;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setHasColor(boolean hasColor) {
        this.hasColor = hasColor;
    }

    public void setHasEnchantment(boolean hasEnchantment) {
        this.hasEnchantment = hasEnchantment;
    }

    public void setItemStack(Map<String, Object> itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack toItemStack() {
        ItemStack stack = ItemStack.deserialize(this.itemStack);
        if (this.hasEnchantment) {
            stack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }
        if (this.hasColor) {
            LeatherArmorMeta meta = (LeatherArmorMeta)stack.getItemMeta();
            meta.setColor(Color.fromRGB((int)this.color));
            stack.setItemMeta((ItemMeta)meta);
        }
        return stack;
    }

    public static SerializableItemStack fromItemStack(ItemStack stack) {
        return SerializableItemStack.fromItemStack(stack, false);
    }

    public static SerializableItemStack fromMaterial(Material mat) {
        return SerializableItemStack.fromItemStack(new ItemStack(mat), false);
    }

    public static SerializableItemStack fromItemStack(ItemStack stack, boolean block) {
        Map serialized = stack.serialize();
        if (block && stack.getType() == Material.FLINT_AND_STEEL) {
            serialized.put("type", "FIRE");
        }
        serialized.entrySet().removeIf(e -> !(e.toString()).equalsIgnoreCase("v") && !(e.toString()).equalsIgnoreCase("type") && !(e.toString()).equalsIgnoreCase("damage"));
        SerializableItemStack serializableItemStack = new SerializableItemStack(serialized);
        if (!block) {
            serializableItemStack.setHasEnchantment(stack.getEnchantments().size() > 0);
            if (stack.hasItemMeta() && stack.getItemMeta() instanceof LeatherArmorMeta) {
                LeatherArmorMeta meta = (LeatherArmorMeta)stack.getItemMeta();
                serializableItemStack.setHasColor(true);
                serializableItemStack.setColor(meta.getColor().asRGB());
            }
        }
        return serializableItemStack;
    }
}

