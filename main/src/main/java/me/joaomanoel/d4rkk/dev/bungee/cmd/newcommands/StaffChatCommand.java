package me.joaomanoel.d4rkk.dev.bungee.cmd.newcommands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
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
         sender.sendMessage(TextComponent.fromLegacyText("§cOnly players can use this command."));
      } else {
         ProxiedPlayer player = (ProxiedPlayer)sender;
         if (!player.hasPermission("aCore.cmd.staffchat")) {
            player.sendMessage(TextComponent.fromLegacyText("§cYou do not have permission to use this command."));
         } else if (args.length == 0) {
            player.sendMessage(TextComponent.fromLegacyText("§cUsage: /sc [message] or /sc [enable/disable]"));
         } else {
            String message = args[0];
            if (message.equalsIgnoreCase("enable")) {
               player.sendMessage(TextComponent.fromLegacyText("§aStaffChat enabled."));
               IGNORE.remove(player.getName());
            } else if (message.equalsIgnoreCase("disable")) {
               player.sendMessage(TextComponent.fromLegacyText("§cStaffChat disabled."));
               IGNORE.add(player.getName());
            } else {
               String format = StringUtils.formatColors(StringUtils.join((Object[])args, " "));
               BungeeCord.getInstance().getPlayers().stream().filter((pplayer) -> pplayer.hasPermission("aCore.cmd.staffchat") && !IGNORE.contains(pplayer.getName())).forEach((pplayer) -> {
                  ByteArrayDataOutput out = ByteStreams.newDataOutput();
                  out.writeUTF("STAFF_BAR");
                  out.writeUTF(pplayer.getName());
                  pplayer.getServer().sendData("aCore", out.toByteArray());
                  pplayer.sendMessage(TextComponent.fromLegacyText("§3[SC] §7[" + StringUtils.capitalise(player.getServer().getInfo().getName().toLowerCase()) + "] §7" + Role.getPrefixed(player.getName(), true) + "§f: " + format));
               });
            }
         }
      }
   }
}
