package me.joaomanoel.d4rkk.dev.replay;

import com.comphenix.packetwrapper.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import java.util.Collection;

public class WrapperPlayServerScoreboardTeam extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_TEAM;

    public WrapperPlayServerScoreboardTeam() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerScoreboardTeam(PacketContainer packet) {
        super(packet, TYPE);
    }

    public String getName() {
        return handle.getStrings().read(0);
    }

    public void setName(String value) {
        handle.getStrings().write(0, value);
    }

    public int getMode() {
        return handle.getIntegers().read(0);
    }

    public void setMode(int value) {
        handle.getIntegers().write(0, value);
    }

    @SuppressWarnings("unchecked")
    public Collection<String> getPlayers() {
        return handle.getSpecificModifier(Collection.class).read(0);
    }

    public void setPlayers(Collection<String> value) {
        handle.getSpecificModifier(Collection.class).write(0, value);
    }
}
