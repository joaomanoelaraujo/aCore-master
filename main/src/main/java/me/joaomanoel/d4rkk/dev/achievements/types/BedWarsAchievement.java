package me.joaomanoel.d4rkk.dev.achievements.types;

import me.joaomanoel.d4rkk.dev.Achievements;
import me.joaomanoel.d4rkk.dev.achievements.Achievement;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.titles.Title;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

public class BedWarsAchievement extends Achievement {
  
  protected BedWarsReward reward;
  protected String icon;
  protected String[] stats;
  protected int reach;
  
  public BedWarsAchievement(BedWarsReward reward, String id, String name, String desc, int reach, String... stats) {
    super("bw-" + id, name);
    this.reward = reward;
    this.icon = "%material% : 1 : name>%name% : desc>" + desc + "\n \n&fProgress: %progress%";
    this.stats = stats;
    this.reach = reach;
  }
  
  public static void setupAchievements() {
    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(100), "2k1",
                    Achievements.bedwars$kill$name,
                    Achievements.bedwars$kill$desc,
                    50, "2v2kills"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(500), "2k2",
                    Achievements.bedwars$master_kill$name,
                    Achievements.bedwars$master_kill$desc,
                    250, "2v2kills"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(250), "2w1",
                    Achievements.bedwars$victorious$name,
                    Achievements.bedwars$victorious$desc,
                    50, "2v2wins"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(1000), "2w2",
                    Achievements.bedwars$master_victorious$name,
                    Achievements.bedwars$master_victorious$desc,
                    200, "2v2wins"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(250), "2d1",
                    Achievements.bedwars$destroyer$name,
                    Achievements.bedwars$destroyer$desc,
                    250, "2v2bedsdestroyeds"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(1000), "2d2",
                    Achievements.bedwars$master_destroyer$name,
                    Achievements.bedwars$master_destroyer$desc,
                    1000, "2v2bedsdestroyeds"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(250), "2g1",
                    Achievements.bedwars$persistent$name,
                    Achievements.bedwars$persistent$desc,
                    1000, "2v2games"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(100), "4k1",
                    Achievements.bedwars$quartet_kill$name,
                    Achievements.bedwars$quartet_kill$desc,
                    50, "4v4kills"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(500), "4k2",
                    Achievements.bedwars$quartet_master_kill$name,
                    Achievements.bedwars$quartet_master_kill$desc,
                    250, "4v4kills"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(250), "4w1",
                    Achievements.bedwars$quartet_victorious$name,
                    Achievements.bedwars$quartet_victorious$desc,
                    50, "4v4wins"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(1000), "4w2",
                    Achievements.bedwars$quartet_master_victorious$name,
                    Achievements.bedwars$quartet_master_victorious$desc,
                    200, "4v4wins"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(250), "4d1",
                    Achievements.bedwars$quartet_destroyer$name,
                    Achievements.bedwars$quartet_destroyer$desc,
                    250, "4v4bedsdestroyeds"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(1000), "4d2",
                    Achievements.bedwars$quartet_master_destroyer$name,
                    Achievements.bedwars$quartet_master_destroyer$desc,
                    1000, "4v4bedsdestroyeds"));

    Achievement.addAchievement(
            new BedWarsAchievement(new CoinsReward(250), "4g1",
                    Achievements.bedwars$quartet_persistent$name,
                    Achievements.bedwars$quartet_persistent$desc,
                    1000, "4v4games"));

    Achievement.addAchievement(
            new BedWarsAchievement(new TitleReward("bwk"), "bwk",
                    Achievements.bedwars$title$kill$name,
                    Achievements.bedwars$title$kill$desc,
                    500, "2v2kills", "4v4kills"));

    Achievement.addAchievement(
            new BedWarsAchievement(new TitleReward("bww"), "bww",
                    Achievements.bedwars$title$win$name,
                    Achievements.bedwars$title$win$desc,
                    400, "2v2wins", "4v4wins"));

    Achievement.addAchievement(
            new BedWarsAchievement(new TitleReward("bwp"), "bwp",
                    Achievements.bedwars$title$destroy$name,
                    Achievements.bedwars$title$destroy$desc,
                    2000, "2v2bedsdestroyeds", "4v4bedsdestroyeds"));
  }
  
  @Override
  protected void give(Profile profile) {
    this.reward.give(profile);
  }
  
  @Override
  protected boolean check(Profile profile) {
    return profile.getStats("aCoreBedWars", this.stats) >= this.reach;
  }
  
  public ItemStack getIcon(Profile profile) {
    long current = profile.getStats("aCoreBedWars", this.stats);
    if (current > this.reach) {
      current = this.reach;
    }
    
    return BukkitUtils.deserializeItemStack(
        this.icon.replace("%material%", current == this.reach ? "ENCHANTED_BOOK" : "BOOK").replace("%name%", (current == this.reach ? "&a" : "&c") + this.getName())
            .replace("%current%", StringUtils.formatNumber(current)).replace("%reach%", StringUtils.formatNumber(this.reach))
            .replace("%progress%", (current == this.reach ? "&a" : current > this.reach / 2 ? "&7" : "&c") + current + "/" + this.reach));
  }
  
  interface BedWarsReward {
    void give(Profile profile);
  }
  
  static class CoinsReward implements BedWarsReward {
    
    private final double amount;
    
    public CoinsReward(double amount) {
      this.amount = amount;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getDataContainer("aCoreBedWars", "coins").addDouble(this.amount);
    }
  }
  
  static class TitleReward implements BedWarsReward {
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