package me.joaomanoel.d4rkk.dev.player.vanish;


import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.nms.NMSManager;
import me.joaomanoel.d4rkk.dev.utils.VanishAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Vanish {

   public static List<String> isVanish = new ArrayList<>();

   public static void hide(Player player) {
      Bukkit.getOnlinePlayers().forEach(p -> {
         if (!p.hasPermission("aCore.cmd.vanish")) {
            VanishAPI.hide(p, player);
         }
      });
   }

   public static void show(Player player) {
      Bukkit.getOnlinePlayers().forEach(p -> {
         VanishAPI.show(p, player);
      });
   }
}
