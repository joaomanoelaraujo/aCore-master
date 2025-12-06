package me.joaomanoel.d4rkk.dev.velocity.cmd.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class FakeResetCommand implements SimpleCommand {

    private final VelocityPlugin plugin;

    public FakeResetCommand(VelocityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!(sender instanceof Player)) {
            sender.sendMessage(deserialize("§cOnly players can use this command."));
            return;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("aCore.cmd.fake")) {
            player.sendMessage(deserialize("§cYou don't have permission to use this command."));
            return;
        }

        if (!VelocityPlugin.isFake(player.getUsername())) {
            player.sendMessage(deserialize("§cYou are not using a fake nickname."));
            return;
        }

        VelocityPlugin.removeFake(player);
    }

    private Component deserialize(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text);
    }
}