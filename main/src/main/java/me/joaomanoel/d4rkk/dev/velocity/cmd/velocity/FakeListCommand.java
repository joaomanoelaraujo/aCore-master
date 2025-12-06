package me.joaomanoel.d4rkk.dev.velocity.cmd.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class FakeListCommand implements SimpleCommand {

    private final VelocityPlugin plugin;

    public FakeListCommand(VelocityPlugin plugin) {
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
        if (!player.hasPermission("aCore.cmd.fakelist")) {
            player.sendMessage(deserialize("§cYou don't have permission to use this command."));
            return;
        }

        List<String> nicked = VelocityPlugin.listNicked();
        StringBuilder sb = new StringBuilder();

        for (int index = 0; index < nicked.size(); index++) {
            sb.append("§c").append(VelocityPlugin.getFake(nicked.get(index)))
                    .append(" §fis actually ")
                    .append("§a").append(nicked.get(index))
                    .append(index + 1 == nicked.size() ? "" : "\n");
        }

        nicked.clear();

        if (sb.length() == 0) {
            sb.append("§cThere are no users using a fake nickname.");
        }

        player.sendMessage(deserialize(" \n§eList of fake nicknames:\n \n" + sb + "\n "));
    }

    private Component deserialize(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text);
    }
}