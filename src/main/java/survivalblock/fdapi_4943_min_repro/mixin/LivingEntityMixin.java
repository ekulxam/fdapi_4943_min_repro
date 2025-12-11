package survivalblock.fdapi_4943_min_repro.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.fdapi_4943_min_repro.FDAPI4943MinReproduction;

@SuppressWarnings("UnstableApiUsage")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
	private void maybeCrashPlease(CallbackInfo info) {
        if (!this.getWorld().isClient() && this.age % 20 == 0) {
            this.setAttached(FDAPI4943MinReproduction.DATA, this.random.nextBetween(0, 99));
        }
	}
}