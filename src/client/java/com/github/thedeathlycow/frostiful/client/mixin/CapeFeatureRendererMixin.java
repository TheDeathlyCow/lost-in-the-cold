package com.github.thedeathlycow.frostiful.client.mixin;

import com.github.thedeathlycow.frostiful.item.cloak.AbstractFrostologyCloakItem;
import com.github.thedeathlycow.frostiful.registry.tag.FItemTags;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {

    @Unique
    @Nullable
    private AbstractClientPlayerEntity scorchful$renderedPlayer = null;

    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V",
            at = @At("HEAD")
    )
    private void capturePlayer(
            MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider,
            int i,
            AbstractClientPlayerEntity player,
            float f, float g, float h, float j, float k, float l,
            CallbackInfo ci
    ) {
        this.scorchful$renderedPlayer = player;
    }

    @WrapOperation(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/SkinTextures;capeTexture()Lnet/minecraft/util/Identifier;"
            )
    )
    private Identifier getFrostologyCloakTexture(SkinTextures instance, Operation<Identifier> original) {

        boolean renderFrostologyCloak = this.scorchful$renderedPlayer != null
                && AbstractFrostologyCloakItem.isWearing(this.scorchful$renderedPlayer, stack -> stack.isIn(FItemTags.FROSTOLOGY_CLOAKS));

        if (renderFrostologyCloak) {
            return AbstractFrostologyCloakItem.MODEL_TEXTURE_ID;
        } else {
            return original.call(instance);
        }
    }
}
