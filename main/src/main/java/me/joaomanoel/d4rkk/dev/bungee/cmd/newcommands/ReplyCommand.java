package me.joaomanoel.d4rkk.dev.bungee.cmd.newcommands;

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
         sender.sendMessage(TextComponent.fromLegacyText("§cOnly players can use this command."));
      } else {
         ProxiedPlayer player = (ProxiedPlayer)sender;
         if (args.length == 0) {
            player.sendMessage(TextComponent.fromLegacyText("§cUsage: /r [message]"));
         } else if (!TellCommand.TELL_CACHE.containsKey(player.getName())) {
            player.sendMessage(TextComponent.fromLegacyText("§cYou have no one to reply to."));
         } else {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer((String)TellCommand.TELL_CACHE.get(player.getName()));
            if (target != null && target.isConnected()) {
               String message = StringUtils.join((Object[])args, " ");
               if (player.hasPermission("aCore.tell.color")) {
                  message = StringUtils.formatColors(message);
               }

               TellCommand.TELL_CACHE.put(player.getName(), target.getName());
               TellCommand.TELL_CACHE.put(target.getName(), player.getName());
               target.sendMessage(TextComponent.fromLegacyText("§8Message from: " + Role.getColored(player.getName()) + "§8: §6" + message));
               player.sendMessage(TextComponent.fromLegacyText("§8Message to: " + Role.getColored(target.getName()) + "§8: §6" + message));
            } else {
               player.sendMessage(TextComponent.fromLegacyText("§cYou have no one to reply to."));
            }
         }
      }
   }
}
