package me.joaomanoel.d4rkk.dev.achievements.types;

import me.joaomanoel.d4rkk.dev.Achievements;
import me.joaomanoel.d4rkk.dev.achievements.Achievement;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.titles.Title;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

public class TheBridgeAchievement extends Achievement {
  
  protected TheBridgeReward reward;
  protected String icon;
  protected String[] stats;
  protected int reach;
  
  public TheBridgeAchievement(TheBridgeReward reward, String id, String name, String desc, int reach, String... stats) {
    super("tb-" + id, name);
    this.reward = reward;
    this.icon = "%material% : 1 : name>%name% : desc>" + desc + "\n \n&fProgress: %progress%";
    this.stats = stats;
    this.reach = reach;
  }
  
  public static void setupAchievements() {
    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(100), "1k1",
                    Achievements.thebridge$solo_kill$name,
                    Achievements.thebridge$solo_kill$desc,
                    50, "1v1kills"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(500), "1k2",
                    Achievements.thebridge$solo_master_kill$name,
                    Achievements.thebridge$solo_master_kill$desc,
                    250, "1v1kills"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(250), "1w1",
                    Achievements.thebridge$solo_victorious$name,
                    Achievements.thebridge$solo_victorious$desc,
                    50, "1v1wins"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(1000), "1w2",
                    Achievements.thebridge$solo_master_victorious$name,
                    Achievements.thebridge$solo_master_victorious$desc,
                    200, "1v1wins"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(250), "1p1",
                    Achievements.thebridge$solo_pontuador$name,
                    Achievements.thebridge$solo_pontuador$desc,
                    250, "1v1points"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(1000), "1p2",
                    Achievements.thebridge$solo_master_pontuador$name,
                    Achievements.thebridge$solo_master_pontuador$desc,
                    1000, "1v1points"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(250), "1g1",
                    Achievements.thebridge$solo_persistent$name,
                    Achievements.thebridge$solo_persistent$desc,
                    250, "1v1games"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(100), "2k1",
                    Achievements.thebridge$duo_kill$name,
                    Achievements.thebridge$duo_kill$desc,
                    50, "2v2kills"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(500), "2k2",
                    Achievements.thebridge$duo_master_kill$name,
                    Achievements.thebridge$duo_master_kill$desc,
                    250, "2v2kills"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(250), "2w1",
                    Achievements.thebridge$duo_victorious$name,
                    Achievements.thebridge$duo_victorious$desc,
                    50, "2v2wins"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(1000), "2w2",
                    Achievements.thebridge$duo_master_victorious$name,
                    Achievements.thebridge$duo_master_victorious$desc,
                    200, "2v2wins"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(250), "2p1",
                    Achievements.thebridge$duo_pontuador$name,
                    Achievements.thebridge$duo_pontuador$desc,
                    250, "2v2points"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(1000), "2p2",
                    Achievements.thebridge$duo_master_pontuador$name,
                    Achievements.thebridge$duo_master_pontuador$desc,
                    1000, "2v2points"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new CoinsReward(250), "2g1",
                    Achievements.thebridge$duo_persistent$name,
                    Achievements.thebridge$duo_persistent$desc,
                    250, "2v2games"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new TitleReward("tbk"), "tk",
                    Achievements.thebridge$title$kill$name,
                    Achievements.thebridge$title$kill$desc,
                    500, "1v1kills", "2v2kills"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new TitleReward("tbw"), "tw",
                    Achievements.thebridge$title$win$name,
                    Achievements.thebridge$title$win$desc,
                    400, "1v1wins", "2v2wins"));

    Achievement.addAchievement(
            new TheBridgeAchievement(new TitleReward("tbp"), "tp",
                    Achievements.thebridge$title$pontuador$name,
                    Achievements.thebridge$title$pontuador$desc,
                    2000, "1v1points", "2v2points"));
  }
  
  @Override
  protected void give(Profile profile) {
    this.reward.give(profile);
  }
  
  @Override
  protected boolean check(Profile profile) {
    return profile.getStats("aCoreTheBridge", this.stats) >= this.reach;
  }
  
  public ItemStack getIcon(Profile profile) {
    long current = profile.getStats("aCoreTheBridge", this.stats);
    if (current > this.reach) {
      current = this.reach;
    }
    
    return BukkitUtils.deserializeItemStack(
        this.icon.replace("%material%", current == this.reach ? "ENCHANTED_BOOK" : "BOOK").replace("%name%", (current == this.reach ? "&a" : "&c") + this.getName())
            .replace("%current%", StringUtils.formatNumber(current)).replace("%reach%", StringUtils.formatNumber(this.reach))
            .replace("%progress%", (current == this.reach ? "&a" : current > this.reach / 2 ? "&7" : "&c") + current + "/" + this.reach));
  }
  
  interface TheBridgeReward {
    void give(Profile profile);
  }
  
  static class CoinsReward implements TheBridgeReward {
    private final double amount;
    
    public CoinsReward(double amount) {
      this.amount = amount;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getDataContainer("aCoreTheBridge", "coins").addDouble(this.amount);
    }
  }
  
  static class TitleReward implements TheBridgeReward {
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
