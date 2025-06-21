package me.joaomanoel.d4rkk.dev.hook;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.enums.PlayerVisibility;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.servers.ServerItem;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;

@SuppressWarnings("all")
public class aCoreExpansion extends PlaceholderExpansion {
  
  private static final SimpleDateFormat MURDER_FORMAT = new SimpleDateFormat("mm:ss");
  
  @Override
  public boolean canRegister() {
    return true;
  }

  @Override
  public boolean persist() {
    return true;
  }

  @Override
  public String getAuthor() {
    return "d4rkk";
  }
  
  @Override
  public String getIdentifier() {
    return "aCore";
  }
  
  @Override
  public String getVersion() {
    return Core.getInstance().getDescription().getVersion();
  }
  
  @Override
  public String onPlaceholderRequest(Player player, String params) {
    Profile profile = null;
    if (player == null || (profile = Profile.getProfile(player.getName())) == null) {
      return "";
    }
    
    if (params.startsWith("online")) {
      if (params.contains("online_")) {
        String server = params.replace("online_", "");
        ServerItem si = ServerItem.getServerItem(server);
        if (si != null) {
          return StringUtils.formatNumber(si.getBalancer().getTotalNumber());
        }
        
        return "entry invalida";
      }
      
      long online = 0;
      for (ServerItem si : ServerItem.listServers()) {
        online += si.getBalancer().getTotalNumber();
      }
      return StringUtils.formatNumber(online);
    } else if (params.equals("role")) {
      return Role.getPlayerRole(player).getName();
    } else if (params.equals("cash")) {
      return StringUtils.formatNumber(CashManager.getCash(player));
    } else if (params.equals("status_jogadores")) {
      return profile.getPreferencesContainer().getPlayerVisibility().getName();
    } else if (params.equals("status_jogadores_nome")) {
      if (profile.getPreferencesContainer().getPlayerVisibility() == PlayerVisibility.TODOS) {
        return "§aON";
      }
      
      return "§cOFF";
    } else if (params.equals("status_jogadores_inksack")) {
      return profile.getPreferencesContainer().getPlayerVisibility().getInkSack();
    } else if (params.startsWith("SkyWars_")) {
      String table = "aCoreSkyWars";
      String value = params.replace("SkyWars_", "");
      if (value.equals("kills") || value.equals("deaths") || value.equals("assists") || value.equals("games") || value.equals("wins")) {
        return StringUtils.formatNumber(profile.getStats(table, "insane" + value, "insane2v2" + value, "normal" + value, "normal2v2" + value, "ranked" + value));
      } else if (value.equals("insanekills") || value.equals("insanedeaths") || value.equals("insaneassists") || value.equals("insanegames") || value.equals("insanewins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("insane2v2kills") || value.equals("insane2v2deaths") || value.equals("insane2v2assists") || value.equals("insane2v2games") || value.equals("insane2v2wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("normal2v2kills") || value.equals("normal2v2deaths") || value.equals("normal2v2assists") || value.equals("normal2v2games") || value.equals("normal2v2wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("normalkills") || value.equals("normaldeaths") || value.equals("normalassists") || value.equals("normalgames") || value.equals("normalwins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("rankedkills") || value.equals("rankeddeaths") || value.equals("rankedassists") || value.equals("rankedgames") || value.equals("rankedwins") || value.equals("rankedpoints")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("coins")) {
        return StringUtils.formatNumber(profile.getCoins(table));
      } else if (value.equals("limitedcoins")) {
        return StringUtils.formatNumber(profile.getLimitedCoins(table));
      }
    } else if (params.startsWith("BlockSumo_")) {
      String table = "aCoreBlockSumo";
      String value = params.replace("BlockSumo_", "");
      if (value.equals("kills") || value.equals("deaths") || value.equals("assists") || value.equals("games") || value.equals("wins")) {
        return StringUtils.formatNumber(profile.getStats(table, "s" + value, "normal" + value));
      } else if (value.equals("skills") || value.equals("sdeaths") || value.equals("sassists") || value.equals("sgames") || value.equals("swins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("normalkills") || value.equals("normaldeaths") || value.equals("normalassists") || value.equals("normalgames") || value.equals("normalwins") || value.equals("normalpoints")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("coins")) {
        return StringUtils.formatNumber(profile.getCoins(table));
      }

    } else if (params.startsWith("ThePit_")) {
      String table = "aCoreThePit";
      String value = params.replace("ThePit_", "");
      if (value.equals("kills") || value.equals("deaths") || value.equals("assists")) {
        return StringUtils.formatNumber(profile.getStats(table, value, value));
      } else if (value.equals("coins")) {
        return StringUtils.formatNumber(profile.getCoins(table));
      }
    } else if (params.startsWith("TheBridge_")) {
      String table = "aCoreTheBridge";
      String value = params.replace("TheBridge_", "");
      if (value.equals("kills") || value.equals("deaths") || value.equals("games") || value.equals("points") || value.equals("wins")) {
        return StringUtils.formatNumber(profile.getStats(table, "1v1" + value, "2v2" + value));
      } else if (value.equals("1v1kills") || value.equals("1v1deaths") || value.equals("1v1games") || value.equals("1v1points") || value.equals("1v1wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("2v2kills") || value.equals("2v2deaths") || value.equals("2v2games") || value.equals("2v2points") || value.equals("2v2wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("winstreak")) {
        return StringUtils.formatNumber(profile.getDailyStats(table, "laststreak", value));
      } else if (value.equals("coins")) {
        return StringUtils.formatNumber(profile.getCoins(table));
      }
    } else if (params.toLowerCase().startsWith("duels_")) {
      String table = "aCoreDuels";
      String value = params.replace("Duels_", "");
      if (value.equals("kills") || value.equals("deaths") || value.equals("games") || value.equals("wins")) {
        return StringUtils.formatNumber(profile.getStats(table, "uhc" + value, "bow" + value, "classic" + value, "op" + value, "sumo" + value, "bedfight" + value));
      } else if (value.equals("uhckills") || value.equals("uhcdeaths") || value.equals("uhcgames") || value.equals("uhcwins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("bowkills") || value.equals("bowdeaths") || value.equals("bowgames") || value.equals("bowwins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("sumokills") || value.equals("sumodeaths") || value.equals("sumogames") || value.equals("sumowins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("bedfightkills") || value.equals("bedfightdeaths") || value.equals("bedfightgames") || value.equals("bedfightwins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("classickills") || value.equals("classicdeaths") || value.equals("classicgames") || value.equals("classicwins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("opkills") || value.equals("opdeaths") || value.equals("opgames") || value.equals("opwins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("killstreak")) {
        return StringUtils.formatNumber(profile.getDailyStats(table, "laststreak", value));
      } else if (value.equals("bestkillstreak")) {
        return StringUtils.formatNumber(profile.getStats("aCoreDuels", "bestkillstreak"));
      } else if (value.equals("coins")) {
        return StringUtils.formatNumber(profile.getCoins(table));
      }
    } else if (params.startsWith("BatteryDash_")) {
      String table = "aCoreBatteryDash";
      String value = params.replace("BatteryDash_", "");
      if (value.equals("kills") || value.equals("deaths") || value.equals("games") || value.equals("points") || value.equals("wins")) {
        return StringUtils.formatNumber(profile.getStats(table, "1v1" + value, "4v4" + value));
      } else if (value.equals("1v1kills") || value.equals("1v1deaths") || value.equals("1v1games") || value.equals("1v1wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("4v4kills") || value.equals("4v4deaths") || value.equals("4v4games") || value.equals("4v4wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("winstreak")) {
        return StringUtils.formatNumber(profile.getDailyStats(table, "laststreak", value));
      } else if (value.equals("coins")) {
        return StringUtils.formatNumber(profile.getCoins(table));
      }
    } else if (params.startsWith("BedWars_")) {
      String table = "aCoreBedWars";
      String value = params.replace("BedWars_", "");
      if (value.equals("kills") || value.equals("deaths") || value.equals("bedslosteds") || value.equals("finalkills") || value.equals("finaldeaths") || value.equals("bedsdestroyeds") || value.equals("games") || value.equals("wins")) {
        return StringUtils.formatNumber(profile.getStats(table, "1v1" + value, "4v4" + value, "2v2" + value, "3v3" + value, "x1" + value, "x2" + value));
      } else if (value.equals("2v2kills") || value.equals("2v2deaths") || value.equals("2v2") || value.equals("2v2games") || value.equals("2v2finalkills") || value.equals("2v2finaldeaths") || value.equals("2v2bedsdestroyeds") || value.equals("2v2bedslosteds") || value.equals("2v2wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("1v1kills") || value.equals("1v1deaths") || value.equals("1v1") || value.equals("1v1games") || value.equals("1v1finalkills") || value.equals("1v1finaldeaths") || value.equals("1v1bedsdestroyeds") || value.equals("1v1bedslosteds") || value.equals("1v1wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("3v3kills") || value.equals("3v3deaths") || value.equals("3v3") || value.equals("3v3games") || value.equals("3v3finalkills") || value.equals("3v3finaldeaths") || value.equals("3v3bedsdestroyeds") || value.equals("3v3bedslosteds") || value.equals("3v3wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("x1kills") || value.equals("x1deaths") || value.equals("x1") || value.equals("x1games") || value.equals("x1finalkills") || value.equals("x1finaldeaths") || value.equals("x1bedsdestroyeds") || value.equals("x1bedslosteds") || value.equals("x1wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("x2kills") || value.equals("x2deaths") || value.equals("x2") || value.equals("x2games") || value.equals("x2finalkills") || value.equals("x2finaldeaths") || value.equals("x2bedsdestroyeds") || value.equals("x2bedslosteds") || value.equals("x2wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("4v4kills") || value.equals("4v4deaths") || value.equals("4v4") || value.equals("4v4games") || value.equals("4v4finalkills") || value.equals("4v4finaldeaths") || value.equals("4v4bedsdestroyeds") || value.equals("4v4bedslosteds") || value.equals("4v4wins")) {
        return StringUtils.formatNumber(profile.getStats(table, value));
      } else if (value.equals("coins")) {
        return StringUtils.formatNumber(profile.getCoins(table));
      }
    }
    
    return null;
  }
}
