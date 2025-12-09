package survivalblock.fdapi_4933_min_repro;

import it.unimi.dsi.fastutil.ints.IntArrays;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Mount;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.fdapi_4933_min_repro.mixin.EntitySpawnS2CPacketAccessor;

import java.util.UUID;

import static survivalblock.fdapi_4933_min_repro.FDAPI4943MinReproduction.MOD_ID;

public class FunnyEntity extends TameableShoulderEntity implements Shearable, Mount {

    public FunnyEntity(EntityType<? extends TameableShoulderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }

    @Override
    public void sheared(SoundCategory shearedSoundCategory) {

    }

    @Override
    public boolean canUsePortals(boolean allowVehicles) {
        return false;
    }

    @Override
    public boolean isShearable() {
        return true;
    }

    @Override
    protected boolean isFlappingWings() {
        return false;
    }

    @Override
    public boolean isFallFlying() {
        return false;
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return super.getControllingPassenger();
    }

    @Override
    public boolean canBeLeashed() {
        return true;
    }

    @Override
    public boolean canBeSpectated(ServerPlayerEntity spectator) {
        return false;
    }

    @Override
    public boolean occludeVibrationSignals() {
        return true;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        Packet<ClientPlayPacketListener> packet = super.createSpawnPacket(entityTrackerEntry);
        if (!(packet instanceof EntitySpawnS2CPacket clientbound)) {
            throw new IllegalStateException("oof");
        }
        return (Packet<ClientPlayPacketListener>) (Packet<?>) new CustomPayloadS2CPacket(new SpawnPacket(this, clientbound));
    }

    public record SpawnPacket(UUID myUuid, Float spawnYaw, int[] passengers, int tickSpawned, EntitySpawnS2CPacket delegate) implements CustomPayload {
        public static final Id<SpawnPacket> ID = new Id<>(Identifier.of(MOD_ID, "spawn_packet"));
        public static final PacketCodec<RegistryByteBuf, SpawnPacket> PACKET_CODEC = CustomPayload.codecOf(SpawnPacket::encode, SpawnPacket::decode);
        public SpawnPacket(TameableEntity funny, EntitySpawnS2CPacket packet) {
            this(funny.getUuid(), funny.getYaw(), funny.getPassengerList().stream().map(Entity::getId).mapToInt(value -> value).toArray(), funny.age, packet);
        }

        public void encode(RegistryByteBuf buf) {
            buf.writeUuid(myUuid);
            buf.writeInt(tickSpawned);
            buf.writeIntArray(passengers);
            buf.writeFloat(spawnYaw);
            ((EntitySpawnS2CPacketAccessor) delegate).yeahyeah$write(buf);
        }

        public static SpawnPacket decode(RegistryByteBuf buf) {
            UUID two = buf.readUuid();
            int three = buf.readInt();
            int[] four = buf.readIntArray();
            float five = buf.readFloat();

            return new SpawnPacket(
                    two, five, four, three,
                    EntitySpawnS2CPacketAccessor.$fromByteBuf(buf)
            );
        }

        public void apply(TameableEntity funny) {
            funny.setUuid(this.myUuid);
            funny.removeAllPassengers();
            for (int id : this.passengers) {
                Entity entity = funny.getWorld().getEntityById(id);
                if (entity != null) entity.startRiding(funny);
            }
            funny.age = this.tickSpawned;

            funny.setYaw(this.spawnYaw);
            funny.prevYaw = this.spawnYaw;
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
