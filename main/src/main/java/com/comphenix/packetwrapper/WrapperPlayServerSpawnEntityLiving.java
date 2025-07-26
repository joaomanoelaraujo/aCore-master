package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class WrapperPlayServerSpawnEntityLiving
        extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY_LIVING;
    private static PacketConstructor entityConstructor;

    public WrapperPlayServerSpawnEntityLiving() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerSpawnEntityLiving(PacketContainer packet) {
        super(packet, TYPE);
    }

    public WrapperPlayServerSpawnEntityLiving(Entity entity) {
        super(fromEntity(entity), TYPE);
    }

    private static PacketContainer fromEntity(Entity entity) {
        // Garantir que o entityConstructor seja inicializado
        if (entityConstructor == null) {
            // Verifique se a entidade está válida antes de criar o construtor
            if (entity == null) {
                throw new IllegalArgumentException("A entidade não pode ser nula.");
            }
            entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, new Object[]{entity});
        }
        // Garantir que o pacote seja criado corretamente
        return entityConstructor.createPacket(new Object[]{entity});
    }

    public int getEntityID() {
        // Verifique se o índice existe antes de acessar
        if (this.handle.getIntegers().size() > 0) {
            return (Integer) this.handle.getIntegers().read(0);
        }
        return -1; // Retornar um valor padrão ou lançar uma exceção personalizada
    }

    public Entity getEntity(World world) {
        // Verifique se o modificador de entidade tem dados
        if (this.handle.getEntityModifier(world).size() > 0) {
            return (Entity) this.handle.getEntityModifier(world).read(0);
        }
        return null; // Retornar nulo ou lançar exceção personalizada
    }

    public Entity getEntity(PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }

    public UUID getUniqueId() {
        // Verifique se o UUID existe
        if (this.handle.getUUIDs().size() > 0) {
            return (UUID) this.handle.getUUIDs().read(0);
        }
        return null; // Retornar nulo ou lançar exceção personalizada
    }

    public void setUniqueId(UUID value) {
        if (value != null) {
            this.handle.getUUIDs().write(0, value);
        }
    }

    public void setEntityID(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public EntityType getType() {
        String type;

        // Garantir que a conversão seja válida
        int id = (Integer) this.handle.getIntegers().read(1);
        return EntityType.fromId(id);
    }

    public void setType(EntityType value) {

            this.handle.getIntegers().write(1, (int) value.getTypeId());

    }

    public double getX() {
        // Verifique o índice e o valor
        if (this.handle.getDoubles().size() > 0) {
            return (Double) this.handle.getDoubles().read(0);
        }
        return 0.0; // Valor padrão
    }

    public void setX(double value) {
        if (this.handle.getIntegers().size() > 0) {
            this.handle.getIntegers().write(0, (int) (value * 32)); // Multiplicar por 32 para ajustar a precisão
        }
    }

    public void setY(double value) {
        if (this.handle.getIntegers().size() > 1) {
            this.handle.getIntegers().write(1, (int) (value * 32)); // Multiplicar por 32
        }
    }

    public void setZ(double value) {
        if (this.handle.getIntegers().size() > 2) {
            this.handle.getIntegers().write(2, (int) (value * 32)); // Multiplicar por 32
        }
    }

    public double getY() {
        if (this.handle.getDoubles().size() > 1) {
            return (Double) this.handle.getDoubles().read(1);
        }
        return 0.0; // Valor padrão
    }


    public double getZ() {
        if (this.handle.getDoubles().size() > 2) {
            return (Double) this.handle.getDoubles().read(2);
        }
        return 0.0; // Valor padrão
    }

  
    public float getYaw() {
        return (float) ((Byte) this.handle.getBytes().read(0)).byteValue() * 360.0f / 256.0f;
    }

    public void setYaw(float value) {
        this.handle.getBytes().write(0, (byte) (value * 256.0f / 360.0f));
    }

    public float getPitch() {
        return (float) ((Byte) this.handle.getBytes().read(1)).byteValue() * 360.0f / 256.0f;
    }

    public void setPitch(float value) {
        this.handle.getBytes().write(1, (byte) (value * 256.0f / 360.0f));
    }

    public float getHeadPitch() {
        return (float) ((Byte) this.handle.getBytes().read(2)).byteValue() * 360.0f / 256.0f;
    }

    public void setHeadPitch(float value) {
        this.handle.getBytes().write(2, (byte) (value * 256.0f / 360.0f));
    }

    public double getVelocityX() {
        if (this.handle.getIntegers().size() > 2) {
            return (double) ((Integer) this.handle.getIntegers().read(2)).intValue() / 8000.0;
        }
        return 0.0; // Valor padrão
    }

    public void setVelocityX(double value) {
        this.handle.getIntegers().write(2, (int) (value * 8000.0));
    }

    public double getVelocityY() {
        if (this.handle.getIntegers().size() > 3) {
            return (double) ((Integer) this.handle.getIntegers().read(3)).intValue() / 8000.0;
        }
        return 0.0; // Valor padrão
    }

    public void setVelocityY(double value) {
        this.handle.getIntegers().write(3, (int) (value * 8000.0));
    }

    public double getVelocityZ() {
        if (this.handle.getIntegers().size() > 4) {
            return (double) ((Integer) this.handle.getIntegers().read(4)).intValue() / 8000.0;
        }
        return 0.0; // Valor padrão
    }

    public void setVelocityZ(double value) {
        this.handle.getIntegers().write(4, (int) (value * 8000.0));
    }

    public WrappedDataWatcher getMetadata() {
        // Verifique se os metadados estão disponíveis
        if (this.handle.getDataWatcherModifier().size() > 0) {
            return (WrappedDataWatcher) this.handle.getDataWatcherModifier().read(0);
        }
        return null; // Retornar nulo se não houver metadados
    }

    public void setMetadata(WrappedDataWatcher value) {
            this.handle.getDataWatcherModifier().write(0, value);

    }
}
