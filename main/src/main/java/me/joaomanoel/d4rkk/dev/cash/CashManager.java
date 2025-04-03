package me.joaomanoel.d4rkk.dev.cash;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import org.bukkit.entity.Player;

public class CashManager {

  public static final boolean CASH;
  private static final KConfig CONFIG;

  static {
    CONFIG = Core.getInstance().getConfig("utils");
    if (!CONFIG.contains("cash")) {
      CONFIG.set("cash", true);
    }

    CASH = CONFIG.getBoolean("cash");
  }

  public static void addCash(Profile profile, long amount) throws CashException {
    if (profile == null) {
      throw new CashException("The user must be online to change the cash");
    }

    profile.setStats("aCoreProfile", profile.getStats("aCoreProfile", "cash") + amount, "cash");
  }

  public static void addCash(Player player, long amount) throws CashException {
    addCash(player.getName(), amount);
  }

  public static void addCash(String name, long amount) throws CashException {
    addCash(Profile.getProfile(name), amount);
  }

  public static void removeCash(Profile profile, long amount) throws CashException {
    if (profile == null) {
      throw new CashException("The user must be online to change the cash");
    }

    profile.setStats("aCoreProfile", profile.getStats("aCoreProfile", "cash") - amount, "cash");
  }

  public static void removeCash(Player player, long amount) throws CashException {
    removeCash(player.getName(), amount);
  }

  public static void removeCash(String name, long amount) throws CashException {
    removeCash(Profile.getProfile(name), amount);
  }

  public static void setCash(Profile profile, long amount) throws CashException {
    if (profile == null) {
      throw new CashException("The user must be online to change the cash");
    }

    profile.setStats("aCoreProfile", amount, "cash");
  }

  public static void setCash(Player player, long amount) throws CashException {
    setCash(player.getName(), amount);
  }

  public static void setCash(String name, long amount) throws CashException {
    setCash(Profile.getProfile(name), amount);
  }

  public static long getCash(Profile profile) {
    long cash = 0L;
    if (profile != null) {
      cash = profile.getStats("aCoreProfile", "cash");
    }

    return cash;
  }

  public static long getCash(Player player) {
    return getCash(player.getName());
  }

  public static long getCash(String name) {
    return getCash(Profile.getProfile(name));
  }
}
