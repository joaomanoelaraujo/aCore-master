package me.joaomanoel.d4rkk.dev.player.gamemode;

import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.entity.Player;

public class GameMode {
   public static void changeMode(Player player, Player target, String mode) {
      GameModeType type = GameModeType.fromName(mode);
      if (type == null) {
         player.sendMessage(LanguageAPI.getConfig(Profile.getProfile(player.getName())).getString("gamemode.mode"));
      } else if (player != target) {
         player.sendMessage(LanguageAPI.getConfig(Profile.getProfile(player.getName())).getString("gamemode.changed").replace("{to}", type.name().toUpperCase()).replace("{mode}", target.getName()));
      } else {
         target.setGameMode(org.bukkit.GameMode.valueOf(type.name()));
         target.sendMessage(LanguageAPI.getConfig(Profile.getProfile(player.getName())).getString("gamemode$changer").replace("{type}", type.name().toUpperCase()));
      }
   }
}
