package me.joaomanoel.d4rkk.dev.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.cmd.Commands;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand extends Commands {
   public TeleportCommand() {
      super("tp", "teleport");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cOnly players can use this command.");
      } else {
         Player player = (Player)sender;
         if (!player.hasPermission("aCore.cmd.teleport")) {
            player.sendMessage("§cYou do not have permission to use this command.");
         } else if (args.length == 0) {
            player.sendMessage("§cUsage: /tp <player> [player] or /tp [player] <x> <y> <z>");
         } else {
            Player target;
            if (args.length == 1) {
               target = Bukkit.getPlayer(args[0]);
               if (target == null) {
                  player.sendMessage("§cPlayer not found.");
                  return;
               }

               player.teleport(target);
               player.sendMessage("§aYou have been teleported to the location of player " + Role.getColored(target.getName()) + "§a.");
            } else if (args.length == 2) {
               target = Bukkit.getPlayer(args[0]);
               Player targetTo = Bukkit.getPlayer(args[1]);
               if (targetTo == null || target == null) {
                  player.sendMessage("§cPlayer not found.");
                  return;
               }

               target.teleport(targetTo);
               target.sendMessage("§aYou have been teleported to the location of player " + Role.getColored(targetTo.getName()) + "§a.");
            } else {
               double x;
               double y;
               double z;
               if (args.length > 3) {
                  try {
                     if (args[1].equalsIgnoreCase("~")) {
                        args[1] = String.valueOf(player.getLocation().getX());
                     }

                     if (args[2].equalsIgnoreCase("~")) {
                        args[2] = String.valueOf(player.getLocation().getY());
                     }

                     if (args[3].equalsIgnoreCase("~")) {
                        args[3] = String.valueOf(player.getLocation().getZ());
                     }

                     x = Double.parseDouble(args[1]);
                     y = Double.parseDouble(args[2]);
                     z = Double.parseDouble(args[3]);
                     target = Bukkit.getPlayer(args[0]);
                     if (target == null) {
                        player.sendMessage("§cPlayer not found.");
                        return;
                     }

                     target.teleport(new Location(player.getWorld(), x, y, z));
                     target.sendMessage("§aYou have been teleported to: §6x: " + x + " y: " + y + " z: " + z + "§a.");
                  } catch (NumberFormatException var13) {
                     player.sendMessage("§cPlease use only numbers.");
                  }
               } else {
                  try {
                     if (args[0].equalsIgnoreCase("~")) {
                        args[0] = String.valueOf(player.getLocation().getX());
                     }

                     if (args[1].equalsIgnoreCase("~")) {
                        args[1] = String.valueOf(player.getLocation().getY());
                     }

                     if (args[2].equalsIgnoreCase("~")) {
                        args[2] = String.valueOf(player.getLocation().getZ());
                     }

                     x = Double.parseDouble(args[0]);
                     y = Double.parseDouble(args[1]);
                     z = Double.parseDouble(args[2]);
                     player.teleport(new Location(player.getWorld(), x, y, z));
                     player.sendMessage("§aYou have been teleported to: §6x: " + x + " y: " + y + " z: " + z + "§a.");
                  } catch (NumberFormatException var12) {
                     player.sendMessage("§cPlease use only numbers.");
                  }
               }
            }

         }
      }
   }
}
