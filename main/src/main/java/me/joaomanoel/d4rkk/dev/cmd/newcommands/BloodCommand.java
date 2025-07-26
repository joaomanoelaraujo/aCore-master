package me.joaomanoel.d4rkk.dev.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.cmd.Commands;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BloodCommand extends Commands {
   public BloodCommand() {
      super("blood");
   }

   public void perform(CommandSender sender, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("§cOnly players can use this command.");
      } else {
         Player player = (((Player) sender).getPlayer());
         Profile profile = Profile.getProfile(player.getPlayer().getName());
         EnumSound.ITEM_PICKUP.play(player, 0.5F, 2.0F);
         profile.getPreferencesContainer().changeBloodAndGore();
      }
   }
}
