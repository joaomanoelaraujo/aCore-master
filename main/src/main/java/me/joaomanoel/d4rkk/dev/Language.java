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
public class Language {
  
  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger())
      .getModule("LANGUAGE");
  private static final KConfig CONFIG = Core.getInstance().getConfig("language");
  
  public static String clickhere$click = "\n §eClick ";
  public static String clickhere$toopen = " §eto open the server shop.\n ";
  public static String clickhere$open = "§7Click here to open the shop.";
  public static String clickhere$here = "HERE";

  public static String discord$click = "\n §eClick ";
  public static String discord$toopen = " §eto open the server's discord.\n ";
  public static String discord$open = "§7Click here to open the server's discord.";
  public static String discord$here = "HERO";
  public static String party$invite = "§a%leader% §7convidou você para a Party dele!\n§rVocê pode ";
  public static String party$accept = "§aClique para aceitar o convite de Party de %leader%§r";
  public static String party$reject = "§cClique para negar o convite de Party de %leader%§r";
  public static String party$join = "§a%member% entrou na Party!§r";
  public static String party$leave = "§c%member% saiu da Party!§r";
  public static String party$transfer = "§a%member% se tornou o novo Líder da Party!§r";
  public static String party$kick = "§c%leader% te expulsou da Party!§r";
  public static String party$invite_buttons$accept = "§a§lACEITAR§7";
  public static String party$invite_buttons$reject = "§c§lNEGAR§7";
  public static String party$hover_accept = "§7Clique para aceitar o convite de Party de %leader%§r";
  public static String party$hover_reject = "§7Clique para negar o convite de Party de %leader%§r";
  public static String party$or = " §7ou§r ";
  public static void setupLanguage() {
    boolean save = false;
    KWriter writer = Core.getInstance().getWriter(CONFIG.getFile(),
        "aCore - Created by D4RKK\nConfiguration version: " + Core.getInstance()
            .getDescription().getVersion());
    for (Field field : Language.class.getDeclaredFields()) {
      if (field.getName().contains("$") && !Modifier.isFinal(field.getModifiers())) {
        String nativeName = field.getName().replace("$", ".").replace("_", "-");
        
        try {
          Object value;
          KWriter.YamlEntryInfo entryInfo = field.getAnnotation(KWriter.YamlEntryInfo.class);
          
          if (CONFIG.contains(nativeName)) {
            value = CONFIG.get(nativeName);
            if (value instanceof String) {
              value = StringUtils.formatColors((String) value).replace("\\n", "\n");
            } else if (value instanceof List) {
              List l = (List) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.formatColors((String) v).replace("\\n", "\n"));
                } else {
                  list.add(v);
                }
              }
              
              value = list;
            }
            
            field.set(null, value);
            writer.set(nativeName, new KWriter.YamlEntry(
                new Object[]{entryInfo == null ? "" : entryInfo.annotation(),
                    CONFIG.get(nativeName)}));
          } else {
            value = field.get(null);
            if (value instanceof String) {
              value = StringUtils.deformatColors((String) value).replace("\n", "\\n");
            } else if (value instanceof List) {
              List l = (List) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.deformatColors((String) v).replace("\n", "\\n"));
                } else {
                  list.add(v);
                }
              }
              
              value = list;
            }
            
            save = true;
            writer.set(nativeName, new KWriter.YamlEntry(new Object[]{entryInfo == null ? "" : entryInfo.annotation(), value}));
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
          () -> LOGGER.info("A config §6language.yml §afoi modificada ou criada."));
    }
  }
}
