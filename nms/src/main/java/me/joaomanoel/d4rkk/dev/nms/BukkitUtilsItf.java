package me.joaomanoel.d4rkk.dev.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface BukkitUtilsItf {

    void openBook(Player player, ItemStack book);
    ItemStack deserializeItemStack(String item);
    String serializeItemStack(ItemStack item);
    ItemStack putProfileOnSkull(Player player, ItemStack head);
    ItemStack putProfileOnSkull(Object profile, ItemStack head);
    ItemStack applyNTBTag(ItemStack item, List<Object> lines);
    void putGlowEnchantment(ItemStack item);
    void displayParticle(Player viewer, String particleName, boolean isFar, float x, float y, float z, float offSetX, float offSetY, float offSetZ, float speed, int count);

}
