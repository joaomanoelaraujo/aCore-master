package me.joaomanoel.d4rkk.dev.cmd;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.game.FakeGame;
import me.joaomanoel.d4rkk.dev.game.Game;
import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand extends Commands {
   public LobbyCommand() {
      super("lobby", "l");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      } else {
         Player player = (Player)sender;
         Profile profile = Profile.getProfile(player.getName());
         Game<?> game = profile.getGame();
         if (game != null && !(game instanceof FakeGame)) {
               if (profile.playingGame()){
                  game.leave(profile, null);
               }
         } else {
            Core.sendServer(profile, "lobby");
         }
      }
   }
}
