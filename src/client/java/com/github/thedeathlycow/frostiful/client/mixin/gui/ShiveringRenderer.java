package com.github.thedeathlycow.frostiful.client.mixin.gui;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.group.ClientConfigGroup;
import com.github.thedeathlycow.frostiful.survival.SurvivalUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class ShiveringRenderer {

    @Shadow
    @Final
    private Random random;

    @Shadow
    @Final
    private MinecraftClient client;

    @Unique
    private static final float frostiful_baseShakeSift = 0.5f;
    @Unique
    private static final float frostiful_baseIntensity = 0.01f;


    @WrapOperation(
            method = "renderHand",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"
            )
    )
    private void shakeViewWhenShivering(
            GameRenderer instance,
            MatrixStack matrices,
            float tickDelta,
            Operation<Void> original
    ) {
        if (this.client.getCameraEntity() instanceof LivingEntity livingEntity && SurvivalUtils.isShiveringRender(livingEntity)) {
            ClientConfigGroup config = Frostiful.getConfig().clientConfig;
            if (config.isShakeCameraWhenShiveringEnabled()) {

                final float intensity = config.getHandShakeIntensity();

                float shakeX = (this.random.nextFloat() - frostiful_baseShakeSift) * frostiful_baseIntensity;
                float shakeY = (this.random.nextFloat() - frostiful_baseShakeSift) * frostiful_baseIntensity;
                float shakeZ = (this.random.nextFloat() - frostiful_baseShakeSift) * frostiful_baseIntensity;

                matrices.translate(intensity * shakeX, intensity * shakeY, intensity * shakeZ);
            }
        }

        original.call(instance, matrices, tickDelta);
    }


}
