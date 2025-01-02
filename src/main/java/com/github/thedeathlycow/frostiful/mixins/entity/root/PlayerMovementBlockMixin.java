package com.github.thedeathlycow.frostiful.mixins.entity.root;

import com.github.thedeathlycow.frostiful.entity.component.FrostWandRootComponent;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This Mixin is necessary since the targeted injection method is overridden by players without a super call
 */
@Mixin(PlayerEntity.class)
public class PlayerMovementBlockMixin {

    @Inject(
            method = "adjustMovementForSneaking",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blockMovementForRootedEntities(Vec3d movement, MovementType type, CallbackInfoReturnable<Vec3d> cir) {
        PlayerEntity instance = (PlayerEntity) (Object) this;

        Vec3d adjustedMovement = FrostWandRootComponent.adjustMovementForRoot(type, movement, instance);
        if (adjustedMovement != null) {
            cir.setReturnValue(adjustedMovement);
        }
    }

}
