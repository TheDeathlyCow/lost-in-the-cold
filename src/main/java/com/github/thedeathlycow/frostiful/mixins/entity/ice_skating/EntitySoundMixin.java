package com.github.thedeathlycow.frostiful.mixins.entity.ice_skating;

import com.github.thedeathlycow.frostiful.entity.IceSkater;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntitySoundMixin {

    @Shadow @Final protected Random random;

    @Inject(
            method = "playStepSounds",
            at = @At("HEAD"),
            cancellable = true
    )
    private void playSkateSlideSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        final Entity instance = (Entity) (Object) this;

        if (ci.isCancelled()) {
            return;
        }

        if (instance instanceof IceSkater iceSkater) {

            boolean playGlideSound = iceSkater.frostiful$isIceSkating()
                    && IceSkater.frostiful$isMoving(instance);

            if (playGlideSound) {
                // don't also play the normal step sounds
                ci.cancel();

                float pitch = random.nextFloat() * 0.75f + 0.5f;
                instance.playSound(FSoundEvents.ENTITY_GENERIC_ICE_SKATE_GLIDE, 1.0f, pitch);

                boolean playSkateSound = instance.isSprinting()
                        && random.nextFloat() < 0.1f;

                if (playSkateSound) {
                    pitch = random.nextFloat() * 0.2f + 0.9f;
                    instance.playSound(FSoundEvents.ENTITY_GENERIC_ICE_SKATE_SKATE, 1.0f, pitch);
                }
            }
        }
    }
}
