package me.joaomanoel.d4rkk.dev.velocity.cmd.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.velocity.LanguageVelocity;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import me.joaomanoel.d4rkk.dev.velocity.cmd.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.HashMap;
import java.util.Map;

public class TellCommand implements SimpleCommand {

    private static final Map<String, String> REPLY_MAP = new HashMap<>();
    private final VelocityPlugin plugin;

    public TellCommand(VelocityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!(sender instanceof Player)) {
            sender.sendMessage(deserialize(LanguageVelocity.general$only_players));
            return;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(deserialize(LanguageVelocity.tell$usage));
            return;
        }

        String targetName = args[0];
        Player target = plugin.getServer().getPlayer(targetName).orElse(null);

        if (target == null) {
            player.sendMessage(deserialize(LanguageVelocity.tell$error$player_not_found));
            return;
        }

        if (target.getUsername().equalsIgnoreCase(player.getUsername())) {
            player.sendMessage(deserialize(LanguageVelocity.tell$error$self));
            return;
        }

        // Verifica se o remetente pode enviar mensagens
        if (!Database.getInstance().getPreference(player.getUsername().toLowerCase(), "pm", true)) {
            player.sendMessage(deserialize(LanguageVelocity.tell$error$sender_disabled));
            return;
        }

        // Verifica se o destinatário pode receber mensagens
        if (!Database.getInstance().getPreference(target.getUsername().toLowerCase(), "pm", true)) {
            player.sendMessage(deserialize(LanguageVelocity.tell$error$target_disabled));
            return;
        }

        // Constrói a mensagem
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]);
            if (i < args.length - 1) {
                messageBuilder.append(" ");
            }
        }
        String message = messageBuilder.toString();

        // Envia as mensagens
        String senderPrefix = Role.getPrefixed(player.getUsername());
        String targetPrefix = Role.getPrefixed(target.getUsername());

        String receiveMessage = LanguageVelocity.tell$receive
                .replace("{sender}", senderPrefix)
                .replace("{message}", message);

        String sendMessage = LanguageVelocity.tell$send
                .replace("{target}", targetPrefix)
                .replace("{message}", message);

        target.sendMessage(deserialize(receiveMessage));
        player.sendMessage(deserialize(sendMessage));

        // Atualiza o mapa de reply
        REPLY_MAP.put(player.getUsername().toLowerCase(), target.getUsername());
        REPLY_MAP.put(target.getUsername().toLowerCase(), player.getUsername());
    }

    public static String getReplyTarget(String playerName) {
        return REPLY_MAP.get(playerName.toLowerCase());
    }

    public static void removeReplyTarget(String playerName) {
        REPLY_MAP.remove(playerName.toLowerCase());
    }

    private Component deserialize(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text);
    }
}