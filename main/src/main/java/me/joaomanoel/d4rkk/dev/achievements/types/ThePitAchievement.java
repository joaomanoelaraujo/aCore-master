package me.joaomanoel.d4rkk.dev.achievements.types;

import me.joaomanoel.d4rkk.dev.achievements.Achievement;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.titles.Title;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

public class ThePitAchievement extends Achievement {
  
  protected ThePitReward reward;
  protected String icon;
  protected String[] stats;
  protected int reach;
  
  public ThePitAchievement(ThePitReward reward, String id, String name, String desc, int reach, String... stats) {
    super("thp-" + id, name);
    this.reward = reward;
    this.icon = "%material% : 1 : name>%name% : desc>" + desc + "\n \n&fProgress: %progress%";
    this.stats = stats;
    this.reach = reach;
  }
  
  public static void setupAchievements() {
    Achievement.addAchievement(
            new ThePitAchievement(new CoinsReward(100), "1k1", "Assassin", "&7Defeat a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins", 150,
                    "kills"));

    Achievement.addAchievement(
            new ThePitAchievement(new CoinsReward(100), "1p1", "Assistant", "&7Get a total of %reach%\n&7assists to receive:\n \n &8• &6100 Coins", 150,
                    "assists"));

  }
  
  @Override
  protected void give(Profile profile) {
    this.reward.give(profile);
  }
  
  @Override
  protected boolean check(Profile profile) {
    return profile.getStats("aCoreThePit", this.stats) >= this.reach;
  }
  
  public ItemStack getIcon(Profile profile) {
    long current = profile.getStats("aCoreThePit", this.stats);
    if (current > this.reach) {
      current = this.reach;
    }
    
    return BukkitUtils.deserializeItemStack(
        this.icon.replace("%material%", current == this.reach ? "ENCHANTED_BOOK" : "BOOK").replace("%name%", (current == this.reach ? "&a" : "&c") + this.getName())
            .replace("%current%", StringUtils.formatNumber(current)).replace("%reach%", StringUtils.formatNumber(this.reach))
            .replace("%progress%", (current == this.reach ? "&a" : current > this.reach / 2 ? "&7" : "&c") + current + "/" + this.reach));
  }
  
  interface ThePitReward {
    void give(Profile profile);
  }
  
  static class CoinsReward implements ThePitReward {
    private final double amount;
    
    public CoinsReward(double amount) {
      this.amount = amount;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getDataContainer("aCoreThePit", "coins").addDouble(this.amount);
    }
  }
  
  static class TitleReward implements ThePitReward {
    private final String titleId;
    
    public TitleReward(String titleId) {
      this.titleId = titleId;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getTitlesContainer().add(Title.getById(this.titleId));
    }
  }
}
