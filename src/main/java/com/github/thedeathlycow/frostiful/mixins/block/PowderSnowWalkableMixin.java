package com.github.thedeathlycow.frostiful.mixins.block;

import com.github.thedeathlycow.frostiful.registry.tag.FItemTags;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowWalkableMixin {

    @Unique
    private static final EquipmentSlot[] ALLOWED_SLOTS = new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.BODY};

    @ModifyReturnValue(
            method = "canWalkOnPowderSnow",
            at = @At(
                    value = "RETURN",
                    ordinal = 1
            )
    )
    private static boolean ignoreHardCodedLeatherBootsCheck(boolean original) {
        return false;
    }

    @Inject(
            method = "canWalkOnPowderSnow",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void checkPowderSnowWalkableItemTag(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity) {
            for (EquipmentSlot slot : ALLOWED_SLOTS) {
                ItemStack stack = livingEntity.getEquippedStack(slot);
                if (!stack.isEmpty() && stack.isIn(FItemTags.POWDER_SNOW_WALKABLE)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }
}
