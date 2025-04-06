package me.joaomanoel.d4rkk.dev.achievements.types;

import me.joaomanoel.d4rkk.dev.Achievements;
import me.joaomanoel.d4rkk.dev.achievements.Achievement;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.titles.Title;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

public class SkyWarsAchievement extends Achievement {
  
  protected SkyWarsReward reward;
  protected String icon;
  protected String[] stats;
  protected int reach;
  
  public SkyWarsAchievement(SkyWarsReward reward, String id, String name, String desc, int reach, String... stats) {
    super("sw-" + id, name);
    this.reward = reward;
    this.icon = "%material% : 1 : name>%name% : desc>" + desc + "\n \n&fProgress: %progress%";
    this.stats = stats;
    this.reach = reach;
  }
  
  public static void setupAchievements() {
    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(100), "1k1",
                    Achievements.skywars$solo_kill$name,
                    Achievements.skywars$solo_kill$desc,
                    50, "insanekills", "normalkills"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(500), "1k2",
                    Achievements.skywars$solo_master_kill$name,
                    Achievements.skywars$solo_master_kill$desc,
                    250, "insanekills", "normalkills"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(250), "1w1",
                    Achievements.skywars$solo_victorious$name,
                    Achievements.skywars$solo_victorious$desc,
                    50, "insanewins", "normalwins"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(1000), "1w2",
                    Achievements.skywars$solo_master_victorious$name,
                    Achievements.skywars$solo_master_victorious$desc,
                    200, "insanewins", "normalwins"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(100), "1p1",
                    Achievements.skywars$solo_assist$name,
                    Achievements.skywars$solo_assist$desc,
                    50, "insaneassists", "normalassists"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(500), "1p2",
                    Achievements.skywars$solo_master_assist$name,
                    Achievements.skywars$solo_master_assist$desc,
                    250, "insaneassists", "normalassists"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(250), "1g1",
                    Achievements.skywars$solo_persistent$name,
                    Achievements.skywars$solo_persistent$desc,
                    250, "insanegames", "normalgames"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(100), "2k1",
                    Achievements.skywars$duo_kill$name,
                    Achievements.skywars$duo_kill$desc,
                    50, "insane2v2kills", "normal2v2kills"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(500), "2k2",
                    Achievements.skywars$duo_master_kill$name,
                    Achievements.skywars$duo_master_kill$desc,
                    250, "insane2v2kills", "normal2v2kills"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(250), "2w1",
                    Achievements.skywars$duo_victorious$name,
                    Achievements.skywars$duo_victorious$desc,
                    50, "insane2v2wins", "normal2v2wins"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(1000), "2w2",
                    Achievements.skywars$duo_master_victorious$name,
                    Achievements.skywars$duo_master_victorious$desc,
                    200, "insane2v2wins", "normal2v2wins"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(100), "2p1",
                    Achievements.skywars$duo_assist$name,
                    Achievements.skywars$duo_assist$desc,
                    50, "insane2v2assists", "normal2v2assists"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(500), "2p2",
                    Achievements.skywars$duo_master_assist$name,
                    Achievements.skywars$duo_master_assist$desc,
                    250, "insane2v2assists", "normal2v2assists"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new CoinsReward(250), "2g1",
                    Achievements.skywars$duo_persistent$name,
                    Achievements.skywars$duo_persistent$desc,
                    250, "insane2v2games", "normal2v2games"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new TitleReward("swk"), "tk",
                    Achievements.skywars$title$kill$name,
                    Achievements.skywars$title$kill$desc,
                    500, "insanekills", "normalkills", "insane2v2kills", "normal2v2kills"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new TitleReward("sww"), "tw",
                    Achievements.skywars$title$win$name,
                    Achievements.skywars$title$win$desc,
                    400, "insanewins","normalwins", "insane2v2wins", "normal2v2wins"));

    Achievement.addAchievement(
            new SkyWarsAchievement(new TitleReward("swa"), "tp",
                    Achievements.skywars$title$assist$name,
                    Achievements.skywars$title$assist$desc,
                    500, "insaneassists", "insaneassists", "insane2v2assists", "normal2v2assists"));
  }
  
  @Override
  protected void give(Profile profile) {
    this.reward.give(profile);
  }
  
  @Override
  protected boolean check(Profile profile) {
    return profile.getStats("aCoreSkyWars", this.stats) >= this.reach;
  }
  
  public ItemStack getIcon(Profile profile) {
    long current = profile.getStats("aCoreSkyWars", this.stats);
    if (current > this.reach) {
      current = this.reach;
    }
    
    return BukkitUtils.deserializeItemStack(
        this.icon.replace("%material%", current == this.reach ? "ENCHANTED_BOOK" : "BOOK").replace("%name%", (current == this.reach ? "&a" : "&c") + this.getName())
            .replace("%current%", StringUtils.formatNumber(current)).replace("%reach%", StringUtils.formatNumber(this.reach))
            .replace("%progress%", (current == this.reach ? "&a" : current > this.reach / 2 ? "&7" : "&c") + current + "/" + this.reach));
  }
  
  interface SkyWarsReward {
    void give(Profile profile);
  }
  
  static class CoinsReward implements SkyWarsReward {
    private final double amount;
    
    public CoinsReward(double amount) {
      this.amount = amount;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getDataContainer("aCoreSkyWars", "coins").addDouble(this.amount);
    }
  }
  
  static class TitleReward implements SkyWarsReward {
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
