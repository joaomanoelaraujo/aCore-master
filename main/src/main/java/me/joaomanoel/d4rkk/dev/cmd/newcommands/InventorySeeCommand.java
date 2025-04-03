package me.joaomanoel.d4rkk.dev.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.cmd.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InventorySeeCommand extends Commands {
   public InventorySeeCommand() {
      super("invsee", "inventorysee");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cOnly players can use this command.");
      } else {
         Player player = (Player)sender;
         if (!player.hasPermission("aCore.cmd.invsee")) {
            player.sendMessage("§cYou do not have permission to use this command.");
         } else if (args.length == 0) {
            player.sendMessage("§cUsage: /invsee [player]");
         } else {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
               player.sendMessage("§cPlayer not found.");
            } else {
               player.openInventory(target.getInventory());
            }
         }
      }
   }
}
