package me.joaomanoel.d4rkk.dev.cmd;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand extends Commands {

  public CoinsCommand() {
    super("coins");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      Profile profile = Profile.getProfile(player.getName());
      player.sendMessage("\n§eYour Coins:");

      // Exibe os valores de Coins e Limites para cada modo
      for (String name : new String[]{"BedWars", "SkyWars", "TheBridge", "ThePit", "Duels"}) {
        // Obtenha o valor de coins
        double coins = profile.getCoins("aCore" + name.replace(" ", ""));

        // Verifica se o modo é SkyWars para exibir o limite
        if (name.equals("SkyWars")) {
          double limit = profile.getLimitedCoins("aCore" + name.replace(" ", ""));
          player.sendMessage(" §8▪ §f" + name + " §7Coins: §f" + StringUtils.formatNumber(coins) + "§f/§7" + limit);
        } else {
          // Para os outros modos, apenas exibe os coins
          player.sendMessage(" §8▪ §f" + name + " §7Coins: §f" + StringUtils.formatNumber(coins));
        }
      }

      player.sendMessage("\n");
    } else {
      sender.sendMessage("§cOnly players can use this command.");
    }
  }
}
