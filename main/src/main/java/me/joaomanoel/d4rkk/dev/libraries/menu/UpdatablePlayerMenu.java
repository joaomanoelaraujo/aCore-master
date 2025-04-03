package me.joaomanoel.d4rkk.dev.libraries.menu;

import me.joaomanoel.d4rkk.dev.plugin.KPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class UpdatablePlayerMenu extends UpdatableMenu implements Listener {
  
  protected Player player;
  private BukkitTask task;
  
  public UpdatablePlayerMenu(Player player, String name) {
    this(player, name, 3);
  }
  
  public UpdatablePlayerMenu(Player player, String name, int rows) {
    super(name, rows);
    this.player = player;
  }
  
  public void open() {
    player.openInventory(getInventory());
  }
  
  public void register(KPlugin plugin, long updateEveryTicks) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
    this.task = new BukkitRunnable() {
      @Override
      public void run() {
        update();
      }
    }.runTaskTimer(plugin, 0, updateEveryTicks);
  }
  
  public void cancel() {
    this.task.cancel();
    this.task = null;
  }
  
  public abstract void update();
}
