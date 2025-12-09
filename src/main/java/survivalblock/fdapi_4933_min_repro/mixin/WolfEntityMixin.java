package survivalblock.fdapi_4933_min_repro.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import survivalblock.fdapi_4933_min_repro.FunnyEntity;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends TameableEntity {
    protected WolfEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    // yeah yeah mixinoverride
    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        Packet<ClientPlayPacketListener> packet = super.createSpawnPacket(entityTrackerEntry);
        if (!(packet instanceof EntitySpawnS2CPacket clientbound)) {
            throw new IllegalStateException("oof");
        }
        return (Packet<ClientPlayPacketListener>) (Packet<?>) new CustomPayloadS2CPacket(new FunnyEntity.SpawnPacket(this, clientbound));
    }
}
