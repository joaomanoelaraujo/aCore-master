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
public class PT_BR {
  
  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger())
      .getModule("LANGUAGE");
  private static final KConfig CONFIG = Core.getInstance().getConfig("translate/PT_BR");


  public static String discord$link = "https://discord.gg/aaa";
  public static String loja$link = "www.yourserver.com";

    // Exemplo de strings para os itens de menu
    public static String profileo$menu$levelInfo = "SKULL_ITEM:3 : 1 : name>%s : desc>§7Nível: §6%s\n§7Guilda: §b%s\n§7Rank: %s";
  public static String profileo$menu$socialMedia = "SKULL_ITEM:3 : 1 : name>§aRedes Sociais : desc>§eClique para ver! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFiN2EwYzIxMGU2Y2RmNWEzNWZkODE5N2U2ZTI0YTAzODMxNWJiZTNiZGNkMWJjYzM2MzBiZjI2ZjU5ZWM1YyJ9fX0=";
  public static String profileo$menu$statistics = "PAPER : 1 : name>§aEstatísticas : desc>§7Clique para ver as estatísticas de %s";
  public static String profileo$menu$partyInvite = "DIAMOND : 1 : name>§aConvidar para a Party : desc>§7Clique aqui para convidar %s para a party!";
  public static String profileo$menu$friendInvite = "386 : 1 : name>§aAdicionar Amigo : desc>§7Clique para adicionar %s como amigo";
  public static String profileo$menu$blockPlayer = "BARRIER : 1 : name>§aBloquear : desc>§7Clique para bloquear %s";
  public static String profileo$menu$guildInvite = "SKULL_ITEM:3 : 1 : name>§aConvidar para a Guilda : desc>§7Clique para convidar %s para a guilda! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRhZmEyZDNlNGU2ZGZmNDI5MTcxMzExYTljYzJiYWIyNjYwYjYyOTUyNzYyNzM0ZDU2OTQ1OTJhMjgwNzk4YSJ9fX0=";
  public static String profileo$menu$leadershipPass = "SKULL_ITEM:3 : 1 : name>§aPassar liderança : desc>§7Clique para passar a liderança da party para %s : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWJkYjc1MDY3Mjk0MTFjMzE3ODhmNDAzZDA3NjgxN2NiNGQwYTJhYmFmZDNkZjk2MGRmMmY1ZDBiNGRiYmZlOSJ9fX0=";
  public static String profileo$menu$close = "ARROW : 1 : name>§cFechar";

  public static String menuprofile$color = "160:1 : 1 : name>§7";
  public static String menuprofile$color_party = "160:11 : 1 : name>§7";
  public static String profile$title = "Perfil";
  public static String player$enter = "§5§l[PARTY] §aDigite o nome de um jogador:";
  public static String deliveries$title = "Entregas";

  public static String cosmetics$punchmessage$icon$perm_desc$start =
          "§8Punch Message\n\n§7Select the {name} Punch Message for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n \n{perm_desc_status}";
  public static String cosmetics$punchmessage$icon$buy_desc$start =
          "§8Punch Message\n\n§7Select the {name} Punch Message for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n \n{buy_desc_status}";
  public static String cosmetics$punchmessage$icon$has_desc$start =
          "§8Punch Message\n\n§7Select the {name} Punch Message for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n\n{has_desc_status}";

  public static String cosmetics$mvpcolor$icon$perm_desc$start =
          "§8Cor MVP\n \n{perm_desc_status}";
  public static String cosmetics$mvpcolor$icon$buy_desc$start =
          "§8Cor MVP\n \n{buy_desc_status}";
  public static String cosmetics$mvpcolor$icon$has_desc$start =
          "§8Cor MVP\n \n{has_desc_status}";

  public static String titles$title = "Título";

  public static String profile$menu$preferences = "REDSTONE_COMPARATOR : 1 : hide>all : name>&aPreferências : desc>&7No nosso servidor, você pode personalizar completamente\n&7sua experiência de jogo.\n&7Personalize várias opções exclusivas como\n&7desejar!\n \n&8As opções incluem habilitar ou desabilitar\n&8mensagens privadas, jogadores e outros.\n \n&eClique para personalizar opções!";

  public static String profile$menu$titles = "MAP : 1 : hide>all : name>&aTítulos : desc>&7Mostre seu estilo com um título exclusivo\n&7que será exibido acima da sua cabeça\n&7para outros jogadores.\n \n&8Lembre-se de que você não verá o título,\n&8apenas outros jogadores verão.\n \n&eClique para selecionar um título!";

  public static String profile$menu$boosters = "POTION:8232 : 1 : hide>all : name>&aMultiplicadores de Moeda : desc>&7No nosso servidor, há um sistema de\n&7&6Multiplicadores de Moeda &7que afetam\n&7a quantidade de &6Moedas &7ganhas em partidas.\n \n&8Os multiplicadores podem ser pessoais ou globais,\n&8beneficiando você e até outros jogadores.\n \n&eClique para ver seus multiplicadores!";

  public static String profile$menu$challenges = "GOLD_INGOT : 1 : name>&aDesafios : desc>&7No nosso servidor, há um sistema de\n&7&6Desafios &7que consistem em missões únicas\n&7que concedem várias recompensas vitalícias.\n \n&8As recompensas variam de títulos, moedas,\n&8ícones de prestígio e outros cosméticos.\n \n&eClique para ver os desafios!";

  public static String profile$menu$statistics = "PAPER : 1 : name>&aEstatísticas : desc>&7Veja suas estatísticas para\n&7cada Minigame no nosso servidor.\n \n&eClique para ver as estatísticas!";

  public static String profile$menu$cosmetics = "416 : 1 : name>&aCosméticos : desc>&7Veja seus cosméticos para\n&7cada servidor.\n \n&eClique para ver!";

  public static String profile$menu$profile = "SKULL_ITEM:3 : 1 : name>&aInformações Pessoais : desc>&fRank: {rank}\n\n&fCash: &b{cash}\n\n&fRegistrado: &7{created}\n&fÚltimo Acesso: {last}";

  public static String profile$mfriends = "SKULL_ITEM:3 : 1 : name>§aAmigos : desc>§7Veja o perfil dos seus amigos e\n§7interaja com aqueles que estão\n§7online! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGU0ODU4ZWUyMzI2MmEzMTRhMGQwZGExN2Q4NzEzOWI5MzhlMzBlZjk2MmQwNDE3N2E4NjVhNGIwZDM4MTA2ZSJ9fX0=";
  public static String kick$title$menu = "Expulsar Jogador";
  public static String profile$mparty = "SKULL_ITEM:3 : 1 : name>§aParty : desc>§7Crie uma party para jogar com\n§7outros jogadores! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWJkYjc1MDY3Mjk0MTFjMzE3ODhmNDAzZDA3NjgxN2NiNGQwYTJhYmFmZDNkZjk2MGRmMmY1ZDBiNGRiYmZlOSJ9fX0=";
  public static String profile$apparence = "299 : 1 : name>§aPersonalizar Aparência : desc>\n§7Personalize as seguintes opções visuais\n§7para você mesmo!\n §f▪ Cor do Rank MVP+\n §f▪ Mensagens de Soco\n\n§eClique para ver! : paint>27:27:118";
  public static String profile$status = "145:0 : 1 : name>§aStatus da Conta : desc>§7Verifique o status da sua conta e seu\n§7histórico de punições. \n\n§eClique para ver!";
  public static String party$kickm = "§aVocê expulsou {member} §a da party.";
  public static String party$invite = "386 : 1 : name>§aConvidar Jogador : desc>§7Convide um jogador para sua party.";
  public static String party$kick = "166 : 1 : name>§aExpulsar Jogador : desc>§7Expulse um jogador da sua party.";
  public static String party$push = "405 : 1 : name>§aEmpurrar Party : desc>§7Teletransporte todos os membros da party para\n§7seu lobby.";
  public static String party$undo = "TNT : 1 : name>§aDesfazer Party : desc>§7Excluir a party atual.";
  public static String party$search = "SIGN : 1 : name>§aProcurar Jogadores";
  public static String party$create = "159:2 : 1 : name>§aCriar Party : desc>\n§eClique para convidar um\n§ejogador para sua party";

  public static String profile$language = "SKULL_ITEM:3 : 1 : name>§aIdiomas : desc>§7Mude seu idioma\n\n§7Atualmente disponíveis: {languages} \n\n§7Mais idiomas estarão disponíveis em breve!\n\n§eClique para mudar o idioma! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjE1MWNmZmRhZjMwMzY3MzUzMWE3NjUxYjM2NjM3Y2FkOTEyYmE0ODU2NDMxNThlNTQ4ZDU5YjJlYWQ1MDExIn19fQ==";
  public static int profile$slot$lang = 40;

  public static String menu$apparence$title = "Personalizar aparência";
  public static int menu$apparence$rows = 4;
  public static String menu$apparence$color = "351:0 : 1 : name>§aCor do Rank MVP+ : desc>§7Jogadores §bMVP§c+§7 podem\n§7desbloquear e alterar a cor\n§7do +.\n\n§eClique para alterar!";
  public static String menu$apparence$punch = "395 : 1 : name>§aSoco Mensagens : desc>§7Personalize a mensagem ao usar\n§7sua habilidade de soco!\n\n§cOs Socos nos lobbies são\n§cexclusivos para §bMVP§c+ ou para\n§caqueles que possuem o item Socar\n§camigos.\n\n§eClique para alterar!";
  public static String menu$apparence$status = "421 : 1 : name>§aStatus : desc>§7Defina um status a ser exibido acima\n§7de sua cabeça nos lobbies.\n\n§eClique para alterar!";
  public static int apparence$status$slot = 15;
  public static int apparence$color$slot = 11;
  public static int apparence$punch$slot = 13;

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
  public static String party$role = "§6Líder"; // or "Member"
  public static String party$rmember = "§7Membro"; // ou "Membro"
  public static String party$guild = "§bNenhum";
  public static String party$member = "SKULL_ITEM:3 : 1 : owner>%name% : name>§a%name% : desc>§7Cargo: %role%\n§7Status: %status%\n§7Guild: %guild%";
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


  public static String actionBar$queueMessage = "§aVocê está na fila para entrar no servidor §8{server} §7(Posição #{position})";

  public static String connecting = "Conectando...";

  public static String already = "§cVocê já está na fila de conexão!";
  public static String connection$notsucess = "§Não foi possível conectar a este servidor no momento!";

  public static String gamemode$mode = "Modo não encontrado.";
  public static String gamemode$changed = "§aVocê mudou o modo de jogo de {mode} para {to}.";
  public static String gamemode$changer = "§aSeu modo de jogo foi alterado para {type}.";

  public static String vanish$mode = "§Você está invisível para outros jogadores!";

  public static String waiting$timer = "§Você precisa esperar {more} vezes para usá-lo novamente.";
  public static String visibility$on = "§aVisibilidade dos jogadores ativada.";
  public static String visibility$off = "§aVisibilidade dos jogadores desativada.";
  public static String menu$back = "ARROW : 1 : name>&cVoltar";
  public static String booster$type = "Global";
  public static String booster$type2 = "Pessoal";
  public static String booster$list$item = "POTION{type} : 1 : hide>all : name>&aBooster {boosterType} : desc>&fMultiplicador: &6{multiplier}x\n&fDuração: &7{duration}\n \n&eClique para ativar o booster!";
  public static String booster$list$empty = "WEB : 1 : name>&cVocê não tem nenhum Booster!";
  public static String booster$network$notInMinigame = "§cVocê precisa estar em um servidor de Minigame para ativar este Booster.";
  public static String booster$network$alreadyActive = "§cJá existe um Booster Global ativo para {minigame}.";
  public static String booster$network$activated = " \n {roleColor}{player} §7ativou um §6Booster Global de Moedas§7.\n §bTODOS §7os jogadores recebem um bônus de {multiplier}x §7em jogos de §6{minigame}§7.\n ";
  public static String booster$personal$alreadyActive = "§cVocê já tem um Booster Pessoal ativo.";
  public static String booster$personal$activated = "§aVocê ativou um §6Booster de Moeda {multiplier}x §8({duration})§a.";

  public static String menu$achievements$title = "Desafios";
  public static String menu$achievements$thepit = "SKULL_ITEM:3 : 1 : name>&aThe Pit : desc>&fDesafios: {color}{completed}/{max}\n \n&eClique para ver! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNDVkNzI1NTg3Mjk4YWRhYTc5NmU3MjRkNDhlZmIyOThkNzA2YWE4MjhhN2E0YzJhZjBjYmM5YzNjMiJ9fX0=";
  public static String menu$achievements$bedwars = "SKULL_ITEM:3 : 1 : name>&aBed Wars : desc>&fDesafios: {color}{completed}/{max}\n \n&eClique para ver! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZiMjkwYTEzZGY4ODI2N2VhNWY1ZmNmNzk2YjYxNTdmZjY0Y2NlZTVjZDM5ZDQ2OTcyNDU5MWJhYmVlZDFmNiJ9fX0=";
  public static String menu$achievements$skywars = "SKULL_ITEM:3 : 1 : name>&aSky Wars : desc>&fDesafios: {color}{completed}/{max}\n \n&eClique para ver! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDUyOGVkNDU4MDI0MDBmNDY1YjVjNGUzYTZiN2E5ZjJiNmE1YjNkND c4OTI1Y2M1ZDk4ODM5MWM3ZCJ9fX0=";
  public static String menu$achievements$thebridge = "SKULL_ITEM:3 : 1 : name>&aThe Bridge : desc>&fDesafios: {color}{completed}/{max}\n \n&eClique para ver! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRhZjg1MzVjNTQ3ZTFkNjhmMmEyMGRkOGVlZTk5YzE2NTAzMTU3N2FhZTYwZWEyZTJlMmNjMTgzMDkwZjU3ZSJ9fX0=";


  public static String menu$preferences$title = "Preferências";
  public static String menu$preferences$players = "347 : 1 : name>&aJogadores : desc>&7Ative ou desative\n&7jogadores no lobby.";
  public static String menu$preferences$privateMessages = "PAPER : 1 : name>&aMensagens Privadas : desc>&7Ative ou desative\n&7mensagens enviadas através do /tell.";
  public static String menu$preferences$violence = "REDSTONE : 1 : name>&aViolência : desc>&7Ative ou desative\n&7partículas de sangue no PvP.";
  public static String menu$preferences$protectionLobby = "NETHER_STAR : 1 : name>&aProteção no /lobby : desc>&7Ative ou desative\n&7solicitação de confirmação ao usar /lobby.";
  public static String menu$preferences$chatMention = "MAP : 1 : hide>all : name>&aMenção no Chat : desc>&7Ative ou desative menções em\n&7chat.";
  public static String menu$preferences$state = "INK_SACK:{inkSack} : 1 : name>{name} : desc>&fEstado: &7{state}\n \n&eClique para modificar!";


  public static String menu$boosters$title = "Multiplicadores";
  public static String menu$boosters$personal = "POTION : 1 : name>&aMultiplicadores Pessoais : desc>&7Concede um &6Multiplicador de Moedas &7apenas\n&7para &bVOCÊ &7em todos os minigames do servidor\n&7por um curto período de tempo.\n \n&eClique para ver seus multiplicadores!";
  public static String menu$boosters$global = "POTION:8232 : 1 : hide>all : name>&aMultiplicadores Globais : desc>&7Concede um &6Multiplicador de Moedas &7para\n&bTODOS &7os jogadores em apenas um minigame\n&7por um curto período de tempo.\n \n&eClique para ver seus multiplicadores!";
  public static String menu$boosters$active = "&fMultiplicador Pessoal Ativo:\n {activeMultiplier}";
  public static String menu$boosters$none = "&cVocê não tem nenhum multiplicador ativo.";
  public static String menu$boosters$calculation = "&8• &6Multiplicador {multiplier}x &8({time})\n \n&fCálculo:\n &7Com o multiplicador ativo ao receber &650 Moedas &7o\n &7total recebido será equivalente a &6{total} Moedas&7.";
  public static String menu$boosters$network = " &8• &b{minigame}: {status}";
  public static String menu$boosters$networkActive = "&6{multiplier}x &7para {player} &8({time})";
  public static String menu$boosters$networkDisabled = "&cDesativado";
  public static String menu$boosters$credits = "PAPER : 1 : name>&aMultiplicadores de Crédito : desc>&7Os Multiplicadores são cumulativos. Quanto mais\n&7multiplicadores ativos, maior o bônus recebido.\n \n&fMultiplicadores Globais:\n{network}\n \n{result}";

  public static String menu$statistics$title = "Estatísticas";
  public static String menu$statistics$bedwars = "SKULL_ITEM:3 : 1 : name>&aBedWars : desc>&eGeral: \n  &fVitórias: &7%aCore_BedWars_wins%\n  &fPartidas: &7%aCore_BedWars_games%\n \n&fMoedas: &6%aCore_BedWars_coins% : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZiMjkwYTEzZGY4ODI2N2VhNWY1ZmNmNzk2YjYxNTdmZjY0Y2NlZTVjZDM5ZDQ2OTcyNDU5MWJhYmVlZDFmNiJ9fX0=";

  public static String menu$statistics$skywars = "SKULL_ITEM:3 : 1 : name>&aSkyWars : desc>&eGeral: \n  &fVitórias: &7%aCore_SkyWars_wins%\n  &fPartidas: &7%aCore_SkyWars_games%\n  &fPontos: &7%aCore_SkyWars_rankedpoints%\n \n&fMoedas: &6%aCore_SkyWars_coins% : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDUyOGVkNDU4MDI0MDBmNDY1YjVjNGUzYTZiN2E5ZjJiNmE1YjNkNDc4YjZmZDg0OTI1Y2M1ZDk4ODM5MWM3ZCJ9fX0=";

  public static String menu$statistics$thebridge = "SKULL_ITEM:3 : 1 : name>&aThe Bridge : desc>&e1v1:\n  &fAbates: &7%aCore_TheBridge_1v1kills%\n  &fMortes: &7%aCore_TheBridge_1v1deaths%\n  &fPontos: &7%aCore_TheBridge_1v1points%\n  &fVitórias: &7%aCore_TheBridge_1v1wins%\n  &fPartidas: &7%aCore_TheBridge_1v1games%\n&e2v2:\n  &fAbates: &7%aCore_TheBridge_2v2kills%\n  &fMortes: &7%aCore_TheBridge_2v2deaths%\n  &fPontos: &7%aCore_TheBridge_2v2points%\n  &fVitórias: &7%aCore_TheBridge_2v2wins%\n  &fPartidas: &7%aCore_TheBridge_2v2games%\n \n&eSequência de Vitórias:\n  &fDiária: &7%aCore_TheBridge_winstreak%\n \n&fMoedas: &6%aCore_TheBridge_coins% : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRhZjg1MzVjNTQ3ZTFkNjhmMmEyMGRkOGVlZTk5YzE2NTAzMTU3N2FhZTYwZWEyZTJlMmNjMTgzMDkwZjU3ZSJ9fX0=";

  public static String menu$statistics$thepit = "SKULL_ITEM:3 : 1 : name>&aThe Pit : desc>&eGeral: \n  &fAbates: &7%aCore_ThePit_kills%\n  &fMortes: &7%aCore_ThePit_deaths%\n  &fAssistências: &7%aCore_ThePit_assists%\n \n&fMoedas: &6%aCore_BedWars_coins% : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNDVkNzI1NTg3Mjk4YWRhYTc5NmU3MjRkNDhlZmIyOThkNzA2YWE4MjhhN2E0YzJhZjBjYmM5YzNjMiJ9fX0=";

  public static String menu$minigames$contribute = "BOOK : 1 : name>§bContribua para o servidor : desc>§7Você pode ajudar o servidor tornando-se\n§eVIP§7!\n\n§7Além de ajudar a manter o servidor,\n§7você também terá acesso a vários\n§7benefícios exclusivos!";

  public static int menu$minigames$slot = 32;
  public static boolean menu$minigames$active = true;

  public static String cosmetics$color$locked = "§c";
  public static String cosmetics$color$canbuy = "§e";
  public static String cosmetics$color$unlocked = "§a";
  public static String cosmetics$color$selected = "§a";
  public static String cosmetics$icon$perm_desc$common = "§cVocê não tem permissão.";
  public static String cosmetics$icon$perm_desc$role = "§7Exclusivo para {role} §7ou superior.";
  public static String cosmetics$icon$buy_desc$enough = "§cVocê não tem saldo suficiente.";
  public static String cosmetics$icon$buy_desc$click_to_buy = "§eClique para comprar!";
  public static String cosmetics$icon$has_desc$select = "§aDESBLOQUEADO!\n§eClique para selecionar!";
  public static String cosmetics$icon$has_desc$selected = "§aDESBLOQUEADO!\n§eClique para desselecionar!";
  public static String language$icon$has_desc$select = "§eClique para selecionar!";
  public static String language$icon$has_desc$selected = "§eClique para desselecionar!";

  public static String cosmetics$coloredtag$icon$perm_desc$start =
          "§8Tag Colorida\n\n§7Selecione a cor {name} para\n§7mensagens no chat do jogo!\n\n§eClique com o botão direito para pré-visualizar! \n\n§7Raridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$coloredtag$icon$buy_desc$start =
          "§8Tag Colorida\n\n§7Selecione a cor {name} para\n§7mensagens no chat do jogo!\n\n§eClique com o botão direito para pré-visualizar! \n\n§7Raridade: {rarity}\n§7Custo: §2{cash}\n \n{buy_desc_status}";
  public static String cosmetics$coloredtag$icon$has_desc$start =
          "§8Tag Colorida\n\n§7Selecione a cor {name} para\n§7mensagens no chat do jogo!\n\n§eClique com o botão direito para pré-visualizar! \n\n§7Raridade: {rarity}\n \n{has_desc_status}";

  public static String cosmetics$joinmessage$icon$perm_desc$start =
          "§8Mensagem de Entrada\n\n§7Selecione a Mensagem de Entrada {name} para\n§7mensagens no chat do jogo!\n\n§eClique com o botão direito para pré-visualizar! \n\n§7Raridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$joinmessage$icon$buy_desc$start =
          "§8Mensagem de Entrada\n\n§7Selecione a Mensagem de Entrada {name} para\n§7mensagens no chat do jogo!\n\n§eClique com o botão direito para pré-visualizar! \n\n§7Raridade: {rarity}\n§7Custo: §2{cash}\n \n{buy_desc_status}";
  public static String cosmetics$joinmessage$icon$has_desc$start =
          "§8Mensagem de Entrada\n\n§7Selecione a Mensagem de Entrada {name} para\n§7mensagens no chat do jogo!\n\n§eClique com o botão direito para pré-visualizar! \n\n§7Raridade: {rarity}\n \n{has_desc_status}";
  public static String menucosmetics$join_message = "323 : 1 : name>&aMensagem de Entrada : desc>&7Ao entrar no lobby, anuncie\n§7sua entrada com estilo! \n\n&7Desbloqueados: {color}{owned}/{max} &8({percentage}%)\n§7Selecionado Atualmente: {selected} \n\n&eClique para ver!";
  public static String menucosmetics$colored_tag = "38:7 : 1 : name>&aTag Colorida : desc>&7Mude a cor da tag e\n§7fique estiloso! \n\n&7Desbloqueados: {color}{owned}/{max} &8({percentage}%)\n§7Selecionado Atualmente: {selected} \n\n&eClique para ver!";

  public static String cosmetic$join_message_name = "Mensagem de Entrada";
  public static String cosmetic$coloredtag_name = "Tag Colorida";
  public static String message$nohaveperm = "§cVocê não tem permissão suficiente para isso.";
  public static String menubuycashsearch$title = "Confirmar";

  public static String main$menu_buy_cancel = "159:14 : 1 : name>&cCancelar";

  public static String main$menu_confirm_cash = "159:13 : 1 : name>&aConfirmar : desc>&7Desbloquear \"{cosmetic_name}\"\n&7Custo: &2{cosmetic_cash}&7.";
  public static String main$not_enough_coins = "§cVocê não tem Moedas suficientes para concluir esta transação.";
  public static String main$cosmetic_purchased = "§aVocê comprou '{name}'!";
  public static int menu$cosmetic_slot$back = 48;
  public static String menu$cosmetic$back = "ARROW : 1 : name>§aVoltar";
  public static String message$deathmessage = "\n§eMensagens que podem ser exibidas ao entrar no lobby:\n \n";
  public static String message$coloredtag = "\n§eSua tag ficará assim:\n {color} {role}\n";

  public static boolean menuprofile$glass = true;
  public static String menucosmetics$empty_slot = "160:7 : 1 : name>§7";

  public static String party$only_players = "§cSomente jogadores podem usar este comando.";
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
    for (Field field : PT_BR.class.getDeclaredFields()) {
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