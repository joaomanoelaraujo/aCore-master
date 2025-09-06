package me.joaomanoel.d4rkk.dev.hook.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.joaomanoel.d4rkk.dev.Core;

public class EntityAdapter extends PacketAdapter {

    public EntityAdapter() {
        super(params().plugin(Core.getInstance()).types(PacketType.Play.Server.ENTITY_METADATA, PacketType.Play.Server.NAMED_ENTITY_SPAWN));
    }

    @Override
    public void onPacketSending(PacketEvent evt) {
        /*for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getEntityId() == evt.getPacket().getIntegers().read(0)) {
                if (evt.getPacketType() == PacketType.findLegacy(7)) {
                    List<WrappedWatchableObject> watchableObjectList = evt.getPacket().getWatchableCollectionModifier().read(0);
                    for (WrappedWatchableObject metadata : watchableObjectList)
                    {
                        if (metadata.getIndex() == 0) {
                            WrappedWatchableObject watchableObject =  watchableObjectList.get(0);
                            byte b = (byte) watchableObject.getValue();
                            b |= 0b01000000;
                            watchableObject.setValue(b);
                        } else {
                            WrappedDataWatcher watcher = evt.getPacket().getDataWatcherModifier().read(0);
                            if (watcher.hasIndex(0)) {
                                byte b = watcher.getByte(0);
                                b |= 0b01000000;
                                watcher.setObject(0, b);
                            }
                        }
                    }
                }
            }
        }*/
    }

}
