package me.joaomanoel.d4rkk.dev.deliveries;

import me.joaomanoel.d4rkk.dev.booster.Booster;
import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.Bukkit;

public class DeliveryReward {

  private RewardType type;
  private Object[] values;

  public DeliveryReward(String reward) {
    if (reward == null) {
      reward = "";
    }

    String[] splitter = reward.split(">");
    RewardType type = RewardType.from(splitter[0]);
    if (type == null || reward.replace(splitter[0] + ">", "").split(":").length < type.getParameters()) {
      this.type = RewardType.COMANDO;
      this.values = new Object[]{"tell {name} §cPrêmio \"" + reward + "\" inválido!"};
      return;
    }

    this.type = type;
    try {
      this.values = type.parseValues(reward.replace(splitter[0] + ">", ""));
    } catch (Exception ex) {
      ex.printStackTrace();
      this.type = RewardType.COMANDO;
      this.values = new Object[]{"tell {name} §cPrêmio \"" + reward + "\" inválido!"};
    }
  }

  public void dispatch(Profile profile) {
    if (profile == null || profile.getPlayer() == null) {
      Bukkit.getLogger().warning("Tentando dar recompensa para profile null!");
      return;
    }

    try {
      if (this.type == RewardType.COMANDO) {
        String command = ((String) this.values[0]).replace("{name}", profile.getName());
        Bukkit.getLogger().info("Executando comando: " + command);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

      } else if (this.type == RewardType.CASH) {
        long currentCash = profile.getStats("aCoreProfile", "cash");
        long newCash = currentCash + (long) this.values[0];
        Bukkit.getLogger().info("Dando cash: " + this.values[0] + " (atual: " + currentCash + " -> novo: " + newCash + ")");
        profile.setStats("aCoreProfile", newCash, "cash");

      } else if (this.type.name().contains("_COINS")) {
        // CORREÇÃO CRÍTICA: Converter o nome corretamente
        String gameName = this.type.name().replace("_COINS", "");
        String gameType = "aCore" + formatGameName(gameName);
        double amount = (double) this.values[0];

        // Verificar se a tabela existe no tableMap
        if (profile.getTableMap() == null || !profile.getTableMap().containsKey(gameType)) {
          Bukkit.getLogger().warning("Tabela " + gameType + " não existe no profile de " + profile.getName() + "! Usando comando alternativo.");

          // SOLUÇÃO ALTERNATIVA: Usar comando para dar as moedas
          String coinCommand = getCoinCommand(this.type, profile.getName(), amount);
          if (coinCommand != null) {
            Bukkit.getLogger().info("Executando comando alternativo: " + coinCommand);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), coinCommand);
          }
          return;
        }

        Bukkit.getLogger().info("Adicionando " + amount + " coins no " + gameType);
        profile.addCoins(gameType, amount);

      } else if (this.type.name().contains("_BOOSTER")) {
        int quantity = (int) this.values[0];
        double multiplier = (double) this.values[1];
        long hours = (long) this.values[2];

        Bukkit.getLogger().info("Dando " + quantity + " boosters (" + multiplier + "x por " + hours + "h)");

        for (int i = 0; i < quantity; i++) {
          profile.getBoostersContainer().addBooster(
                  Booster.BoosterType.valueOf(this.type.name().replace("_BOOSTER", "")),
                  multiplier,
                  hours
          );
        }
      }
    } catch (Exception e) {
      Bukkit.getLogger().severe("Erro ao executar recompensa tipo " + this.type + ": " + e.getMessage());
      e.printStackTrace();
    }
  }

  private String formatGameName(String gameName) {
    switch (gameName) {
      case "BEDWARS":
        return "BedWars";
      case "SKYWARS":
        return "SkyWars";
      case "THEBRIDGE":
        return "TheBridge";
      case "BLOCKSUMO":
        return "BlockSumo";
      case "MURDER":
        return "Murder";
      default:
        // Fallback: primeira letra maiúscula, resto minúsculo
        return gameName.charAt(0) + gameName.substring(1).toLowerCase();
    }
  }

  /**
   * Retorna um comando alternativo para dar moedas quando a tabela não existe
   */
  private String getCoinCommand(RewardType type, String playerName, double amount) {
    switch (type) {
      case BEDWARS_COINS:
        return "bw give " + playerName + "coins " + (int) amount;
      case SKYWARS_COINS:
        return "sw addcoins " + playerName + " " + (int) amount;
      case THEBRIDGE_COINS:
        return "tb addcoins " + playerName + " " + (int) amount;
      case MURDER_COINS:
        return "murder addcoins " + playerName + " " + (int) amount;
      case BLOCKSUMO_COINS:
        return "blocksumo addcoins " + playerName + " " + (int) amount;
      default:
        // Se não houver comando específico, tenta um genérico
        Bukkit.getLogger().warning("Nenhum comando de moedas configurado para " + type);
        return null;
    }
  }

  @Override
  public String toString() {
    return "DeliveryReward{type=" + type + ", values=" + java.util.Arrays.toString(values) + "}";
  }

  private enum RewardType {
    COMANDO(1),
    CASH(1),
    BEDWARS_COINS(1),
    BLOCKSUMO_COINS(1),
    SKYWARS_COINS(1),
    THEBRIDGE_COINS(1),
    MURDER_COINS(1),

    PRIVATE_BOOSTER(3),
    NETWORK_BOOSTER(3);

    private final int parameters;

    RewardType(int parameters) {
      this.parameters = parameters;
    }

    public static RewardType from(String name) {
      for (RewardType type : values()) {
        if (type.name().equalsIgnoreCase(name)) {
          return type;
        }
      }

      return null;
    }

    public int getParameters() {
      return this.parameters;
    }

    public Object[] parseValues(String value) throws Exception {
      if (this == COMANDO) {
        return new Object[]{value};
      } else if (this == CASH) {
        return new Object[]{Long.parseLong(value)};
      } else if (this.name().contains("_COINS")) {
        return new Object[]{Double.parseDouble(value)};
      } else if (this.name().contains("_BOOSTER")) {
        String[] values = value.split(":");
        return new Object[]{
                Integer.parseInt(values[0]), // quantia
                Double.parseDouble(values[1]), // multiplicador
                Long.parseLong(values[2]) // horas
        };
      }

      throw new Exception();
    }
  }
}