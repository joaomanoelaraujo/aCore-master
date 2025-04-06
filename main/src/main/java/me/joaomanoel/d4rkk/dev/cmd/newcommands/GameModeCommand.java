package me.joaomanoel.d4rkk.dev.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.cmd.Commands;
import me.joaomanoel.d4rkk.dev.player.gamemode.GameMode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand extends Commands {
   public GameModeCommand() {
      super("gamemode", "gm");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cOnly players can use this command.");
      } else {
         Player player = (Player)sender;
         if (!player.hasPermission("aCore.cmd.gamemode")) {
            player.sendMessage("§cYou do not have permission to use this command.");
         } else if (args.length == 0) {
            player.sendMessage("§cUsage: /gamemode [player] <mode>");
         } else if (args.length > 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
               player.sendMessage("§cPlayer not found.");
            } else {
               GameMode.changeMode(player, target, args[1]);
            }
         } else {
            GameMode.changeMode(player, player, args[0]);
         }
      }
   }
}
