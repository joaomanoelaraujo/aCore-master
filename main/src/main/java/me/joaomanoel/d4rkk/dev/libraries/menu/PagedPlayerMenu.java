package me.joaomanoel.d4rkk.dev.libraries.menu;

import me.joaomanoel.d4rkk.dev.plugin.KPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PagedPlayerMenu extends PagedMenu implements Listener {
  
  protected Player player;
  
  public PagedPlayerMenu(Player player, String name) {
    this(player, name, 3);
  }
  
  public PagedPlayerMenu(Player player, String name, int rows) {
    super(name, rows);
    this.player = player;
  }
  
  public void open() {
    player.openInventory(this.menus.get(0).getInventory());
  }
  
  public void register(KPlugin plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }
  
  public void openPrevious() {
    if (this.currentPage == 1) {
      return;
    }
    
    this.currentPage--;
    player.openInventory(this.menus.get(this.currentPage - 1).getInventory());
  }
  
  public void openNext() {
    if (this.currentPage + 1 > this.menus.size()) {
      return;
    }
    
    this.currentPage++;
    player.openInventory(this.menus.get(this.currentPage - 1).getInventory());
  }
}
