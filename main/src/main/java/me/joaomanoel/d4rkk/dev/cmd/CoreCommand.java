package me.joaomanoel.d4rkk.dev.cmd;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.booster.Booster;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.player.Profile;

import me.joaomanoel.d4rkk.dev.utils.PluginDependencyChecker;
import me.joaomanoel.d4rkk.dev.utils.TPSUtil;
import me.joaomanoel.d4rkk.dev.utils.aUpdater;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.List;

public class CoreCommand extends Commands {

  private final OperatingSystemMXBean osBean;

  public CoreCommand() {
    super("aCore", "ac");
    this.osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;

      if (!player.hasPermission("aCore.admin")) {
        player.sendMessage("§6aCore §bv" + Core.getInstance().getDescription().getVersion() + " §7Created by §6d4rkk§7.");
        return;
      }

      if (args.length == 0) {
        List<String> dependents = PluginDependencyChecker.getPluginsUsingCore();
        String dependentsList = dependents.isEmpty() ? "None" : "  §8▪ §f" + String.join("\n  §8▪ §f", dependents);

        long maxMemory = Runtime.getRuntime().maxMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = maxMemory - freeMemory;
        long usedMemoryMB = usedMemory / (1024 * 1024);

        // Obtém os valores de TPS
        double[] tps = TPSUtil.getRecentTPS();
        String tpsFormatted = String.format("%s, %s, %s",
                TPSUtil.formatTPS(tps[0]),
                TPSUtil.formatTPS(tps[1]),
                TPSUtil.formatTPS(tps[2])
        );

        player.sendMessage("{s}\n  §a[AetherPlugins] Plugins active on server:\n".replace("{s}", " ") + dependentsList + "\n" +"\n" +
                " \n  §bServer:\n" +
                "  §8▪ §fTPS: " + tpsFormatted + " §7(1m, 5m, 15m)\n" +
                "  §8▪ §fProcessor: §b" + getCpuLoad() + "%\n" +
                "  §8▪ §fRAM memory: §b" + usedMemoryMB + "MB/" + (maxMemory / (1024 * 1024)) + "MB\n{s}".replace("{s}", " ") +
                " \n  §bAvailable sub-commands:" +
                " \n{s} §8▪ §a/ac update §f- §7Update aCore.".replace("{s}", " ") +
                " \n{s} §8▪ §a/ac convert §f- §7Convert MYSQL.".replace("{s}", " ") +
                " \n{s} §8▪ §a/ac gb <player> <private/network> <multiplier> <hours> §f- §7Give booster.\n {s}".replace("{s}", " "));
        return;
      }

      String action = args[0];
      if (action.equalsIgnoreCase("update")) {
        if (aUpdater.UPDATER != null) {
          if (!aUpdater.UPDATER.canDownload) {
            player.sendMessage(" \n§6§l[aCore]\n \n§aThe update is already downloaded, it will be applied on the next server restart. If you wish to apply it now, use the command /stop.\n ");
            return;
          }
          aUpdater.UPDATER.canDownload = false;
          aUpdater.UPDATER.downloadUpdate(player);
        } else {
          player.sendMessage("§aThe plugin is already up to date.");
        }
      } else if (action.equalsIgnoreCase("convert")) {
        player.sendMessage("§fDatabase: " + Database.getInstance().getClass().getSimpleName().replace("Database", ""));
        Database.getInstance().convertDatabase(player);
      } else if (action.equalsIgnoreCase("giveboost") || action.equalsIgnoreCase("gb")) {
        if (args.length < 5) {
          player.sendMessage("§cUsage: /ac gb <player> <private/network> <multiplier> <hours>");
          return;
        }

        Profile target = Profile.getProfile(args[1]);
        if (target == null) {
          player.sendMessage("§cUser not found.");
          return;
        }

        try {
          Booster.BoosterType type = Booster.BoosterType.valueOf(args[2].toUpperCase());
          double multiplier = Double.parseDouble(args[3]);
          long hours = Long.parseLong(args[4]);

          if (multiplier < 1.0D || hours < 1) {
            throw new Exception();
          }

          target.getBoostersContainer().addBooster(type, multiplier, hours);
          player.sendMessage("§aBooster added successfully to " + args[1] + "!");
        } catch (Exception ex) {
          player.sendMessage("§cPlease use valid values. Example: /ac gb [player] private 2.5 3");
        }
      } else {
        player.sendMessage(" \n§6/ac update §f- §7Update aCore.\n§6/ac convert §f- §7Convert your Database.\n§6/ac gb <player> <private/network> <multiplier> <hours> §f- §7Give a booster.\n ");
      }
    }
  }

  public double getCpuLoad() {
    try {
      double cpuLoad = osBean.getProcessCpuLoad() * 100;
      return Math.round(cpuLoad * 10.0) / 10.0; // Arredonda para uma casa decimal
    } catch (Exception e) {
      e.printStackTrace();
      return 0.0;
    }
  }
}