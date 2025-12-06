package me.joaomanoel.d4rkk.dev.velocity.party;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.scheduler.ScheduledTask;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class VelocityPartyManager {
    
    private static final List<VelocityParty> VELOCITY_PARTIES = new ArrayList<>();
    private static ScheduledTask CLEAN_PARTIES;
    
    public static VelocityParty createParty(Player leader) {
        VelocityParty vp = new VelocityParty(leader.getUsername(), VelocityPartySizer.getPartySize(leader));
        VELOCITY_PARTIES.add(vp);
        
        if (CLEAN_PARTIES == null) {
            CLEAN_PARTIES = VelocityPlugin.getInstance().getServer().getScheduler()
                .buildTask(VelocityPlugin.getInstance(), () -> {
                    ImmutableList.copyOf(VELOCITY_PARTIES).forEach(VelocityParty::update);
                })
                .repeat(Duration.ofSeconds(2))
                .schedule();
        }
        
        return vp;
    }
    
    public static VelocityParty getLeaderParty(String player) {
        return VELOCITY_PARTIES.stream()
            .filter(vp -> vp.isLeader(player))
            .findAny()
            .orElse(null);
    }
    
    public static VelocityParty getMemberParty(String player) {
        return VELOCITY_PARTIES.stream()
            .filter(vp -> vp.isMember(player))
            .findAny()
            .orElse(null);
    }
    
    public static List<VelocityParty> listParties() {
        return VELOCITY_PARTIES;
    }
}