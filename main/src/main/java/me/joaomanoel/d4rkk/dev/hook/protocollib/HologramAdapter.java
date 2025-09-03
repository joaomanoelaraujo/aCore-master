package me.joaomanoel.d4rkk.dev.hook.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import me.joaomanoel.d4rkk.dev.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HologramAdapter extends PacketAdapter {

    public HologramAdapter() {
        super(params().plugin(Core.getInstance()).types(PacketType.Play.Server.ENTITY_METADATA));
    }

    @Override
    public void onPacketSending(PacketEvent evt) {
        PacketContainer packet = evt.getPacket();
        List<WrappedDataValue> dataValues = packet.getDataValueCollectionModifier().read(0);
        if (dataValues.stream().noneMatch(wrappedDataValue -> wrappedDataValue.getIndex() == 2)) {
            return;
        }

        List<WrappedDataValue> newValues = new ArrayList<>();
        for (WrappedDataValue value : dataValues) {
            if (value.getIndex() == 2 && value.getValue() instanceof Optional) {
                ((Optional<WrappedChatComponent>) value.getValue()).ifPresent(finalname -> newValues.add(new WrappedDataValue(
                        value.getIndex(),
                        value.getSerializer(),
                        Optional.of(WrappedChatComponent.fromJson(finalname.getJson().replace("{PLAYER}", evt.getPlayer().getName())).getHandle())
                )));
            } else {
                newValues.add(value);
            }
        }

        PacketContainer clone = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        clone.getIntegers().write(0, packet.getIntegers().read(0));
        clone.getDataValueCollectionModifier().write(0, newValues);
        evt.setPacket(clone);
    }

}
