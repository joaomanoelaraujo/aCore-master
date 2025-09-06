package me.joaomanoel.d4rkk.dev.bungee;

import me.joaomanoel.d4rkk.dev.bungee.methods.BungeeWriter;
import me.joaomanoel.d4rkk.dev.bungee.logger.BungeeLogger;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@SuppressWarnings("rawtypes")
public class LanguageBungee {

    public static final BungeeLogger LOGGER = new BungeeLogger(
            Bungee.getInstance().getLogger(), "LANGUAGE"
    );

    private static final Configuration CONFIG = Bungee.getInstance().getConfig();

    public static String general$no_permission = "§cVocê não tem permissão para executar este comando!";
    public static String general$player_not_found = "§cJogador não encontrado!";
    public static String general$invalid_arguments = "§cArgumentos inválidos!";
    public static String general$only_players = "§cOnly players can use this command.";
    public static String general$user_not_found = "§cUser not found.";

    public static String tell$usage = "§cUsage: /tell [player] [message]";
    public static String tell$error$player_not_found = "§cPlayer not found.";
    public static String tell$error$self = "§cYou cannot send private messages to yourself.";
    public static String tell$error$sender_disabled = "§cYou cannot send private messages with private messages disabled.";
    public static String tell$error$target_disabled = "§cThis user has disabled private messages.";
    public static String tell$receive = "§8Message from: {sender}§8: §6{message}";
    public static String tell$send = "§8Message to: {target}§8: §6{message}";

    public static String reply$usage = "§cUsage: /r [message]";
    public static String reply$error$no_target = "§cYou have no one to reply to.";
    public static String reply$receive = "§8Message from: {sender}§8: §6{message}";
    public static String reply$send = "§8Message to: {target}§8: §6{message}";

    public static String party$chat$usage = "§cUtilize /p [mensagem] para conversar com a sua Party.";
    public static String party$error$not_in_party = "§cVocê não pertence a uma Party.";
    public static String party$chat$format = "§d[Party] {player}§f: {message}";

    public static String staffchat$usage = "§cUsage: /sc [message] or /sc [enable/disable]";
    public static String staffchat$enabled = "§aStaffChat enabled.";
    public static String staffchat$disabled = "§cStaffChat disabled.";
    public static String staffchat$format = "§3[SC] §7[{server}] §7{player}§f: {message}";

    public static String party$new_leader = " \n{player} §ahas become the new Party Leader!\n ";
    public static String party$member_left = " \n{player} §ahas left the Party!\n ";
    public static String party$member_kicked = " \n{player} §awas kicked from the Party!\n ";
    public static String party$transfer_leader = " \n{player} §awas promoted to Party Leader!\n ";
    public static String party$pull_members = " \n{leader} §apulled all party members.\n";
    public static String party$help = " \n§6/p [message] §f- §7Communicate with party members.\n§6/party open §f- §7Make the party public.\n§6/party close §f- §7Make the party private.\n§6/party join [player] §f- §7Join a public party.\n§6/party accept [player] §f- §7Accept an invitation.\n§6/party help §f- §7Show this help message.\n§6/party invite [player] §f- §7Invite a player.\n§6/party delete §f- §7Delete the party.\n§6/party kick [player] §f- §7Kick a member.\n§6/party info §f- §7Information about your Party.\n§6/party deny [player] §f- §7Deny an invitation.\n§6/party leave §f- §7Leave the Party.\n§6/party transfer [player] §f- §7Transfer Party leadership to another member.\n ";
    public static String party$not_in_party = "§cYou don't belong to a Party.";
    public static String party$not_leader = "§cYou are not the Party Leader.";
    public static String party$already_public = "§cYour party is already public.";
    public static String party$opened = "§aYou've opened the party to any player.";
    public static String party$already_private = "§cYour party is already private.";
    public static String party$closed = "§cYou've closed the party to invited members only.";
    public static String party$usage_join = "§cUse /party join [player]";
    public static String party$cannot_join_self = "§cYou cannot join your own party.";
    public static String party$already_in_party = "§cYou already belong to a Party.";
    public static String party$target_not_leader = "§c{player} is not a Party Leader.";
    public static String party$party_closed = "§c{player}'s Party is closed to invited members only.";
    public static String party$party_full = "§c{player}'s Party is full.";
    public static String party$joined_party = " \n§aYou've joined {player}'s Party!\n ";
    public static String party$usage_accept = "§cUse /party accept [player]";
    public static String party$cannot_accept_self = "§cYou cannot accept invitations from yourself.";
    public static String party$not_invited = "§c{player} did not invite you to their Party.";
    public static String party$usage_kick = "§cUse /party kick [player]";
    public static String party$not_party_leader = "§cYou are not a Party Leader.";
    public static String party$cannot_kick_self = "§cYou cannot kick yourself.";
    public static String party$player_not_in_party = "§cThat player does not belong to your Party.";
    public static String party$player_kicked = " \n{leader} §akicked {player} §afrom the Party!\n ";
    public static String party$info = " \n§6Leader: {leader}\n§6Public: {public}\n§6Member Limit: §f{current}/{max}\n§6Members: {members}\n ";
    public static String party$usage_deny = "§cUse /party deny [player]";
    public static String party$cannot_deny_self = "§cYou cannot deny invitations from yourself.";
    public static String party$denied_invitation = " \n§aYou've denied the invitation from {player}§a!\n ";
    public static String party$left_party = "§aYou've left the Party!";
    public static String party$usage_transfer = "§cUse /party transfer [player]";
    public static String party$cannot_transfer_self = "§cYou cannot transfer Party leadership to yourself.";
    public static String party$leadership_transferred = " \n{old_leader} §atransferred Party leadership to {new_leader}§a!\n ";
    public static String party$usage_invite = "§cUse /party invite [player]";
    public static String party$cannot_invite_self = "§cYou cannot send invitations to yourself.";
    public static String party$only_leader_invite = "§cOnly the Party Leader can send invitations!";
    public static String party$already_invited = "§cYou've already sent an invitation to {player}.";
    public static String party$target_in_party = "§c{player} already belongs to a Party.";
    public static String party$invitation_sent = " \n{player} §awas invited to the Party. They have 60 seconds to accept or deny this request.\n ";
    public static String party$deleted = " \n{leader} §adeleted the Party!\n ";
    public static String party$party_deleted = "§aYou've deleted the Party.";
    public static String party$summoned = "§aYou've summoned all players to your server.";

