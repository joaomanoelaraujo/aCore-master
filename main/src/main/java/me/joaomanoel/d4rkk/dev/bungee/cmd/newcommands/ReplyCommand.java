package me.joaomanoel.d4rkk.dev.bungee.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.bungee.LanguageBungee;
import me.joaomanoel.d4rkk.dev.bungee.cmd.Commands;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReplyCommand extends Commands {
   public ReplyCommand() {
      super("r", "reply");
   }

   public void perform(CommandSender sender, String[] args) {
      if (!(sender instanceof ProxiedPlayer)) {
         sender.sendMessage(TextComponent.fromLegacyText(LanguageBungee.general$only_players));
         return;
      }

      ProxiedPlayer player = (ProxiedPlayer)sender;

      if (args.length == 0) {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.reply$usage));
         return;
      }

      if (!TellCommand.TELL_CACHE.containsKey(player.getName())) {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.reply$error$no_target));
         return;
      }

      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(TellCommand.TELL_CACHE.get(player.getName()));
      if (target != null && target.isConnected()) {
         String message = StringUtils.join((Object[])args, " ");
         if (player.hasPermission("aCore.tell.color")) {
            message = StringUtils.formatColors(message);
         }

         TellCommand.TELL_CACHE.put(player.getName(), target.getName());
         TellCommand.TELL_CACHE.put(target.getName(), player.getName());

         String toTarget = LanguageBungee.reply$receive
                 .replace("{sender}", Role.getColored(player.getName()))
                 .replace("{message}", message);

         String toSender = LanguageBungee.reply$send
                 .replace("{target}", Role.getColored(target.getName()))
                 .replace("{message}", message);

         target.sendMessage(TextComponent.fromLegacyText(toTarget));
         player.sendMessage(TextComponent.fromLegacyText(toSender));
      } else {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.reply$error$no_target));
      }
   }
}
