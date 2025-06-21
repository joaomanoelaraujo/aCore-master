package me.joaomanoel.d4rkk.dev.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.cmd.Commands;
import me.joaomanoel.d4rkk.dev.cosmetic.types.JoinMessage;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;
import me.joaomanoel.d4rkk.dev.menus.MenuCosmetic;
import me.joaomanoel.d4rkk.dev.menus.others.MenuCosmeticNoback;
import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CsmCommand extends Commands {
   public CsmCommand() {
      super("csm");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cOnly players can use this command.");
      } else {
         Player player = (((Player) sender).getPlayer());
         Profile profile = Profile.getProfile(player.getPlayer().getName());
         new MenuCosmeticNoback<>(profile, LanguageAPI.getConfig(profile).getString("cosmetic.join_message_name"), JoinMessage.class);
      }
   }
}
