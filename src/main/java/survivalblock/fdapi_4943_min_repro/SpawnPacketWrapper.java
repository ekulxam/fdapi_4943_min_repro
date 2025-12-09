package survivalblock.fdapi_4943_min_repro;

import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.Identifier;
import survivalblock.fdapi_4943_min_repro.mixin.EntitySpawnS2CPacketAccessor;

import static survivalblock.fdapi_4943_min_repro.FDAPI4943MinReproduction.MOD_ID;

public record SpawnPacketWrapper(int age, EntitySpawnS2CPacket delegate) implements CustomPayload {
    public static final Id<SpawnPacketWrapper> ID = new Id<>(Identifier.of(MOD_ID, "spawn_packet"));
    public static final PacketCodec<RegistryByteBuf, SpawnPacketWrapper> PACKET_CODEC = CustomPayload.codecOf(SpawnPacketWrapper::encode, SpawnPacketWrapper::decode);

    public void encode(RegistryByteBuf buf) {
        buf.writeInt(this.age);
        ((EntitySpawnS2CPacketAccessor) delegate).fdapi_4943_min_repro$invokeWrite(buf);
    }

    public static SpawnPacketWrapper decode(RegistryByteBuf buf) {
        return new SpawnPacketWrapper(
                buf.readInt(),
                EntitySpawnS2CPacketAccessor.fdapi_4943_min_repro$invokeInit(buf)
        );
    }

    public void apply(Entity funny) {
        funny.age = this.age;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
