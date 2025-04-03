package me.joaomanoel.d4rkk.dev.languages.translates;


import me.joaomanoel.d4rkk.dev.Core;
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
public class EN_US {
  
  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger())
      .getModule("LANGUAGE");
  private static final KConfig CONFIG = Core.getInstance().getConfig("translate/EN_US");

  public static String discord$link = "https://discord.gg/aaa";
  public static String loja$link = "www.yourserver.com";

    // Exemplo de strings para os itens de menu
    public static String profileo$menu$levelInfo = "SKULL_ITEM:3 : 1 : name>%s : desc>§7Level: §6%s\n§7Guild: §b%s\n§7Rank: %s";
    public static String profileo$menu$socialMedia = "SKULL_ITEM:3 : 1 : name>§aSocial Midias : desc>§eClick to view! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFiN2EwYzIxMGU2Y2RmNWEzNWZkODE5N2U2ZTI0YTAzODMxNWJiZTNiZGNkMWJjYzM2MzBiZjI2ZjU5ZWM1YyJ9fX0=";
    public static String profileo$menu$statistics = "PAPER : 1 : name>§aStatistics : desc>§7Click to see %s's statistics";
    public static String profileo$menu$partyInvite = "DIAMOND : 1 : name>§aInvite to Party : desc>§7Click here to invite %s to the party!";
    public static String profileo$menu$friendInvite = "386 : 1 : name>§aAdd Friend : desc>§7Click to add %s as a friend";
    public static String profileo$menu$blockPlayer = "BARRIER : 1 : name>§aBlock : desc>§7Click to block %s";
    public static String profileo$menu$guildInvite = "SKULL_ITEM:3 : 1 : name>§aInvite to Guild : desc>§7Click to invite %s to the guild! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRhZmEyZDNlNGU2ZGZmNDI5MTcxMzExYTljYzJiYWIyNjYwYjYyOTUyNzYyNzM0ZDU2OTQ1OTJhMjgwNzk4YSJ9fX0=";
    public static String profileo$menu$leadershipPass = "SKULL_ITEM:3 : 1 : name>§aPass leadership : desc>§7Click to pass Party leadership to %s : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWJkYjc1MDY3Mjk0MTFjMzE3ODhmNDAzZDA3NjgxN2NiNGQwYTJhYmFmZDNkZjk2MGRmMmY1ZDBiNGRiYmZlOSJ9fX0=";
    public static String profileo$menu$close = "ARROW : 1 : name>§cClose";


  public static String menuprofile$color = "160:1 : 1 : name>§7";
  public static String menuprofile$color_party = "160:11 : 1 : name>§7";
  public static String profile$title = "Profile";
  public static String player$enter = "§5§l[PARTY] §aEnter a player's name:";
  public static String deliveries$title = "Deliveries";

  public static String titles$title = "Title";

  public static String profile$menu$preferences = "REDSTONE_COMPARATOR : 1 : hide>all : name>&aPreferences : desc>&7On our server you can fully customize\n&7your gaming experience.\n&7Customize various unique options as\n&7you desire!\n \n&8Options include enabling or disabling\n&8private messages, players, and others.\n \n&eClick to customize options!";

  public static String profile$menu$titles = "MAP : 1 : hide>all : name>&aTitles : desc>&7Show off style with an exclusive title\n&7that will be displayed above your head\n&7to other players.\n \n&8Remember you wont see the title,\n&8only other players will.\n \n&eClick to select a title!";

  public static String profile$menu$boosters = "POTION:8232 : 1 : hide>all : name>&aCoin Multipliers : desc>&7On our server theres a system of\n&7&6Coin Multipliers &7that affect\n&7the amount of &6Coins &7earned in matches.\n \n&8Multipliers can be personal or global,\n&8benefiting you and even other players.\n \n&eClick to view your multipliers!";

  public static String profile$menu$challenges = "GOLD_INGOT : 1 : name>&aChallenges : desc>&7On our server theres a system of\n&7&6Challenges &7which consist of unique missions\n&7granting you various lifelong rewards.\n \n&8Rewards range from titles, coins,\n&8prestige icons, and other cosmetics.\n \n&eClick to view challenges!";

  public static String profile$menu$statistics = "PAPER : 1 : name>&aStatistics : desc>&7View your statistics for\n&7each Minigame on our server.\n \n&eClick to view statistics!";

  public static String profile$menu$cosmetics = "416 : 1 : name>&aCosmetics : desc>&7View your cosmetics for\n&7each server.\n \n&eClick to view!";


  public static String profile$menu$profile = "SKULL_ITEM:3 : 1 : name>&aPersonal Information : desc>&fRank: {rank}\n\n&fCash: &b{cash}\n\n&fRegistered: &7{created}\n&fLast Access: {last}";

  public static String profile$mfriends = "SKULL_ITEM:3 : 1 : name>§aFriends : desc>§7View the profile of your friends and\n§7interact with those who are\n§7online! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGU0ODU4ZWUyMzI2MmEzMTRhMGQwZGExN2Q4NzEzOWI5MzhlMzBlZjk2MmQwNDE3N2E4NjVhNGIwZDM4MTA2ZSJ9fX0=";
  public static String kick$title$menu = "Kick Player";
  public static String profile$mparty = "SKULL_ITEM:3 : 1 : name>§aParty : desc>§7Create a party to play with\n§7other players! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWJkYjc1MDY3Mjk0MTFjMzE3ODhmNDAzZDA3NjgxN2NiNGQwYTJhYmFmZDNkZjk2MGRmMmY1ZDBiNGRiYmZlOSJ9fX0=";
  public static String profile$apparence = "299 : 1 : name>§aCustomize Appearance : desc>\n§7Customize the following visual\n§7options for yourself!\n §f▪ MVP+ Rank Color\n §f▪ Punch Messages\n\n§eClick to view! : paint>27:27:118";
  public static String profile$status = "145:0 : 1 : name>§aAccount Status : desc>§7Check the status of your account and its\n§7punishment history. \n\n§eClick to view!";
  public static String party$kickm = "§aYou kicked {member} §a from the party.";
  public static String party$invite = "386 : 1 : name>§aInvite Player : desc>§7Invite a player to your party.";
  public static String party$kick = "166 : 1 : name>§aKick Player : desc>§7Kick a player from your party.";
  public static String party$push = "405 : 1 : name>§aPush Party : desc>§7Teleport all party members to\n§7your lobby.";
  public static String party$undo = "TNT : 1 : name>§aUndo Party : desc>§7Delete the current party.";
  public static String party$search = "SIGN : 1 : name>§aSearch Players";
  public static String party$create = "159:2 : 1 : name>§aCreate Party : desc>\n§eClick to invite a\n§eplayer to your party";

  public static String profile$language = "SKULL_ITEM:3 : 1 : name>§aLanguages : desc>§7Change your language\n\n§7Currently available: {languages} \n\n§7More languages will be available soon!\n\n§eClick to change language! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjE1MWNmZmRhZjMwMzY3MzUzMWE3NjUxYjM2NjM3Y2FkOTEyYmE0ODU2NDMxNThlNTQ4ZDU5YjJlYWQ1MDExIn19fQ==";
  public static int profile$slot$lang = 40;

  @KWriter.YamlEntryInfo(annotation = "Create party slot Menu Party")
  public static int party$cslot = 31;
  @KWriter.YamlEntryInfo(annotation = "Invite party slot Menu Party")
  public static int party$islot = 18;
  @KWriter.YamlEntryInfo(annotation = "Kick party slot Menu Party")
  public static int party$kslot = 19;
  @KWriter.YamlEntryInfo(annotation = "Push party slot Menu Party")
  public static int party$pslot = 20;
  @KWriter.YamlEntryInfo(annotation = "Invite party slot Menu Party")
  public static int party$inviteslot = 18;
  @KWriter.YamlEntryInfo(annotation = "Undo party slot Menu Party")
  public static int party$uslot = 21;
  @KWriter.YamlEntryInfo(annotation = "Search slot Menu Party")
  public static int party$sslot = 26;
  public static String menu$ptitle = "Party";
  @KWriter.YamlEntryInfo(annotation = "Menu Profile and Menu Party Rows, Recommended Rows: 6")
  public static int menu$prows = 6;
  public static String party$online = "§aOnline"; // ou "§cOffline"
  public static String party$offline = "§cOffline";
  public static String party$role = "§6Leader"; // or "Member"
  public static String party$rmember = "§7Member"; // ou "Membro"
  public static String party$guild = "§bNothing";
  public static String party$member = "SKULL_ITEM:3 : 1 : owner>%name% : name>§a%name% : desc>§7Role: %role%\n§7Status: %status%\n§7Guild: %guild%";
  @KWriter.YamlEntryInfo(annotation = "Menu kick rows")
  public static int menu$kcrows = 5;
  @KWriter.YamlEntryInfo(annotation = "Apparence icon slot")
  public static int profile$aslot = 30;
  @KWriter.YamlEntryInfo(annotation = "Status account icon slot")
  public static int profile$sslot = 32;
  @KWriter.YamlEntryInfo(annotation = "Friends Icon Slot")
  public static int profile$fslot = 5;
  @KWriter.YamlEntryInfo(annotation = "Menu Party Slot")
  public static int profile$pslot = 3;
  @KWriter.YamlEntryInfo(annotation = "Preferences menu slot")
  public static int profile$prslot2 = 21;
  @KWriter.YamlEntryInfo(annotation = "Menu Title slot")
  public static int profile$tslot3 = 33;
  @KWriter.YamlEntryInfo(annotation = "Menu Cosmetics slot")
  public static int profile$cosmetics$slot4 = 22;
  @KWriter.YamlEntryInfo(annotation = "Booster slot")
  public static int profile$boosters$slot4 = 31;
  @KWriter.YamlEntryInfo(annotation = "Challenges slot")
  public static int profile$challenges$slot5 = 23;
  @KWriter.YamlEntryInfo(annotation = "Statistics slot")
  public static int profile$statistics$slot1 = 29;
  public static int profile$slot = 4;


  public static String actionBar$queueMessage = "§aYou are in the queue to join the server §8{server} §7(Position #{position})";

  public static String connecting = "Connecting...";

  public static String already = "§cYou are already in the connection queue!";
  public static String connection$notsucess = "§Unable to connect to this server at this time!";

  public static String gamemode$mode = "Mode not found.";
  public static String gamemode$changed = "§aYou changed the game mode from {mode} to {to}.";
  public static String gamemode$changer = "§aYour game mode has been changed to {type}.";

  public static String vanish$mode = "§You are invisible to other players!";

  public static String waiting$timer = "§cYou need to wait {more} times to use it again.";
  public static String visibility$on = "§aPlayer visibility enabled.";
  public static String visibility$off = "§aPlayer visibility disabled.";
  public static String menu$back = "ARROW : 1 : name>&cBack";
  public static String booster$type = "Global";
  public static String booster$type2 = "Personal";
  public static String booster$list$item = "POTION{type} : 1 : hide>all : name>&aBooster {boosterType} : desc>&fMultiplier: &6{multiplier}x\n&fDuration: &7{duration}\n \n&eClick to activate the booster!";
  public static String booster$list$empty = "WEB : 1 : name>&cYou don't have any Boosters!";
  public static String booster$network$notInMinigame = "§cYou need to be on a Minigame server to activate this Booster.";
  public static String booster$network$alreadyActive = "§cThere's already a Global Booster for {minigame} active.";
  public static String booster$network$activated = " \n {roleColor}{player} §7activated a §6Global Coin Booster§7.\n §bALL §7players receive a bonus of {multiplier}x §7in §6{minigame}§7 games.\n ";
  public static String booster$personal$alreadyActive = "§cYou already have an active Personal Booster.";
  public static String booster$personal$activated = "§aYou activated a §6Coin Booster {multiplier}x §8({duration})§a.";

  public static String menu$apparence$title = "Apparence";
  public static String menu$apparence$color = "351:0 : 1 : name>§aMVP+ Rank Color : desc>§7Players ranked §bMVP§c+§7 can\n§7unlock and switch the color of\n§7their +.\n\n§eClick to change!";
  public static String menu$apparence$punch = "395 : 1 : name>§aPunch Messages : desc>§7Customize the message when you use\n§7your punch ability!\n\n§cPunching in lobbies is only available\n§cto §bMVP§c+ players or those who own\n§cthe Punching Friends item from the\n§cTournament Hall.\n\n§eClick to change!";
  public static String menu$apparence$status = "421 : 1 : name>§aStatus : desc>§7Set a status to be displayed above\n§7your head in lobbies.\n\n§eClick to change!";
  public static int apparence$status$slot = 15;
  public static int apparence$color$slot = 11;
  public static int apparence$punch$slot = 13;
  public static int menu$apparence$rows = 4;

  public static String menu$achievements$title = "Challenges";
  public static String menu$achievements$thepit = "SKULL_ITEM:3 : 1 : name>&aThe Pit : desc>&fChallenges: {color}{completed}/{max}\n \n&eClick to view! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNDVkNzI1NTg3Mjk4YWRhYTc5NmU3MjRkNDhlZmIyOThkNzA2YWE4MjhhN2E0YzJhZjBjYmM5YzNjMiJ9fX0=";
  public static String menu$achievements$bedwars = "SKULL_ITEM:3 : 1 : name>&aBed Wars : desc>&fChallenges: {color}{completed}/{max}\n \n&eClick to view! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZiMjkwYTEzZGY4ODI2N2VhNWY1ZmNmNzk2YjYxNTdmZjY0Y2NlZTVjZDM5ZDQ2OTcyNDU5MWJhYmVlZDFmNiJ9fX0=";
  public static String menu$achievements$skywars = "SKULL_ITEM:3 : 1 : name>&aSky Wars : desc>&fChallenges: {color}{completed}/{max}\n \n&eClick to view! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDUyOGVkNDU4MDI0MDBmNDY1YjVjNGUzYTZiN2E5ZjJiNmE1YjNkNDc4YjZmZDg0OTI1Y2M1ZDk4ODM5MWM3ZCJ9fX0=";
  public static String menu$achievements$thebridge = "SKULL_ITEM:3 : 1 : name>&aThe Bridge : desc>&fChallenges: {color}{completed}/{max}\n \n&eClick to view! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRhZjg1MzVjNTQ3ZTFkNjhmMmEyMGRkOGVlZTk5YzE2NTAzMTU3N2FhZTYwZWEyZTJlMmNjMTgzMDkwZjU3ZSJ9fX0=";


  public static String menu$preferences$title = "Preferences";
  public static String menu$preferences$players = "347 : 1 : name>&aPlayers : desc>&7Activate or deactivate\n&7players in the lobby.";
  public static String menu$preferences$privateMessages = "PAPER : 1 : name>&aPrivate Messages : desc>&7Activate or deactivate\n&7messages sent through /tell.";
  public static String menu$preferences$violence = "REDSTONE : 1 : name>&aViolence : desc>&7Activate or deactivate\n&7blood particles in PvP.";
  public static String menu$preferences$protectionLobby = "NETHER_STAR : 1 : name>&aProtection in /lobby : desc>&7Activate or deactivate\n&7confirmation request when using /lobby.";
  public static String menu$preferences$chatMention = "MAP : 1 : hide>all : name>&aChat Mention : desc>&7Activate or deactivate mention in\n&7chat.";
  public static String menu$preferences$state = "INK_SACK:{inkSack} : 1 : name>{name} : desc>&fState: &7{state}\n \n&eClick to modify!";


  public static String menu$boosters$title = "Multipliers";
  public static String menu$boosters$personal = "POTION : 1 : name>&aPersonal Multipliers : desc>&7Grants a &6Coin Multiplier &7only\n&7for &bYOU &7in all server minigames\n&7for a short period of time.\n \n&eClick to view your multipliers!";
  public static String menu$boosters$global = "POTION:8232 : 1 : hide>all : name>&aGlobal Multipliers : desc>&7Grants a &6Coin Multiplier &7for\n&bALL &7players in just one minigame\n&7for a short period of time.\n \n&eClick to view your multipliers!";
  public static String menu$boosters$active = "&fActive Personal Multiplier:\n {activeMultiplier}";
  public static String menu$boosters$none = "&cYou don't have any active multiplier.";
  public static String menu$boosters$calculation = "&8• &6Multiplier {multiplier}x &8({time})\n \n&fCalculation:\n &7With the active multiplier when receiving &650 Coins &7the\n &7total received will be equivalent to &6{total} Coins&7.";
  public static String menu$boosters$network = " &8• &b{minigame}: {status}";
  public static String menu$boosters$networkActive = "&6{multiplier}x &7for {player} &8({time})";
  public static String menu$boosters$networkDisabled = "&cDisabled";
  public static String menu$boosters$credits = "PAPER : 1 : name>&aCredit Multipliers : desc>&7The Multipliers are cumulative. The more\n&7active multipliers, the greater the bonus received.\n \n&fGlobal Multipliers:\n{network}\n \n{result}";


  public static String menu$statistics$title = "Statistics";
  public static String menu$statistics$bedwars = "SKULL_ITEM:3 : 1 : name>&aBedWars : desc>&eGeneral: \n  &fWins: &7%aCore_BedWars_wins%\n  &fGames: &7%aCore_BedWars_games%\n \n&fCoins: &6%aCore_BedWars_coins% : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZiMjkwYTEzZGY4ODI2N2VhNWY1ZmNmNzk2YjYxNTdmZjY0Y2NlZTVjZDM5ZDQ2OTcyNDU5MWJhYmVlZDFmNiJ9fX0=";

  public static String menu$statistics$skywars = "SKULL_ITEM:3 : 1 : name>&aSkyWars : desc>&eGeneral: \n  &fWins: &7%aCore_SkyWars_wins%\n  &fGames: &7%aCore_SkyWars_games%\n  &fPoints: &7%aCore_SkyWars_rankedpoints%\n \n&fCoins: &6%aCore_SkyWars_coins% : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDUyOGVkNDU4MDI0MDBmNDY1YjVjNGUzYTZiN2E5ZjJiNmE1YjNkNDc4YjZmZDg0OTI1Y2M1ZDk4ODM5MWM3ZCJ9fX0=";

  public static String menu$statistics$thebridge = "SKULL_ITEM:3 : 1 : name>&aThe Bridge : desc>&e1v1:\n  &fKills: &7%aCore_TheBridge_1v1kills%\n  &fDeaths: &7%aCore_TheBridge_1v1deaths%\n  &fPoints: &7%aCore_TheBridge_1v1points%\n  &fWins: &7%aCore_TheBridge_1v1wins%\n  &fPlays: &7%aCore_TheBridge_1v1games%\n&e2v2:\n  &fKills: &7%aCore_TheBridge_2v2kills%\n  &fDeaths: &7%aCore_TheBridge_2v2deaths%\n  &fPoints: &7%aCore_TheBridge_2v2points%\n  &fWins: &7%aCore_TheBridge_2v2wins%\n  &fPlays: &7%aCore_TheBridge_2v2games%\n \n&eWinstreak:\n  &fDiário: &7%aCore_TheBridge_winstreak%\n \n&fCoins: &6%aCore_TheBridge_coins% : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRhZjg1MzVjNTQ3ZTFkNjhmMmEyMGRkOGVlZTk5YzE2NTAzMTU3N2FhZTYwZWEyZTJlMmNjMTgzMDkwZjU3ZSJ9fX0=";

  public static String menu$statistics$thepit = "SKULL_ITEM:3 : 1 : name>&aThe Pit : desc>&eGeneral: \n  &fKills: &7%aCore_ThePit_kills%\n  &fDeaths: &7%aCore_ThePit_deaths%\n  &fAssistance: &7%aCore_ThePit_assists%\n \n&fCoins: &6%aCore_BedWars_coins% : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNDVkNzI1NTg3Mjk4YWRhYTc5NmU3MjRkNDhlZmIyOThkNzA2YWE4MjhhN2E0YzJhZjBjYmM5YzNjMiJ9fX0=";


  public static String menu$minigames$contribute = "BOOK : 1 : name>§bContribute to the server : desc>§7You can help the server by becoming\n§eVIP§7!\n\n§7In addition to helping the server maintain itself,\n§7you can also have access to several\n§7exclusive benefits!";
  public static int menu$minigames$slot = 32;
  public static boolean menu$minigames$active = true;

  public static String cosmetics$color$locked = "§c";
  public static String cosmetics$color$canbuy = "§e";
  public static String cosmetics$color$unlocked = "§a";
  public static String cosmetics$color$selected = "§a";
  public static String cosmetics$icon$perm_desc$common = "§cYou don't have permission.";
  public static String cosmetics$icon$perm_desc$role = "§7Exclusive for {role} §7or higher.";
  public static String cosmetics$icon$buy_desc$enough = "§cYou don't have enough balance.";
  public static String cosmetics$icon$buy_desc$click_to_buy = "§eClick to buy!";
  public static String cosmetics$icon$has_desc$select = "§aUNLOCKED!\n§eClick to select!";
  public static String cosmetics$icon$has_desc$selected = "§aUNLOCKED!\n§eClick to deselect!";
  public static String language$icon$has_desc$select = "§eClick to select!";
  public static String language$icon$has_desc$selected = "§eClick to deselect!";

  public static String cosmetics$coloredtag$icon$perm_desc$start =
          "§8Colored Tag\n\n§7Select the {name} color for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n\n§7Rarity: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$coloredtag$icon$buy_desc$start =
          "§8Colored Tag\n\n§7Select the {name} color for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n\n§7Rarity: {rarity}\n§7Cost: §2{cash}\n \n{buy_desc_status}";
  public static String cosmetics$coloredtag$icon$has_desc$start =
          "§8Colored Tag\n\n§7Select the {name} color for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n\n§7Rarity: {rarity}\n \n{has_desc_status}";

  public static String cosmetics$mvpcolor$icon$perm_desc$start =
          "§8Mvp Color\n \n{perm_desc_status}";
  public static String cosmetics$mvpcolor$icon$buy_desc$start =
          "§8Mvp Color\n \n{buy_desc_status}";
  public static String cosmetics$mvpcolor$icon$has_desc$start =
          "§8Mvp Color\n \n{has_desc_status}";


  public static String cosmetics$joinmessage$icon$perm_desc$start =
          "§8Join Message\n\n§7Select the {name} Join Message for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n\n§7Rarity: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$joinmessage$icon$buy_desc$start =
          "§8Join Message\n\n§7Select the {name} Join Message for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n\n§7Rarity: {rarity}\n§7Cost: §2{cash}\n \n{buy_desc_status}";
  public static String cosmetics$joinmessage$icon$has_desc$start =
          "§8Join Message\n\n§7Select the {name} Join Message for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n\n§7Rarity: {rarity}\n \n{has_desc_status}";

  public static String cosmetics$punchmessage$icon$perm_desc$start =
          "§8Punch Message\n\n§7Select the {name} Punch Message for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n \n{perm_desc_status}";
  public static String cosmetics$punchmessage$icon$has_desc$start =
          "§8Punch Message\n\n§7Select the {name} Punch Message for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n \n{has_desc_status}";



  public static String menucosmetics$join_message = "323 : 1 : name>&aJoin Message : desc>&7When entering the lobby, announce\n§7your entrance in style! \n\n&7Unlocked: {color}{owned}/{max} &8({percentage}%)\n§7Currently Selected: {selected} \n\n&eClick to view!";
  public static String menucosmetics$colored_tag = "38:7 : 1 : name>&aColored Tag : desc>&7Change the tag color and\n§7stay in style! \n\n&7Unlocked: {color}{owned}/{max} &8({percentage}%)\n§7Currently Selected: {selected} \n\n&eClick to view!";

  public static String cosmetic$join_message_name = "Join Message";
  public static String cosmetic$coloredtag_name = "Colored Tag";
  public static String message$nohaveperm = "§cYou don't have enough permission for that.";
  public static String menubuycashsearch$title = "Confirm";

  public static String main$menu_buy_cancel = "159:14 : 1 : name>&cCancel";

  public static String main$menu_confirm_cash = "159:13 : 1 : name>&aConfirm : desc>&7Unlock \"{cosmetic_name}\"\n&7Cost: &2{cosmetic_cash}&7.";
  public static String main$not_enough_coins = "§cYou don't have enough Coins to complete this transaction.";
  public static String main$cosmetic_purchased = "§aYou have purchased '{name}'!";
  public static int menu$cosmetic_slot$back = 48;
  public static String menu$cosmetic$back = "ARROW : 1 : name>§aGo Back";
  public static String message$deathmessage = "\n§eMessages that can be displayed when joined lobby:\n \n";
  public static String message$coloredtag = "\n§eYour tag will look like this:\n {color} {role}\n";

  public static boolean menuprofile$glass = true;
  public static String menucosmetics$empty_slot = "160:7 : 1 : name>§7";

  public static String party$only_players = "§cOnly players can use this command.";
  public static String party$chat_usage = "§cUse /p [message] to chat with your Party.";
  public static String party$not_in_party = "§cYou are not in a Party.";
  public static String party$chat_format = "§d[Party] {prefix}§f: {message}";
  public static String party$help_message = " \n§6/p [message] §f- §7Chat with party members.\n§6/party open §f- §7Make the party public.\n§6/party close §f- §7Make the party private.\n§6/party join [player] §f- §7Join a public party.\n§6/party accept [player] §f- §7Accept an invitation.\n§6/party help §f- §7Display this help message.\n§6/party invite [player] §f- §7Invite a player.\n§6/party delete §f- §7Delete the party.\n§6/party kick [player] §f- §7Kick a member.\n§6/party info §f- §7View information about your Party.\n§6/party deny [player] §f- §7Deny an invitation.\n§6/party leave §f- §7Leave the Party.\n§6/party transfer [player] §f- §7Transfer leadership to another member.\n ";
  public static String party$not_leader = "§cYou are not the Party leader.";
  public static String party$already_public = "§cYour party is already public.";
  public static String party$opened = "§aYou have opened the party to any player.";
  public static String party$already_private = "§cYour party is already private.";
  public static String party$closed = "§cYou have closed the party to invited players only.";
  public static String party$join_usage = "§cUse /party join [player]";
  public static String party$cant_join_own = "§cYou cannot join your own party.";
  public static String party$already_in_party = "§cYou already belong to a Party.";
  public static String party$not_leader_target = "§c{player} is not a Party leader.";
  public static String party$closed_party = "§c{player}'s Party is closed to invited players only.";
  public static String party$full_party = "§c{player}'s Party is full.";
  public static String party$joined = " \n§aYou have joined {prefix}'s Party!\n ";
  public static String party$accept_usage = "§cUse /party accept [player]";
  public static String party$cant_accept_own = "§cYou cannot accept invitations from yourself.";
  public static String party$no_invite = "§c{player} did not invite you to their Party.";
  public static String party$deleted_broadcast = " \n{prefix} §adeleted the Party!\n ";
  public static String party$deleted = "§aYou have deleted the Party.";
  public static String party$kick_usage = "§cUse /party kick [player]";
  public static String party$not_party_leader = "§cYou are not a Party leader.";
  public static String party$cant_kick_self = "§cYou cannot kick yourself.";
  public static String party$not_in_your_party = "§cThat player does not belong to your Party.";
  public static String party$kicked_broadcast = " \n{kicker} §akicked {kicked} §afrom the Party!\n ";
  public static String party$info_format = " \n§6Leader: {leader}\n§6Public: {public}\n§6Member Limit: §f{members}/{slots}\n§6Members: {member_list}\n ";
  public static String party$deny_usage = "§cUse /party deny [player]";
  public static String party$cant_deny_self = "§cYou cannot deny invitations from yourself.";
  public static String party$denied = " \n§aYou have denied the Party invitation from {prefix}!\n ";
  public static String party$left = "§aYou have left the Party!";
  public static String party$transfer_usage = "§cUse /party transfer [player]";
  public static String party$cant_transfer_self = "§cYou cannot transfer Party leadership to yourself.";
  public static String party$transferred_broadcast = " \n{old_leader} §atransferred Party leadership to {new_leader}§a!\n ";
  public static String party$invite_usage = "§cUse /party invite [player]";
  public static String party$player_not_found = "§cUser not found.";
  public static String party$cant_invite_self = "§cYou cannot send invitations to yourself.";
  public static String party$only_leader_invite = "§cOnly the Party leader can send invitations!";
  public static String party$already_invited = "§cYou have already sent an invitation to {player}.";

  public static String party$invited = " \n{prefix} §ahas been invited to the Party. They have 60 seconds to accept or deny this invitation.\n ";

  public static void setupLanguage() {
    boolean save = false;
    KWriter writer = Core.getInstance().getWriter(CONFIG.getFile(),
            "aCore - Created by D4RKK \nVersion: " + Core.getInstance()
                    .getDescription().getVersion());
    for (Field field : EN_US.class.getDeclaredFields()) {
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