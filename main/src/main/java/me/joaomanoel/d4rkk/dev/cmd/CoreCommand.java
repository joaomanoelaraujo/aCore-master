package me.joaomanoel.d4rkk.dev.cmd;

import com.sun.management.OperatingSystemMXBean;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.booster.Booster;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.PluginDependencyChecker;
import me.joaomanoel.d4rkk.dev.utils.TPSUtil;
import me.joaomanoel.d4rkk.dev.utils.aUpdater;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
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
                " \n{s} §8▪ §a/ac gb <player> <private/network> <multiplier> <hours> §f- §7Give booster.".replace("{s}", " ") +
                " \n{s} §8▪ §a/ac give <player> <experience/coins> <table> <amount> §f- §7Give XP or coins.\n {s}".replace("{s}", " "));
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
      } else if (action.equalsIgnoreCase("give")) {
        if (args.length < 5) {
          player.sendMessage("§cUsage: /ac give <player> <experience/coins> <table> <amount>");
          player.sendMessage("§cTables: aCoreBedWars, aCoreDuels, aCoreSkyWars");
          return;
        }

        Profile target = Profile.getProfile(args[1]);
        if (target == null) {
          player.sendMessage("§cUser not found.");
          return;
        }

        String type = args[2].toLowerCase();
        String table = args[3];

        // Valida a tabela
        if (!table.equalsIgnoreCase("aCoreBedWars") &&
                !table.equalsIgnoreCase("aCoreDuels") &&
                !table.equalsIgnoreCase("aCoreSkyWars")) {
          player.sendMessage("§cInvalid table! Use: aCoreBedWars, aCoreDuels or aCoreSkyWars");
          return;
        }

        try {
          long amount = Long.parseLong(args[4]);

          if (amount <= 0) {
            player.sendMessage("§cThe amount must be greater than 0!");
            return;
          }

          if (type.equals("experience") || type.equals("xp") || type.equals("exp")) {
            // Adiciona experiência usando addStats
            target.addStats(table, amount, "experience");
            player.sendMessage("§aSuccessfully gave §f" + amount + " XP §ato " + args[1] + " §ain table §f" + table + "§a!");

            // Se o jogador estiver online, notifica
            Player targetPlayer = target.getPlayer();
            if (targetPlayer != null && targetPlayer.isOnline()) {
              targetPlayer.sendMessage("§aYou received §f" + amount + " XP §ain §f" + table.replace("aCore", "") + "§a!");
            }

          } else if (type.equals("coins") || type.equals("coin")) {
            // Adiciona coins usando addCoins
            target.addCoins(table, amount);
            player.sendMessage("§aSuccessfully gave §f" + amount + " coins §ato " + args[1] + " §ain table §f" + table + "§a!");

            // Se o jogador estiver online, notifica
            Player targetPlayer = target.getPlayer();
            if (targetPlayer != null && targetPlayer.isOnline()) {
              targetPlayer.sendMessage("§aYou received §f" + amount + " coins §ain §f" + table.replace("aCore", "") + "§a!");
            }

          } else {
            player.sendMessage("§cInvalid type! Use: experience or coins");
            return;
          }

        } catch (NumberFormatException ex) {
          player.sendMessage("§cPlease use a valid number for the amount!");
        } catch (Exception ex) {
          player.sendMessage("§cAn error occurred while processing the command!");
          ex.printStackTrace();
        }
      } else {
        player.sendMessage(" \n§6/ac update §f- §7Update aCore.\n§6/ac convert §f- §7Convert your Database.\n§6/ac gb <player> <private/network> <multiplier> <hours> §f- §7Give a booster.\n§6/ac give <player> <experience/coins> <table> <amount> §f- §7Give XP or coins.\n ");
      }
    }
  }

  public double getCpuLoad() {
    try {
      double cpuLoad = osBean.getProcessCpuLoad() * 100;
      return Math.round(cpuLoad * 10.0) / 10.0;
    } catch (Exception e) {
      e.printStackTrace();
      return 0.0;
    }
  }
}