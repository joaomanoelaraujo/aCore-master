package me.joaomanoel.d4rkk.dev.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("all")
public class Wrapper {

    private Object packet = Acessor.createPacket();

    public Wrapper(String name, int param, List<String> members) {
        if (param != 3 && param != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }

        setupDefaults(name, param);
        setupMembers(members);
    }

    public Wrapper(String name, String prefix, String suffix, int param, Collection<?> players) {
        setupDefaults(name, param);
        if (param == 0 || param == 2) {
            try {
                Acessor.DISPLAY_NAME.set(packet, name);
                Acessor.PREFIX.set(packet, prefix);
                Acessor.SUFFIX.set(packet, suffix);
                Acessor.PACK_OPTION.set(packet, 1);
                if (param == 0) {
                    ((Collection) Acessor.MEMBERS.get(packet)).addAll(players);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Wrapper(String name, String fakenick, String prefix, String suffix, int param, Collection<?> players) {
        setupDefaults(name, param);
        if (param == 0 || param == 2) {
            try {
                Acessor.DISPLAY_NAME.set(packet, fakenick);
                Acessor.PREFIX.set(packet, prefix);
                Acessor.SUFFIX.set(packet, suffix);
                Acessor.PACK_OPTION.set(packet, 1);
                if (param == 0) {
                    ((Collection) Acessor.MEMBERS.get(packet)).addAll(players);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupMembers(Collection<?> players) {
        try {
            players = players == null || players.isEmpty() ? new ArrayList<>() : players;
            ((Collection) Acessor.MEMBERS.get(packet)).addAll(players);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupDefaults(String name, int param) {
        try {
            Acessor.TEAM_NAME.set(packet, name);
            Acessor.PARAM_INT.set(packet, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send() {
        Acessor.sendPacket(Bukkit.getOnlinePlayers(), packet);
    }

    public void send(Player player) {
        Acessor.sendPacket(player, packet);
    }
}