    // Novas mensagens transferidas do sistema anterior
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
        try {
            Configuration config = Bungee.getInstance().getConfig().getSection("language");
            File file = new File(Bungee.getInstance().getDataFolder(), "language.yml");

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            BungeeWriter writer = new BungeeWriter(file, config);
            boolean save = false;

            LOGGER.info("Configurando sistema de linguagem...");

            for (Field field : LanguageBungee.class.getDeclaredFields()) {
                if (field.getName().contains("$") && !Modifier.isFinal(field.getModifiers())) {
                    String nativeName = field.getName().replace("$", ".").replace("_", "-");

                    try {
                        Object value;
                        if (config != null && config.contains(nativeName)) {
                            value = config.get(nativeName);
                            if (value instanceof String) {
                                value = StringUtils.formatColors((String) value).replace("\\n", "\n");
                            }
                            field.set(null, value);
                            writer.set(nativeName, value);
                        } else {
                            value = field.get(null);
                            if (value instanceof String) {
                                value = StringUtils.deformatColors((String) value).replace("\n", "\\n");
                            }
                            save = true;
                            writer.set(nativeName, value);
                        }
                    } catch (ReflectiveOperationException e) {
                        LOGGER.log(Level.WARNING, "Erro inesperado ao configurar arquivo de linguagem: ", e);
                    }
                }
            }

            if (save) {
                writer.write();
                try {
                    ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                    LOGGER.info("Arquivo language.yml criado com sucesso!");
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Erro ao carregar language.yml: ", e);
                }

                ProxyServer.getInstance().getScheduler().schedule(
                        Bungee.getInstance(),
                        () -> LOGGER.info("A config §6language.yml §afoi modificada ou criada."),
                        1L, TimeUnit.SECONDS
                );
            } else {
                LOGGER.info("Sistema de linguagem carregado com sucesso!");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro fatal ao configurar sistema de linguagem: ", e);
        }
    }

    /**
     * Obtém uma mensagem formatada do sistema de linguagem
     * @param key A chave da mensagem
     * @return A mensagem formatada
     */
    public static String getMessage(String key) {
        try {
            Field field = LanguageBungee.class.getDeclaredField(key.replace(".", "$").replace("-", "_"));
            Object value = field.get(null);
            return value instanceof String ? (String) value : String.valueOf(value);
        } catch (Exception e) {
            LOGGER.warning("Mensagem não encontrada: " + key);
            return "§cMensagem não encontrada: " + key;
        }
    }
}