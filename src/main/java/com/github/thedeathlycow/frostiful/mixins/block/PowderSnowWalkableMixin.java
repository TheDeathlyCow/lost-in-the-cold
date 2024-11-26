package com.github.thedeathlycow.frostiful.mixins.block;

import com.github.thedeathlycow.frostiful.registry.tag.FItemTags;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowWalkableMixin {
    @Inject(
            method = "canWalkOnPowderSnow",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void checkPowderSnowWalkableItemTag(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity) {
            for (ItemStack stack : livingEntity.getArmorItems()) {
                if (!stack.isEmpty() && stack.isIn(FItemTags.POWDER_SNOW_WALKABLE)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }
}
