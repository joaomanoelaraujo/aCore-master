package me.joaomanoel.d4rkk.dev.bungee.cmd;

import me.joaomanoel.d4rkk.dev.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class FakeListCommand extends Commands {

  public FakeListCommand() {
    super("fakelist");
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cOnly players can use this command."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (!player.hasPermission("aCore.cmd.fakelist")) {
      player.sendMessage(TextComponent.fromLegacyText("§cYou don't have permission to use this command."));
      return;
    }

    List<String> nicked = Bungee.listNicked();
    StringBuilder sb = new StringBuilder();
    for (int index = 0; index < nicked.size(); index++) {
      sb.append("§c").append(Bungee.getFake(nicked.get(index))).append(" §fis actually ").append("§a").append(nicked.get(index)).append(index + 1 == nicked.size() ? "" : "\n");
    }

    nicked.clear();
    if (sb.length() == 0) {
      sb.append("§cThere are no users using a fake nickname.");
    }

    player.sendMessage(TextComponent.fromLegacyText(" \n§eList of fake nicknames:\n \n" + sb + "\n "));
  }
}
