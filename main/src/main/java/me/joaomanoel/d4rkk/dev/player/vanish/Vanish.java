package me.joaomanoel.d4rkk.dev.player.vanish;


import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.languages.translates.EN_US;
import me.joaomanoel.d4rkk.dev.nms.NMS;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Vanish {
   public static List<String> isVanish = new ArrayList<>();

   public static void setupVanish() {
      Bukkit.getScheduler().runTaskTimer(Core.getInstance(), () -> Bukkit.getOnlinePlayers().stream().filter((player) -> isVanish.contains(player.getName())).forEach((player) -> {
         NMS.sendActionBar(player, EN_US.vanish$mode);
         Bukkit.getOnlinePlayers().stream().filter((p) -> !p.hasPermission("aCore.cmd.vanish")).forEach((p) -> p.hidePlayer(player));
      }), 1L, 1L);
   }
}
