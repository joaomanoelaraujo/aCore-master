package me.joaomanoel.d4rkk.dev.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.cmd.Commands;
import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand extends Commands {
   public FlyCommand() {
      super("fly", "fly");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cOnly players can use this command.");
      } else {
         Player player = (Player)sender;
         Profile profile = Profile.getProfile(player.getName());
         if (!player.hasPermission("aCore.fly")) {
            player.sendMessage("§cYou do not have permission to use this command.");
         } else if (profile.playingGame()) {
            player.sendMessage("§cYou cannot change fly mode while in-game.");
         } else {
            player.setAllowFlight(!player.getAllowFlight());
            player.sendMessage("§aFly mode " + (player.getAllowFlight() ? "enabled." : "disabled."));
         }
      }
   }
}
