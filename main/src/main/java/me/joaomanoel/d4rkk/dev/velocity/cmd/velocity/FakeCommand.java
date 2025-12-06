package me.joaomanoel.d4rkk.dev.velocity.cmd.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin.ALEX;
import static me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin.STEVE;

public class FakeCommand implements SimpleCommand {

    private final VelocityPlugin plugin;

    public FakeCommand(VelocityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!(sender instanceof Player)) {
            sender.sendMessage(deserialize("§cOnly players can use this command."));
            return;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("aCore.cmd.fake")) {
            player.sendMessage(deserialize("§cYou don't have permission to use this command."));
            return;
        }

        try {
            if (VelocityPlugin.getRandomNicks().stream().noneMatch(VelocityPlugin::isUsable)) {
                player.sendMessage(deserialize(" \n §c§lCHANGE NICKNAME\n \n §cNo nickname is available for use at the moment.\n "));
                return;
            }
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }

        if (args.length == 0) {
            // TODO: Implement sendRole equivalent for Velocity
            player.sendMessage(deserialize("§eUse: /fake <role> <skin>"));
            return;
        }

        String roleName = args[0];
        try {
            if (!VelocityPlugin.isFakeRole(roleName)) {
                player.sendMessage(deserialize("§cInvalid role for fake."));
                return;
            }
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }

        if (Role.getRoleByName(roleName) == null) {
            player.sendMessage(deserialize("§cRole not found."));
            return;
        }

        if (args.length == 1) {
            player.sendMessage(deserialize("§eChoose a skin: steve, alex, or you"));
            return;
        }

        String skin = args[1];
        if (!skin.equalsIgnoreCase("alex") && !skin.equalsIgnoreCase("steve") && !skin.equalsIgnoreCase("you")) {
            player.sendMessage(deserialize("§cInvalid skin. Use: steve, alex, or you"));
            return;
        }

        List<String> enabled = null;
        try {
            enabled = VelocityPlugin.getRandomNicks().stream()
                    .filter(VelocityPlugin::isUsable)
                    .collect(Collectors.toList());
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }

        String fakeName = enabled.isEmpty() ? null : enabled.get(ThreadLocalRandom.current().nextInt(enabled.size()));
        if (fakeName == null) {
            player.sendMessage(deserialize(" \n §c§lCHANGE NICKNAME\n \n §cNo nickname is available for use at the moment.\n "));
            return;
        }

        enabled.clear();
        String skinData = skin.equalsIgnoreCase("steve") ? STEVE :
                skin.equalsIgnoreCase("you") ? Manager.getSkin(player.getUsername(), "signature") :
                        ALEX;

        VelocityPlugin.applyFake(player, fakeName, roleName, skinData);
    }

    private Component deserialize(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text);
    }
}