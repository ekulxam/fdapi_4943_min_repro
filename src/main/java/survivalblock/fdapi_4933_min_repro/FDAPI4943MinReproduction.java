package survivalblock.fdapi_4933_min_repro;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("UnstableApiUsage")
public class FDAPI4943MinReproduction implements ModInitializer {
	public static final String MOD_ID = "fdapi_4933_min_repro";
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final AttachmentType<Integer> DATA = AttachmentRegistry.<Integer>builder()
            .initializer(() -> 0)
            .persistent(Codec.INT)
            .syncWith(PacketCodecs.VAR_INT, AttachmentSyncPredicate.all())
            .buildAndRegister(Identifier.of(MOD_ID, "data"));

    public static final EntityType<FunnyEntity> FUNNY = EntityType.Builder.create(FunnyEntity::new, SpawnGroup.CREATURE)
            .dimensions(1, 1).build();

	@Override
	public void onInitialize() {
        Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "funny"), FUNNY);

        PayloadTypeRegistry.playS2C().register(FunnyEntity.SpawnPacket.ID, FunnyEntity.SpawnPacket.PACKET_CODEC);

        FabricDefaultAttributeRegistry.register(FUNNY, TameableShoulderEntity.createMobAttributes());
	}
}