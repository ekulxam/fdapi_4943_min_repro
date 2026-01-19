package survivalblock.fdapi_4943_min_repro.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.fdapi_4943_min_repro.FDAPI4943MinReproduction;

@SuppressWarnings("UnstableApiUsage")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void maybeCrashPlease(CallbackInfo info) {
        if (!FDAPI4943MinReproduction.DEBUG_PLAYERS) {
            return;
        }
        if (!this.getWorld().isClient()) {
            this.setAttached(FDAPI4943MinReproduction.DATA, this.random.nextBetween(0, 99));
        }
    }
}
