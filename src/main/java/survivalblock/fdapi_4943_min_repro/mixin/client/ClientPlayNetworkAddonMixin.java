package survivalblock.fdapi_4943_min_repro.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.impl.networking.client.ClientPlayNetworkAddon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.CustomPayload;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.fdapi_4943_min_repro.FDAPI4943MinReproClient;

@Debug(export = true)
@Mixin(ClientPlayNetworkAddon.class)
public class ClientPlayNetworkAddonMixin {

    @Inject(method = "receive(Lnet/fabricmc/fabric/api/client/networking/v1/ClientPlayNetworking$PlayPayloadHandler;Lnet/minecraft/network/packet/CustomPayload;)V", at = @At("HEAD"), cancellable = true)
    private void noExecute(ClientPlayNetworking.PlayPayloadHandler<?> handler, CustomPayload payload, CallbackInfo ci) {
        if (!FDAPI4943MinReproClient.BYPASS_CUSTOM_PAYLOAD_EXECUTE) {
            return;
        }

        if (!handler.equals(FDAPI4943MinReproClient.SpawnPacketWrapperReceiver.INSTANCE)) {
            return;
        }

        ((ClientPlayNetworking.PlayPayloadHandler) handler).receive(payload, new ClientPlayNetworking.Context() {
            @Override
            public MinecraftClient client() {
                return MinecraftClient.getInstance();
            }

            @Override
            public ClientPlayerEntity player() {
                return this.client().player;
            }

            @Override
            public PacketSender responseSender() {
                return (ClientPlayNetworkAddon) (Object) this;
            }
        });
        ci.cancel();
    }
}
