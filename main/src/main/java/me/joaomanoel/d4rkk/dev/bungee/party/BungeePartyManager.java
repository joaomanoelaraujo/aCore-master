package me.joaomanoel.d4rkk.dev.bungee.party;

import com.google.common.collect.ImmutableList;
import me.joaomanoel.d4rkk.dev.bungee.Bungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BungeePartyManager {
  
  private static final List<BungeeParty> BUNGEE_PARTIES = new ArrayList<>();
  private static ScheduledTask CLEAN_PARTIES;
  
  public static BungeeParty createParty(ProxiedPlayer leader) {
    BungeeParty bp = new BungeeParty(leader.getName(), BungeePartySizer.getPartySize(leader));
    BUNGEE_PARTIES.add(bp);
    if (CLEAN_PARTIES == null) {
      CLEAN_PARTIES = ProxyServer.getInstance().getScheduler().schedule(Bungee.getInstance(),
          () -> ImmutableList.copyOf(BUNGEE_PARTIES).forEach(BungeeParty::update), 0L, 2L, TimeUnit.SECONDS);
    }
    
    return bp;
  }
  
  public static BungeeParty getLeaderParty(String player) {
    return BUNGEE_PARTIES.stream().filter(bp -> bp.isLeader(player)).findAny().orElse(null);
  }
  
  public static BungeeParty getMemberParty(String player) {
    return BUNGEE_PARTIES.stream().filter(bp -> bp.isMember(player)).findAny().orElse(null);
  }
  
  public static List<BungeeParty> listParties() {
    return BUNGEE_PARTIES;
  }
}
