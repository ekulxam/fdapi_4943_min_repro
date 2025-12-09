package survivalblock.fdapi_4943_min_repro.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.fdapi_4943_min_repro.SpawnPacketWrapper;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    public int age;

    @SuppressWarnings("unchecked")
    @ModifyReturnValue(method = "createSpawnPacket", at = @At("RETURN"))
    private Packet<ClientPlayPacketListener> funnyCrash(Packet<ClientPlayPacketListener> original) {
        if (!((Entity) (Object) this instanceof LivingEntity)) {
            return original;
        }
        if (!(original instanceof EntitySpawnS2CPacket clientbound)) {
            throw new IllegalStateException("oof");
        }
        return (Packet<ClientPlayPacketListener>) (Packet<?>) new CustomPayloadS2CPacket(new SpawnPacketWrapper(this.age, clientbound));
    }
}
