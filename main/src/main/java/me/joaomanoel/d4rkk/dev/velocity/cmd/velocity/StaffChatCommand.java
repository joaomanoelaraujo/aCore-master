package me.joaomanoel.d4rkk.dev.velocity.cmd.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.velocity.LanguageVelocity;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import me.joaomanoel.d4rkk.dev.velocity.cmd.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.HashMap;
import java.util.Map;

public class StaffChatCommand implements SimpleCommand {

    private static final Map<String, Boolean> STAFFCHAT_TOGGLE = new HashMap<>();
    private static final String PERMISSION = "aCore.staffchat";
    private final VelocityPlugin plugin;

    public StaffChatCommand(VelocityPlugin plugin) {
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

        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage(deserialize(LanguageVelocity.general$no_permission));
            return;
        }

        // Toggle ou enviar mensagem
        if (args.length == 0) {
            player.sendMessage(deserialize(LanguageVelocity.staffchat$usage));
            return;
        }

        String firstArg = args[0].toLowerCase();

        // Ativar/Desativar StaffChat
        if (firstArg.equals("enable") || firstArg.equals("on")) {
            STAFFCHAT_TOGGLE.put(player.getUsername().toLowerCase(), true);
            player.sendMessage(deserialize(LanguageVelocity.staffchat$enabled));
            return;
        } else if (firstArg.equals("disable") || firstArg.equals("off")) {
            STAFFCHAT_TOGGLE.put(player.getUsername().toLowerCase(), false);
            player.sendMessage(deserialize(LanguageVelocity.staffchat$disabled));
            return;
        }

        // Enviar mensagem ao staffchat
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            messageBuilder.append(args[i]);
            if (i < args.length - 1) {
                messageBuilder.append(" ");
            }
        }
        String message = messageBuilder.toString();

        String serverName = player.getCurrentServer()
                .map(s -> s.getServerInfo().getName())
                .orElse("Unknown");

        String formattedMessage = LanguageVelocity.staffchat$format
                .replace("{server}", serverName)
                .replace("{player}", Role.getPrefixed(player.getUsername()))
                .replace("{message}", message);

        // Broadcast para todos com permissão
        Component component = deserialize(formattedMessage);
        plugin.getServer().getAllPlayers().stream()
                .filter(p -> p.hasPermission(PERMISSION))
                .forEach(p -> p.sendMessage(component));
    }

    public static boolean isStaffChatEnabled(String playerName) {
        return STAFFCHAT_TOGGLE.getOrDefault(playerName.toLowerCase(), false);
    }

    public static void broadcastStaffChat(Player sender, String message) {
        String serverName = sender.getCurrentServer()
                .map(s -> s.getServerInfo().getName())
                .orElse("Unknown");

        String formattedMessage = LanguageVelocity.staffchat$format
                .replace("{server}", serverName)
                .replace("{player}", Role.getPrefixed(sender.getUsername()))
                .replace("{message}", message);

        Component component = LegacyComponentSerializer.legacySection().deserialize(formattedMessage);

        VelocityPlugin.getInstance().getServer().getAllPlayers().stream()
                .filter(p -> p.hasPermission(PERMISSION))
                .forEach(p -> p.sendMessage(component));
    }

    private Component deserialize(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text);
    }
}