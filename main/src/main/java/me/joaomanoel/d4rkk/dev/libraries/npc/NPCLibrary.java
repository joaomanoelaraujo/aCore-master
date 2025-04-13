package me.joaomanoel.d4rkk.dev.libraries.npc;

import me.joaomanoel.d4rkk.dev.nms.NMSManager;
import me.joaomanoel.d4rkk.dev.nms.npc.NpcEntity;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCLibrary {

    private static final List<NpcEntity> NPCS = new ArrayList<>();

    public static NpcEntity createNPC(Location location, String name) {
        NpcEntity npc = NMSManager.createNPC(location, name, "", "");
        NPCS.add(npc);
        return npc;
    }

    public static NpcEntity createNPC(Location location, String name, String value, String signature) {
        NpcEntity npc = NMSManager.createNPC(location, name, value, signature);
        NPCS.add(npc);
        return npc;
    }

    public static NpcEntity findByUUID(UUID uuid) {
        return NPCS.stream().filter(npcEntity -> npcEntity.getPlayer().getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    public static void removeNPC(NpcEntity npc) {
        npc.kill();
        NPCS.remove(npc);
    }

    public static List<NpcEntity> listNPCs() {
        return NPCS;
    }
}
