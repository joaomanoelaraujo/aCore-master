package me.joaomanoel.d4rkk.dev.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.cmd.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PingCommand extends Commands {
   public PingCommand() {
      super("ping");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cOnly players can use this command.");
      } else {
         Player player;
         if (args.length == 0) {
            player = (Player)sender;
            player.sendMessage("§aYour ping is " + ((CraftPlayer)player).getHandle().ping + "ms.");
         } else {
            player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
               sender.sendMessage("§cPlayer not found.");
            } else {
               sender.sendMessage("§aPing of " + player.getName() + " is " + ((CraftPlayer)player).getHandle().ping + "ms.");
            }
         }
      }
   }
}
