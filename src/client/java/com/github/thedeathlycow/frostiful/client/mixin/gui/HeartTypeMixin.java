package com.github.thedeathlycow.frostiful.client.mixin.gui;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.HeartType.class)
public abstract class HeartTypeMixin {
    @ModifyReturnValue(
            method = "fromPlayerState",
            at = @At("TAIL")
    )
    private static InGameHud.HeartType frostifulIsFrozen(InGameHud.HeartType original, PlayerEntity player) {
        return original == InGameHud.HeartType.NORMAL && player.thermoo$getTemperatureScale() <= -0.99f
                ? InGameHud.HeartType.FROZEN
                : original;
    }
}
