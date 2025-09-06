package me.joaomanoel.d4rkk.dev.bungee.cmd;

import me.joaomanoel.d4rkk.dev.bungee.LanguageBungee;
import me.joaomanoel.d4rkk.dev.bungee.party.BungeeParty;
import me.joaomanoel.d4rkk.dev.bungee.party.BungeePartyManager;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyChatCommand extends Commands {

  public PartyChatCommand() {
    super("p");
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText(LanguageBungee.general$only_players));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$chat$usage));
      return;
    }

    BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
    if (party == null) {
      player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.party$error$not_in_party));
      return;
    }

    String message = LanguageBungee.party$chat$format
            .replace("{player}", Role.getPrefixed(player.getName()))
            .replace("{message}", StringUtils.join(args, " "));

    party.broadcast(message);
  }
}
