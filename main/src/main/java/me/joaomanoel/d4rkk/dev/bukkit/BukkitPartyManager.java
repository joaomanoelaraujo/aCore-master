package me.joaomanoel.d4rkk.dev.bukkit;

import com.google.common.collect.ImmutableList;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.player.fake.FakeManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class BukkitPartyManager {
  
  private static final List<BukkitParty> BUKKIT_PARTIES = new ArrayList<>();
  private static BukkitTask CLEAN_PARTIES;
  
  public static BukkitParty createParty(Player leader) {
    return createParty(leader.getName(), BukkitPartySizer.getPartySize(leader));
  }
  
  public static BukkitParty createParty(String leader, int size) {
    BukkitParty bp = new BukkitParty(leader, size);
    BUKKIT_PARTIES.add(bp);
    if (CLEAN_PARTIES == null && !FakeManager.isBungeeSide()) {
      CLEAN_PARTIES = new BukkitRunnable() {
        @Override
        public void run() {
          ImmutableList.copyOf(BUKKIT_PARTIES).forEach(BukkitParty::update);
        }
      }.runTaskTimer(Core.getInstance(), 0L, 40L);
    }
    
    return bp;
  }
  
  public static BukkitParty getLeaderParty(String player) {
    return BUKKIT_PARTIES.stream().filter(bp -> bp.isLeader(player)).findAny().orElse(null);
  }
  
  public static BukkitParty getMemberParty(String player) {
    return BUKKIT_PARTIES.stream().filter(bp -> bp.isMember(player)).findAny().orElse(null);
  }
  
  public static List<BukkitParty> listParties() {
    return BUKKIT_PARTIES;
  }

  public static List<String> listAllMembers() {
    List<String> allMembers = new ArrayList<>();
    for (BukkitParty party : BUKKIT_PARTIES) {
      allMembers.addAll(party.getMembers());
    }
    return allMembers;
  }
}
