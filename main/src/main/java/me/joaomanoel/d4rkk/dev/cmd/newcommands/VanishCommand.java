package me.joaomanoel.d4rkk.dev.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.cmd.Commands;
import me.joaomanoel.d4rkk.dev.game.FakeGame;
import me.joaomanoel.d4rkk.dev.game.Game;
import me.joaomanoel.d4rkk.dev.game.GameTeam;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.vanish.Vanish;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand extends Commands {
   public VanishCommand() {
      super("vanish", "v");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cOnly players can use this command.");
      } else {
         Player player = (Player)sender;
         Profile profile = Profile.getProfile(player.getName());
         if (!player.hasPermission("aCore.cmd.vanish")) {
            player.sendMessage("§cYou do not have permission to use this command.");
         } else {
            boolean contains = Vanish.isVanish.contains(player.getName());
            Game<? extends GameTeam> game = profile.getGame();
            if (contains) {
               Bukkit.getOnlinePlayers().forEach((p) -> {
                  Profile profilep = Profile.getProfile(p.getName());
                  if (!(game instanceof FakeGame)) {
                     if (game == null && !profilep.playingGame() && p.getWorld() == player.getWorld()) {
                        p.showPlayer(player);
                     }

                     if (game != null && profilep.getGame() != null && profile.getGame() == game) {
                        if (game.isSpectator(player) && game.isSpectator(p)) {
                           p.showPlayer(player);
                        }

                        if (!game.isSpectator(player) && !game.isSpectator(p)) {
                           p.showPlayer(player);
                        }
                     }

                  }
               });
               Vanish.isVanish.remove(player.getName());
               Vanish.show(player);
               player.sendMessage("§aVanish mode deactivated.");
            } else {
               Vanish.isVanish.add(player.getName());
               Vanish.hide(player);
               player.sendMessage("§aVanish mode activated.");
            }
         }
      }
   }
}
