package survivalblock.fdapi_4933_min_repro;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.Identifier;

import static survivalblock.fdapi_4933_min_repro.FDAPI4943MinReproduction.MOD_ID;

public class FDAPI4943MinReproClient implements ClientModInitializer {
    public static final EntityModelLayer FUNNY_LAYER = new EntityModelLayer(Identifier.of(MOD_ID, "funny"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(FDAPI4943MinReproduction.FUNNY, FunnyEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(FUNNY_LAYER, FunnyEntityModel::getTexturedModelData);

        ClientPlayNetworking.registerGlobalReceiver(FunnyEntity.SpawnPacket.ID, (spawnPacket, context) -> {
            MinecraftClient client = context.client();
            client.execute(() -> {
                if (client.player == null) {
                    return;
                }
                if (!(client.player.getWorld() instanceof ClientWorld clientWorld)) {
                    return;
                }
                EntitySpawnS2CPacket delegate = spawnPacket.delegate();
                NetworkThreadUtils.forceMainThread(delegate, client.getNetworkHandler(), client);
                EntityType<?> entityType = delegate.getEntityType();
                Entity entity = entityType.create(clientWorld);
                if (entity == null) {
                    return;
                }
                entity.onSpawnPacket(delegate);
                entity.setVelocity(delegate.getVelocityX(), delegate.getVelocityY(), delegate.getVelocityZ());
                if (entity instanceof TameableEntity funny) {
                    spawnPacket.apply(funny);
                }
                clientWorld.addEntity(entity);
            });
        });
    }

    public static class FunnyEntityRenderer extends LivingEntityRenderer<FunnyEntity, FunnyEntityModel> {

        public FunnyEntityRenderer(EntityRendererFactory.Context ctx) {
            super(ctx, new FunnyEntityModel(ctx.getPart(FUNNY_LAYER)), 1);
        }

        @Override
        public Identifier getTexture(FunnyEntity entity) {
            return Identifier.of("nothingtoseeheremovealong");
        }
    }

    public static class FunnyEntityModel extends EntityModel<FunnyEntity> {
        protected final ModelPart cube;

        public FunnyEntityModel(ModelPart root) {
            this.cube = root.getChild("cube");
        }

        @Override
        public void setAngles(FunnyEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
            this.cube.render(matrices, vertices, light, overlay, color);
        }

        public static TexturedModelData getTexturedModelData() {
            ModelData modelData = new ModelData();
            ModelPartData modelPartData = modelData.getRoot();
            modelPartData.addChild("cube", ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F), ModelTransform.pivot(0F, 0F, 0F));
            return TexturedModelData.of(modelData, 64, 64);
        }
    }
}
