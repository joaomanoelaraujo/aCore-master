package me.joaomanoel.d4rkk.dev.cmd;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.game.FakeGame;
import me.joaomanoel.d4rkk.dev.game.Game;
import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand extends Commands {
   public LobbyCommand() {
      super("lobby");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      } else {
         Player player = (Player)sender;
         Profile profile = Profile.getProfile(player.getName());
         Game<?> game = profile.getGame();
         if (game != null && !(game instanceof FakeGame)) {
            player.sendMessage("§aConecting...");
            if (Core.minigame.equals("Sky Wars")) {
               Core.sendServer(profile, "skywars");
            }
            if (Core.minigame.equals("Bed Wars")) {
               Core.sendServer(profile, "bedwars");
            }
            if (Core.minigame.equals("Duels")) {
               Core.sendServer(profile, "duels");
            }
            if (Core.minigame.equals("The Bridge")) {
               Core.sendServer(profile, "thebridge");
            }

         } else {
            Core.sendServer(profile, "lobby");
         }
      }
   }
}
