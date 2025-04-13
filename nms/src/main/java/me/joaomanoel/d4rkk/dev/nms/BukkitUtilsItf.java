package me.joaomanoel.d4rkk.dev.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BukkitUtilsItf {

    void openBook(Player player, ItemStack book);
    ItemStack deserializeItemStack(String item);
    String serializeItemStack(ItemStack item);
    ItemStack putProfileOnSkull(Player player, ItemStack head);
    ItemStack putProfileOnSkull(Object profile, ItemStack head);
    void putGlowEnchantment(ItemStack item);

}
