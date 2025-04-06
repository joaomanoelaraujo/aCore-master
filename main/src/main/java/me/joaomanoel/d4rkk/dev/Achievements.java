package me.joaomanoel.d4rkk.dev;


import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.plugin.config.KWriter;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@SuppressWarnings("rawtypes")
public class Achievements {
  
  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger())
      .getModule("ACHIEVEMENTS");
  private static final KConfig CONFIG = Core.getInstance().getConfig("achievements/achievements");


  public static String bedwars$kill$name = "Assassin (Doubles)";
  public static String bedwars$kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins";

  public static String bedwars$master_kill$name = "Master Assassin (Doubles)";
  public static String bedwars$master_kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6500 Coins";

  public static String bedwars$victorious$name = "Victorious (Doubles)";
  public static String bedwars$victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String bedwars$master_victorious$name = "Master Victorious (Doubles)";
  public static String bedwars$master_victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61,000 Coins";

  public static String bedwars$destroyer$name = "Destroyer (Doubles)";
  public static String bedwars$destroyer$desc = "&7Destroy %reach%\n&7beds to receive:\n \n &8• &6250 Coins";

  public static String bedwars$master_destroyer$name = "Master Destroyer (Doubles)";
  public static String bedwars$master_destroyer$desc = "&7Destroy %reach%\n&7beds to receive:\n \n &8• &61,000 Coins";

  public static String bedwars$persistent$name = "Persistent (Doubles)";
  public static String bedwars$persistent$desc = "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String bedwars$quartet_kill$name = "Assassin (Quartets)";
  public static String bedwars$quartet_kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins";

  public static String bedwars$quartet_master_kill$name = "Master Assassin (Quartets)";
  public static String bedwars$quartet_master_kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6500 Coins";

  public static String bedwars$quartet_victorious$name = "Victorious (Quartets)";
  public static String bedwars$quartet_victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String bedwars$quartet_master_victorious$name = "Master Victorious (Quartets)";
  public static String bedwars$quartet_master_victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61,000 Coins";

  public static String bedwars$quartet_destroyer$name = "Destroyer (Quartets)";
  public static String bedwars$quartet_destroyer$desc = "&7Destroy %reach%\n&7beds to receive:\n \n &8• &6250 Coins";

  public static String bedwars$quartet_master_destroyer$name = "Master Destroyer (Quartets)";
  public static String bedwars$quartet_master_destroyer$desc = "&7Destroy %reach%\n&7beds to receive:\n \n &8• &61,000 Coins";

  public static String bedwars$quartet_persistent$name = "Persistent (Quartets)";
  public static String bedwars$quartet_persistent$desc = "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String bedwars$title$kill$name = "Lurking Assassin";
  public static String bedwars$title$kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &fTitle: &cDream Destroyer";

  public static String bedwars$title$win$name = "Protector of Beds";
  public static String bedwars$title$win$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &fTitle: &6Sleepy Angel";

  public static String bedwars$title$destroy$name = "Freddy Krueger";
  public static String bedwars$title$destroy$desc = "&7Destroy a total of %reach%\n&7beds to receive:\n \n &8• &fTitle: &4Nightmare";

  // SkyWars Achievements
  public static String skywars$solo_kill$name = "Assassin (Solo)";
  public static String skywars$solo_kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins";

  public static String skywars$solo_master_kill$name = "Master Assassin (Solo)";
  public static String skywars$solo_master_kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6500 Coins";

  public static String skywars$solo_victorious$name = "Victorious (Solo)";
  public static String skywars$solo_victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String skywars$solo_master_victorious$name = "Master Victorious (Solo)";
  public static String skywars$solo_master_victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61,000 Coins";

  public static String skywars$solo_assist$name = "Assistant (Solo)";
  public static String skywars$solo_assist$desc = "&7Get a total of %reach%\n&7assists to receive:\n \n &8• &6100 Coins";

  public static String skywars$solo_master_assist$name = "Master Assistant (Solo)";
  public static String skywars$solo_master_assist$desc = "&7Get a total of %reach%\n&7assists to receive:\n \n &8• &6500 Coins";

  public static String skywars$solo_persistent$name = "Persistent (Solo)";
  public static String skywars$solo_persistent$desc = "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String skywars$duo_kill$name = "Assassin (Duos)";
  public static String skywars$duo_kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins";

  public static String skywars$duo_master_kill$name = "Master Assassin (Duos)";
  public static String skywars$duo_master_kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6500 Coins";

  public static String skywars$duo_victorious$name = "Victorious (Duos)";
  public static String skywars$duo_victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String skywars$duo_master_victorious$name = "Master Victorious (Duos)";
  public static String skywars$duo_master_victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61,000 Coins";

  public static String skywars$duo_assist$name = "Assistant (Duos)";
  public static String skywars$duo_assist$desc = "&7Get a total of %reach%\n&7assists to receive:\n \n &8• &6100 Coins";

  public static String skywars$duo_master_assist$name = "Master Assistant (Duos)";
  public static String skywars$duo_master_assist$desc = "&7Get a total of %reach%\n&7assists to receive:\n \n &8• &6500 Coins";

  public static String skywars$duo_persistent$name = "Persistent (Duos)";
  public static String skywars$duo_persistent$desc = "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String skywars$title$kill$name = "Heavenly Traitor";
  public static String skywars$title$kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &fTitle: &cAngel of Death";

  public static String skywars$title$win$name = "Heavenly Dethroner";
  public static String skywars$title$win$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &fTitle: &bHeavenly King";

  public static String skywars$title$assist$name = "Guardian Angel";
  public static String skywars$title$assist$desc = "&7Get a total of %reach%\n&7assists to receive:\n \n &8• &fTitle: &6Winged Companion";

  // The Bridge Achievements
  public static String thebridge$solo_kill$name = "Assassin (Solo)";
  public static String thebridge$solo_kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins";

  public static String thebridge$solo_master_kill$name = "Master Assassin (Solo)";
  public static String thebridge$solo_master_kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6500 Coins";

  public static String thebridge$solo_victorious$name = "Victorious (Solo)";
  public static String thebridge$solo_victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String thebridge$solo_master_victorious$name = "Master Victorious (Solo)";
  public static String thebridge$solo_master_victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61,000 Coins";

  public static String thebridge$solo_pontuador$name = "Scorer (Solo)";
  public static String thebridge$solo_pontuador$desc = "&7Score a total of %reach%\n&7points to receive:\n \n &8• &6250 Coins";

  public static String thebridge$solo_master_pontuador$name = "Master Scorer (Solo)";
  public static String thebridge$solo_master_pontuador$desc = "&7Score a total of %reach%\n&7points to receive:\n \n &8• &61,000 Coins";

  public static String thebridge$solo_persistent$name = "Persistent (Solo)";
  public static String thebridge$solo_persistent$desc = "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String thebridge$duo_kill$name = "Assassin (Duos)";
  public static String thebridge$duo_kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins";

  public static String thebridge$duo_master_kill$name = "Master Assassin (Duos)";
  public static String thebridge$duo_master_kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6500 Coins";

  public static String thebridge$duo_victorious$name = "Victorious (Duos)";
  public static String thebridge$duo_victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String thebridge$duo_master_victorious$name = "Master Victorious (Duos)";
  public static String thebridge$duo_master_victorious$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61,000 Coins";

  public static String thebridge$duo_pontuador$name = "Scorer (Duos)";
  public static String thebridge$duo_pontuador$desc = "&7Score a total of %reach%\n&7points to receive:\n \n &8• &6250 Coins";

  public static String thebridge$duo_master_pontuador$name = "Master Scorer (Duos)";
  public static String thebridge$duo_master_pontuador$desc = "&7Score a total of %reach%\n&7points to receive:\n \n &8• &61,000 Coins";

  public static String thebridge$duo_persistent$name = "Persistent (Duos)";
  public static String thebridge$duo_persistent$desc = "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins";

  public static String thebridge$title$kill$name = "Bridge Assassin";
  public static String thebridge$title$kill$desc = "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &fTitle: &cBridge Sentinel";

  public static String thebridge$title$win$name = "Glorious Over Bridges";
  public static String thebridge$title$win$desc = "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &fTitle: &6Bridge Leader";

  public static String thebridge$title$pontuador$name = "Mastery in Scoring";
  public static String thebridge$title$pontuador$desc = "&7Score a total of %reach%\n&7points to receive:\n \n &8• &fTitle: &eMaster Scorer";


  public static void setupAchaviments() {
    boolean save = false;
    KWriter writer = Core.getInstance().getWriter(CONFIG.getFile(),
            "aCore - Created by D4RKK\nVersion on configuration: " + Core.getInstance()
                    .getDescription().getVersion());
    for (Field field : Achievements.class.getDeclaredFields()) {
      if (field.getName().contains("$") && !Modifier.isFinal(field.getModifiers())) {
        String nativeName = field.getName().replace("$", ".").replace("_", "-");

        try {
          Object value;

          if (CONFIG.contains(nativeName)) {
            value = CONFIG.get(nativeName);
            if (value instanceof String) {
              value = StringUtils.formatColors((String) value).replace("\\n", "\n");
            } else if (value instanceof List) {
              List<?> l = (List<?>) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.formatColors((String) v).replace("\\n", "\n"));
                } else {
                  list.add(v);
                }
              }

              l = null;
              value = list;
            }

            field.set(null, value);
            writer.set(nativeName, new KWriter.YamlEntry(new Object[]{"", CONFIG.get(nativeName)}));
          } else {
            value = field.get(null);
            if (value instanceof String) {
              value = StringUtils.deformatColors((String) value).replace("\n", "\\n");
            } else if (value instanceof List) {
              List<?> l = (List<?>) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.deformatColors((String) v).replace("\n", "\\n"));
                } else {
                  list.add(v);
                }
              }

              l = null;
              value = list;
            }

            save = true;
            writer.set(nativeName, new KWriter.YamlEntry(new Object[]{"", value}));
          }
        } catch (ReflectiveOperationException e) {
          LOGGER.log(Level.WARNING, "Unexpected error on settings file: ", e);
        }
      }
    }

    if (save) {
      writer.write();
      CONFIG.reload();
      Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(),
              () -> LOGGER.info("A config §6achaviments.yml §ahas created or modified."));
    }
  }
}