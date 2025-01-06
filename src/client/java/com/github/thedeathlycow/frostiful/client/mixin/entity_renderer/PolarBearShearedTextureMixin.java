package com.github.thedeathlycow.frostiful.client.mixin.entity_renderer;

import com.github.thedeathlycow.frostiful.client.BrushableTextures;
import com.github.thedeathlycow.frostiful.registry.FComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.PolarBearEntityRenderer;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PolarBearEntityRenderer.class)
@Environment(EnvType.CLIENT)
public class PolarBearShearedTextureMixin {
    @Inject(
            method = "getTexture(Lnet/minecraft/entity/passive/PolarBearEntity;)Lnet/minecraft/util/Identifier;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getShearedTexture(PolarBearEntity polarBear, CallbackInfoReturnable<Identifier> cir) {
        if (FComponents.BRUSHABLE_COMPONENT.get(polarBear).wasBrushed()) {
            cir.setReturnValue(BrushableTextures.POLAR_BEAR);
        }
    }
}
