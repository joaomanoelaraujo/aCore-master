package me.joaomanoel.d4rkk.dev.bungee.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.bungee.cmd.Commands;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

public class TellCommand extends Commands {
   public static Map<String, String> TELL_CACHE = new HashMap<>();

   public TellCommand() {
      super("tell");
   }

   public void perform(CommandSender sender, String[] args) {
      if (!(sender instanceof ProxiedPlayer)) {
         sender.sendMessage(TextComponent.fromLegacyText("§cOnly players can use this command."));
      } else {
         ProxiedPlayer player = (ProxiedPlayer)sender;
         if (args.length < 2) {
            player.sendMessage(TextComponent.fromLegacyText("§cUsage: /tell [player] [message]"));
         } else {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
            if (target != null && target.isConnected()) {
               if (target.equals(player)) {
                  player.sendMessage(TextComponent.fromLegacyText("§cYou cannot send private messages to yourself."));
               } else {
                  boolean canReceiveTell = Database.getInstance().getPreference(player.getName(), "pm", true);
                  boolean canReceiveTellT = Database.getInstance().getPreference(target.getName(), "pm", true);
                  if (!canReceiveTell) {
                     player.sendMessage(TextComponent.fromLegacyText("§cYou cannot send private messages with private messages disabled."));
                  } else if (!canReceiveTellT) {
                     player.sendMessage(TextComponent.fromLegacyText("§cThis user has disabled private messages."));
                  } else {
                     String message = StringUtils.join(args, 1, " ");
                     if (player.hasPermission("aCore.tell.color")) {
                        message = StringUtils.formatColors(message);
                     }

                     TELL_CACHE.put(player.getName(), target.getName());
                     TELL_CACHE.put(target.getName(), player.getName());
                     target.sendMessage(TextComponent.fromLegacyText("§8Message from: " + Role.getColored(player.getName()) + "§8: §6" + message));
                     player.sendMessage(TextComponent.fromLegacyText("§8Message to: " + Role.getColored(target.getName()) + "§8: §6" + message));
                  }
               }
            } else {
               player.sendMessage(TextComponent.fromLegacyText("§cPlayer not found."));
            }
         }
      }
   }
}
