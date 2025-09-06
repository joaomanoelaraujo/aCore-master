package me.joaomanoel.d4rkk.dev.bungee.cmd.newcommands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.joaomanoel.d4rkk.dev.bungee.LanguageBungee;
import me.joaomanoel.d4rkk.dev.bungee.cmd.Commands;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class StaffChatCommand extends Commands {
   public static List<String> IGNORE = new ArrayList<>();

   public StaffChatCommand() {
      super("sc", "staffchat");
   }

   public void perform(CommandSender sender, String[] args) {
      if (!(sender instanceof ProxiedPlayer)) {
         sender.sendMessage(TextComponent.fromLegacyText(LanguageBungee.general$only_players));
         return;
      }

      ProxiedPlayer player = (ProxiedPlayer)sender;
      if (!player.hasPermission("aCore.cmd.staffchat")) {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.general$no_permission));
         return;
      }

      if (args.length == 0) {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.staffchat$usage));
         return;
      }

      String message = args[0];
      if (message.equalsIgnoreCase("enable")) {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.staffchat$enabled));
         IGNORE.remove(player.getName());
      } else if (message.equalsIgnoreCase("disable")) {
         player.sendMessage(TextComponent.fromLegacyText(LanguageBungee.staffchat$disabled));
         IGNORE.add(player.getName());
      } else {
         String format = StringUtils.formatColors(StringUtils.join((Object[])args, " "));
         BungeeCord.getInstance().getPlayers().stream()
                 .filter((pplayer) -> pplayer.hasPermission("aCore.cmd.staffchat") && !IGNORE.contains(pplayer.getName()))
                 .forEach((pplayer) -> {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("STAFF_BAR");
                    out.writeUTF(pplayer.getName());
                    pplayer.getServer().sendData("aCore", out.toByteArray());

                    String msg = LanguageBungee.staffchat$format
                            .replace("{server}", StringUtils.capitalise(player.getServer().getInfo().getName().toLowerCase()))
                            .replace("{player}", Role.getPrefixed(player.getName(), true))
                            .replace("{message}", format);

                    pplayer.sendMessage(TextComponent.fromLegacyText(msg));
                 });
      }
   }
}
