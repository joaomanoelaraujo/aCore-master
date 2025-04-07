package me.joaomanoel.d4rkk.dev.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.cmd.Commands;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.player.Profile;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DiscordCommand extends Commands {
   public DiscordCommand() {
      super("discord", "dc");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cOnly players can use this command.");
      } else {
         Player player = (Player)sender;
         player.closeInventory();
         TextComponent component = new TextComponent("");
         BaseComponent[] var6 = TextComponent.fromLegacyText("\n §eClick ");
         int var7 = var6.length;

         int var8;
         for(var8 = 0; var8 < var7; ++var8) {
            BaseComponent components = var6[var8];
            component.addExtra(components);
         }

         TextComponent click = new TextComponent("HERE");
         click.setColor(ChatColor.YELLOW);
         click.setBold(true);
         click.setClickEvent(new ClickEvent(Action.OPEN_URL, LanguageAPI.getConfig(Profile.getProfile(player.getName())).getString("discord.link")));
         click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Click here to open the server's discord.")));
         component.addExtra(click);
         BaseComponent[] var12 = TextComponent.fromLegacyText(" §eto open the server's discord.\n ");
         var8 = var12.length;

         for(int var13 = 0; var13 < var8; ++var13) {
            BaseComponent components = var12[var13];
            component.addExtra(components);
         }

         player.spigot().sendMessage(component);
      }
   }
}
