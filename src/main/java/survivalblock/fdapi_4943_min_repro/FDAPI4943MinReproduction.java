package survivalblock.fdapi_4943_min_repro;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("UnstableApiUsage")
public class FDAPI4943MinReproduction implements ModInitializer {
	public static final String MOD_ID = "fdapi_4943_min_repro";
    public static final boolean DEBUG_PLAYERS = true;
    public static final boolean BYPASS_CUSTOM_PAYLOAD_EXECUTE = false;
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final AttachmentType<Integer> DATA = AttachmentRegistry.create(
            Identifier.of(MOD_ID, "data"),
            builder -> builder.initializer(() -> 0)
                    .persistent(Codec.INT)
                    .syncWith(PacketCodecs.VAR_INT, AttachmentSyncPredicate.all())
    );

	@Override
	public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(SpawnPacketWrapper.ID, SpawnPacketWrapper.PACKET_CODEC);
	}
}