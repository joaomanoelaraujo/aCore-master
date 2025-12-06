package me.joaomanoel.d4rkk.dev.velocity.cmd.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.velocity.LanguageVelocity;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import me.joaomanoel.d4rkk.dev.velocity.party.VelocityParty;
import me.joaomanoel.d4rkk.dev.velocity.party.VelocityPartyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class PartyChatCommand implements SimpleCommand {

    private final VelocityPlugin plugin;

    public PartyChatCommand(VelocityPlugin plugin) {
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
        if (args.length == 0) {
            player.sendMessage(deserialize(LanguageVelocity.party$chat$usage));
            return;
        }

        VelocityParty party = VelocityPartyManager.getMemberParty(player.getUsername());
        if (party == null) {
            player.sendMessage(deserialize(LanguageVelocity.party$error$not_in_party));
            return;
        }

        String message = LanguageVelocity.party$chat$format
                .replace("{player}", Role.getPrefixed(player.getUsername()))
                .replace("{message}", StringUtils.join(args, " "));

        party.broadcast(message);
    }

    private Component deserialize(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text);
    }
}