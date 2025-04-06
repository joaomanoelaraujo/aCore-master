package me.joaomanoel.d4rkk.dev.bungee.cmd;

import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.bungee.Bungee;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static me.joaomanoel.d4rkk.dev.bungee.Bungee.ALEX;
import static me.joaomanoel.d4rkk.dev.bungee.Bungee.STEVE;

public class FakeCommand extends Commands {

  public FakeCommand() {
    super("fake");
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

    if (Bungee.getRandomNicks().stream().noneMatch(Bungee::isUsable)) {
      player.sendMessage(TextComponent.fromLegacyText(" \n §c§lCHANGE NICKNAME\n \n §cNo nickname is available for use at the moment.\n "));
      return;
    }

    if (args.length == 0) {
      Bungee.sendRole(player, null);
      return;
    }

    String roleName = args[0];
    if (!Bungee.isFakeRole(roleName)) {
      Bungee.sendRole(player, "VILLAGER_NO");
      return;
    }

    if (Role.getRoleByName(roleName) == null) {
      Bungee.sendRole(player, "VILLAGER_NO");
      return;
    }

    if (args.length == 1) {
      Bungee.sendSkin(player, roleName, "ORB_PICKUP");
      return;
    }

    String skin = args[1];
    if (!skin.equalsIgnoreCase("alex") && !skin.equalsIgnoreCase("steve") && !skin.equalsIgnoreCase("you")) {
      Bungee.sendSkin(player, roleName, "VILLAGER_NO");
      return;
    }

    List<String> enabled = Bungee.getRandomNicks().stream().filter(Bungee::isUsable).collect(Collectors.toList());
    String fakeName = enabled.isEmpty() ? null : enabled.get(ThreadLocalRandom.current().nextInt(enabled.size()));
    if (fakeName == null) {
      player.sendMessage(TextComponent.fromLegacyText(" \n §c§lCHANGE NICKNAME\n \n §cNo nickname is available for use at the moment.\n "));
      return;
    }

    enabled.clear();
    Bungee.applyFake(player, fakeName, roleName, skin.equalsIgnoreCase("steve") ? STEVE : skin.equalsIgnoreCase("you") ? Manager.getSkin(player.getName(), "signature") : ALEX);
  }
}
