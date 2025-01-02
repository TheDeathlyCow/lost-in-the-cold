package com.github.thedeathlycow.frostiful.mixins.entity.root;

import com.github.thedeathlycow.frostiful.entity.component.FrostWandRootComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMovementBlockMixin {

    @Inject(
            method = "adjustMovementForSneaking",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blockMovementForRootedEntities(Vec3d movement, MovementType type, CallbackInfoReturnable<Vec3d> cir) {
        Entity instance = (Entity) (Object) this;

        Vec3d adjustedMovement = FrostWandRootComponent.adjustMovementForRoot(type, movement, instance);
        if (adjustedMovement != null) {
            cir.setReturnValue(adjustedMovement);
        }
    }

}
