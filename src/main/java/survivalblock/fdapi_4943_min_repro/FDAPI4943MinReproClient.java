package survivalblock.fdapi_4943_min_repro;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

public class FDAPI4943MinReproClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SpawnPacketWrapper.ID, SpawnPacketWrapperReceiver.INSTANCE);
    }

    public static class SpawnPacketWrapperReceiver implements ClientPlayNetworking.PlayPayloadHandler<SpawnPacketWrapper> {
        public static final SpawnPacketWrapperReceiver INSTANCE = new SpawnPacketWrapperReceiver();

        protected SpawnPacketWrapperReceiver() {
        }

        @Override
        public void receive(SpawnPacketWrapper spawnPacketWrapper, ClientPlayNetworking.Context context) {
            MinecraftClient client = context.client();
            //client.execute(() -> {
            if (client.player == null) {
                return;
            }
            if (!(client.player.getWorld() instanceof ClientWorld clientWorld)) {
                return;
            }
            EntitySpawnS2CPacket delegate = spawnPacketWrapper.delegate();
            NetworkThreadUtils.forceMainThread(delegate, client.getNetworkHandler(), client);
            EntityType<?> entityType = delegate.getEntityType();
            Entity entity = entityType.create(clientWorld);
            if (entity == null) {
                return;
            }
            entity.onSpawnPacket(delegate);
            entity.setVelocity(delegate.getVelocityX(), delegate.getVelocityY(), delegate.getVelocityZ());
            if (entity instanceof LivingEntity living) {
                spawnPacketWrapper.apply(living);
            }
            clientWorld.addEntity(entity);
            //});
        }
    }
}
