package me.joaomanoel.d4rkk.dev.player.scoreboard;

import me.joaomanoel.d4rkk.dev.player.scoreboard.scroller.ScoreboardScroller;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public abstract class KScoreboard {

  private Player player;
  private Objective objective;
  private Scoreboard scoreboard;
  private ScoreboardScroller scroller;

  private String display;
  private boolean health, healthTab;

  private VirtualTeam[] teams = new VirtualTeam[15];

  public KScoreboard() {}

  public void scroll() {
    if (this.scroller != null) {
      display(this.scroller.next());
    }
  }

  public void update() {}

  public void updateHealth() {
    if (this.healthTab && this.scoreboard != null) {
      Objective tabObjective = this.scoreboard.getObjective("healthPL");
      if (tabObjective != null) {
        for (Player online : Bukkit.getOnlinePlayers()) {
          int level = (int) online.getHealth();
          tabObjective.getScore(online.getName()).setScore(level);
        }
      }
    }
  }

  public KScoreboard add(int line) {
    return add(line, "");
  }

  public KScoreboard add(int line, String name) {
    if (line > 15 || line < 1 || this.teams == null) return this;

    VirtualTeam team = getOrCreate(line);
    team.setValue(name);
    if (this.scoreboard != null) {
      team.update();
    }
    return this;
  }

  public KScoreboard remove(int line) {
    if (line > 15 || line < 1 || this.teams == null) return this;

    VirtualTeam team = this.teams[line - 1];
    if (team != null) {
      team.destroy();
      this.teams[line - 1] = null;
    }
    return this;
  }

  public KScoreboard to(Player player) {
    Player lastPlayer = this.player;
    this.player = player;
    if (this.scoreboard != null) {
      if (lastPlayer != null) {
        lastPlayer.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
      }
      player.setScoreboard(this.scoreboard);
    }
    return this;
  }

  public KScoreboard display(String display) {
    this.display = StringUtils.translateAlternateColorCodes('&', display);
    if (this.objective != null) {
      this.objective.setDisplayName(this.display.substring(0, Math.min(this.display.length(), 32)));
    }
    return this;
  }

  public KScoreboard scroller(ScoreboardScroller ss) {
    this.scroller = ss;
    return this;
  }

  public KScoreboard health() {
    this.health = !this.health;
    if (this.scoreboard != null) {
      if (!this.health) {
        Objective obj = this.scoreboard.getObjective("healthBN");
        if (obj != null) obj.unregister();
      } else {
        Objective healthObj = this.scoreboard.registerNewObjective("healthBN", "health");
        healthObj.setDisplayName("§c❤");
        healthObj.setDisplaySlot(DisplaySlot.BELOW_NAME);
      }
    }
    return this;
  }

  public KScoreboard healthTab() {
    this.healthTab = !this.healthTab;
    if (this.scoreboard != null) {
      if (!this.healthTab) {
        Objective obj = this.scoreboard.getObjective("healthPL");
        if (obj != null) obj.unregister();
      } else {
        Objective tab = this.scoreboard.registerNewObjective("healthPL", "dummy");
        tab.setDisplaySlot(DisplaySlot.PLAYER_LIST);
      }
    }
    return this;
  }

  public KScoreboard build() {
    this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    this.objective = scoreboard.registerNewObjective(getObjectiveName(), "dummy");
    this.objective.setDisplayName(this.display == null ? "" : this.display.substring(0, Math.min(this.display.length(), 32)));
    this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    if (this.player != null) {
      this.player.setScoreboard(this.scoreboard);
    }

    if (this.health) {
      Objective healthObj = this.scoreboard.registerNewObjective("healthBN", "health");
      healthObj.setDisplayName("§c❤");
      healthObj.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }

    if (this.healthTab) {
      Objective tab = this.scoreboard.registerNewObjective("healthPL", "dummy");
      tab.setDisplaySlot(DisplaySlot.PLAYER_LIST);
      // Preenche com health atual
      for (Player online : Bukkit.getOnlinePlayers()) {
        tab.getScore(online.getName()).setScore((int) online.getHealth());
      }
    }

    for (VirtualTeam team : this.teams) {
      if (team != null) {
        team.update();
      }
    }

    return this;
  }

  public void destroy() {
    if (this.objective != null) this.objective.unregister();
    if (this.health) {
      Objective obj = this.scoreboard.getObjective("healthBN");
      if (obj != null) obj.unregister();
    }
    if (this.healthTab) {
      Objective obj = this.scoreboard.getObjective("healthPL");
      if (obj != null) obj.unregister();
    }

    this.objective = null;
    this.scoreboard = null;
    this.teams = null;
    this.player = null;
    this.display = null;
  }

  public VirtualTeam getTeam(int line) {
    if (line > 15 || line < 1) return null;
    return teams[line - 1];
  }

  public VirtualTeam getOrCreate(int line) {
    if (line > 15 || line < 1) return null;
    if (this.teams[line - 1] == null) {
      this.teams[line - 1] = new VirtualTeam(this, "score[" + line + "]", line);
    }

    return this.teams[line - 1];
  }

  public String getObjectiveName() {
    return "kScoreboard";
  }

  public Scoreboard getScoreboard() {
    return this.scoreboard;
  }

  public Objective getObjective() {
    return this.objective;
  }
}
