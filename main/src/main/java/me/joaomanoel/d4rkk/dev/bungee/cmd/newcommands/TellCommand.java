package me.joaomanoel.d4rkk.dev.bungee.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.bungee.LanguageBungee;
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
         sender.sendMessage(TextComponent.fromLegacyText(LanguageBungee.general$only_players));
         return;
      }

      ProxiedPlayer player = (ProxiedPlayer)sender;

      if (args.length < 2) {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.tell$usage));
         return;
      }

      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
      if (target == null || !target.isConnected()) {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.tell$error$player_not_found));
         return;
      }

      if (target.equals(player)) {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.tell$error$self));
         return;
      }

      boolean canReceiveTell = Database.getInstance().getPreference(player.getName(), "pm", true);
      boolean canReceiveTellT = Database.getInstance().getPreference(target.getName(), "pm", true);

      if (!canReceiveTell) {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.tell$error$sender_disabled));
         return;
      }

      if (!canReceiveTellT) {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.tell$error$target_disabled));
         return;
      }

      String message = StringUtils.join(args, 1, " ");
      if (player.hasPermission("aCore.tell.color")) {
         message = StringUtils.formatColors(message);
      }

      TELL_CACHE.put(player.getName(), target.getName());
      TELL_CACHE.put(target.getName(), player.getName());

      String toTarget = LanguageBungee.tell$receive
              .replace("{sender}", Role.getColored(player.getName()))
              .replace("{message}", message);

      String toSender = LanguageBungee.tell$send
              .replace("{target}", Role.getColored(target.getName()))
              .replace("{message}", message);

      target.sendMessage(TextComponent.fromLegacyText(toTarget));
      player.sendMessage(TextComponent.fromLegacyText(toSender));
   }
}
