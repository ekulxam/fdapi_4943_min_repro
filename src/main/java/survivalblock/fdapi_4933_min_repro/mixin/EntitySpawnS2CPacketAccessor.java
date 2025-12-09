package survivalblock.fdapi_4933_min_repro.mixin;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntitySpawnS2CPacket.class)
public interface EntitySpawnS2CPacketAccessor {

    @Invoker("write")
    void yeahyeah$write(RegistryByteBuf buf);

    @Invoker("<init>")
    static EntitySpawnS2CPacket $fromByteBuf(RegistryByteBuf buf) {
        throw new UnsupportedOperationException("mixin boo womp");
    }
}
