package me.joaomanoel.d4rkk.dev.nms.npc;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NpcEntity {

    void setName(String text);
    void kill();
    void kill(Player player);
    void setLocation(World world, double x, double y, double z);
    void spawn();
    void spawn(Player player);
    void setItemInHand(ItemStack item);
    void setShowNick(boolean showNick);
    void setShowNick(Player player);
    void setPlayerCopySkin(boolean playerCopySkin);
    void interactAtPlayer(Player player);
    boolean isCopySkin();
    Player getPlayer();
    NpcEntity setData(String key, String value);
    boolean hasData(String key);
    String getData(String key);


}