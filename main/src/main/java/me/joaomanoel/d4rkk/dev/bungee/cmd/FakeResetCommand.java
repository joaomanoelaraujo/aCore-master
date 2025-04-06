package me.joaomanoel.d4rkk.dev.bungee.cmd;

import me.joaomanoel.d4rkk.dev.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FakeResetCommand extends Commands {

  public FakeResetCommand() {
    super("fakereset");
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cOnly players can use this command."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (!player.hasPermission("aCore.cmd.fake")) {
      player.sendMessage(TextComponent.fromLegacyText("§cYou don't have permission to use this command."));
      return;
    }

    if (!Bungee.isFake(player.getName())) {
      player.sendMessage(TextComponent.fromLegacyText("§cYou are not using a fake nickname."));
      return;
    }

    Bungee.removeFake(player);
  }
}
