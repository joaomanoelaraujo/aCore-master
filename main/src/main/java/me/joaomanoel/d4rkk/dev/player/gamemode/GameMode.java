package me.joaomanoel.d4rkk.dev.player.gamemode;

import me.joaomanoel.d4rkk.dev.languages.translates.EN_US;
import org.bukkit.entity.Player;

public class GameMode {
   public static void changeMode(Player player, Player target, String mode) {
      GameModeType type = GameModeType.fromName(mode);
      if (type == null) {
         player.sendMessage(EN_US.gamemode$mode);
      } else if (player != target) {
         player.sendMessage(EN_US.gamemode$changed.replace("{to}", type.name().toUpperCase()).replace("{mode}", target.getName()));
      } else {
         target.setGameMode(org.bukkit.GameMode.valueOf(type.name()));
         target.sendMessage(EN_US.gamemode$changer.replace("{type}", type.name().toUpperCase()));
      }
   }
}
