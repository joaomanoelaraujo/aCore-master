package me.joaomanoel.d4rkk.dev.player.role;

import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.cache.RoleCache;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Role {
  private static final List<Role> ROLES = new ArrayList<>();
  private final int id;
  private final String name;
  private final String prefix;
  private final String permission;
  private final boolean alwaysVisible;
  private final boolean broadcast;
  private ChatColor tagColor;
  private final boolean fly;

  public Role(String name, String prefix, String permission, boolean alwaysVisible, boolean broadcast, boolean fly) {
    this.id = ROLES.size();
    this.name = StringUtils.formatColors(name);
    this.prefix = StringUtils.formatColors(prefix);
    this.permission = permission;
    this.alwaysVisible = alwaysVisible;
    this.broadcast = broadcast;
    this.fly = fly;
  }
  public boolean canFly() {
    return this.fly;
  }
  public static String getPrefixed(String name) {
    return getPrefixed(name, null, false);
  }

  public static String getPrefixed(String name, String colorCode) {
    return getPrefixed(name, colorCode, false);
  }

  public static String getPrefixed(String name, boolean removeFake) {
    return getPrefixed(name, null, removeFake);
  }
  public static String getColored(String name) {
    return getColored(name, false);
  }

  public static String getColor(String color){
    return getColor(color);
  }

  public static String getPrefixed(String name, String colorCode, boolean removeFake) {
    String prefix = "&7"; // Default color if none is found
    if (!removeFake && Manager.isFake(name)) {
      prefix = Manager.getFakeRole(name).getPrefix();
    } else {
      Object target = Manager.getPlayer(name);
      if (target != null) {
        prefix = getPlayerRole(target, true).getPrefix();
      } else {
        String rs = RoleCache.isPresent(name) ? RoleCache.get(name) : Database.getInstance().getRankAndName(name);
        if (rs != null) {
          prefix = getRoleByName(rs.split(" : ")[0]).getPrefix();
          name = rs.split(" : ")[1];
          if (!removeFake && Manager.isFake(name)) {
            name = Manager.getFake(name);
          }
        }
      }
    }

    // Apply the colored tag if provided
    if (colorCode != null && !colorCode.isEmpty()) {
      prefix = colorCode + prefix.replaceFirst("§[0-9a-fk-or]", "");
    }

    return prefix + name;
  }



  public static String getColored(String name, boolean removeFake) {
    return getTaggedName(name, true, removeFake);
  }

  private static String getTaggedName(String name, boolean onlyColor, boolean removeFake) {
    String prefix = "&7";
    if (!removeFake && Manager.isFake(name)) {
      prefix = Manager.getFakeRole(name).getPrefix();
      if (onlyColor) {
        prefix = StringUtils.getLastColor(prefix);
      }

      return prefix + Manager.getFake(name);
    } else {
      Object target = Manager.getPlayer(name);
      if (target != null) {
        prefix = getPlayerRole(target, true).getPrefix();
        if (onlyColor) {
          prefix = StringUtils.getLastColor(prefix);
        }

        return prefix + name;
      } else {
        String rs = RoleCache.isPresent(name) ? RoleCache.get(name) : Database.getInstance().getRankAndName(name);
        if (rs != null) {
          prefix = getRoleByName(rs.split(" : ")[0]).getPrefix();
          if (onlyColor) {
            prefix = StringUtils.getLastColor(prefix);
          }

          name = rs.split(" : ")[1];
          if (!removeFake && Manager.isFake(name)) {
            name = Manager.getFake(name);
          }

          return prefix + name;
        } else {
          return prefix + name;
        }
      }
    }
  }

  public static Role getRoleByName(String name) {
    Iterator<Role> var1 = ROLES.iterator();

    Role role;
    do {
      if (!var1.hasNext()) {
        return ROLES.get(ROLES.size() - 1);
      }

      role = var1.next();
    } while(!StringUtils.stripColors(role.getName()).equalsIgnoreCase(name));

    return role;
  }

  public static Role getRoleByPermission(String permission) {
    Iterator<Role> var1 = ROLES.iterator();

    Role role;
    do {
      if (!var1.hasNext()) {
        return null;
      }

      role = var1.next();
    } while(!role.getPermission().equals(permission));

    return role;
  }

  public static Role getPlayerRole(Object player) {
    return getPlayerRole(player, false);
  }

  public static String getPlayerTag(String playerName) {
    Object target = Manager.getPlayer(playerName);
    if (target != null) {
      Profile profile = Profile.getProfile(target.toString());
      if (profile != null) {
        DataContainer dataContainer = profile.getDataContainer("aCoreProfile", "tag");
        if (dataContainer != null) {
          Role tagRole = getRoleByName(dataContainer.getAsString());
          String prefix = tagRole.getPrefix();
          System.out.println("Tag do jogador: " + prefix + playerName);
          return prefix + playerName;
        }
      }
    }

    return "&7" + playerName;
  }

  public static Role getPlayerRole(Object player, boolean removeFake) {
    if (!removeFake && Manager.isFake(Manager.getName(player))) {
      return Manager.getFakeRole(Manager.getName(player));
    } else {
      Iterator<Role> var2 = ROLES.iterator();

      Role role;
      do {
        if (!var2.hasNext()) {
          return getLastRole();
        }

        role = var2.next();
      } while(!role.has(player));

      return role;
    }
  }

  public static Role getLastRole() {
    return ROLES.get(ROLES.size() - 1);
  }

  public static List<Role> listRoles() {
    return ROLES;
  }

  public static Role getPlayerTagRole(Object player) {
    return getPlayerTagRole(player, false);
  }

  public static Role getPlayerTagRole(Object player, boolean removeFake) {
    if (!removeFake && Manager.isFake(Manager.getName(player))) {
      DataContainer dataContainer = Profile.getProfile(Manager.getName(player)).getDataContainer("aCoreCoreProfile", "tag");
      return Manager.getFakeRole(dataContainer.toString());
    } else {
      return getPlayerTagRole(player);
    }
  }

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getPrefix() {
    return this.prefix;
  }


  public String getPermission() {
    return this.permission;
  }

  public boolean isDefault() {
    return this.permission.isEmpty();
  }

  public boolean isAlwaysVisible() {
    return this.alwaysVisible;
  }

  public boolean isBroadcast() {
    return this.broadcast;
  }

  public boolean has(Object player) {
    return this.isDefault() || Manager.hasPermission(player, this.permission);
  }
}
