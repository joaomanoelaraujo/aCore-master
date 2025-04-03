package me.joaomanoel.d4rkk.dev.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.cmd.Commands;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportHereCommand extends Commands {
   public TeleportHereCommand() {
      super("tphere", "teleporthere");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cOnly players can use this command.");
      } else {
         Player player = (Player)sender;
         if (!player.hasPermission("aCore.cmd.teleport")) {
            player.sendMessage("§cYou do not have permission to use this command.");
         } else if (args.length == 0) {
            player.sendMessage("§cUsage: /tphere [player]");
         } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
               player.sendMessage("§cPlayer not found.");
            } else {
               target.teleport(player);
               target.sendMessage("§aYou have been teleported to player " + Role.getColored(player.getName()) + "§a.");
            }
         }
      }
   }
}
