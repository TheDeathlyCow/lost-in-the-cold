package com.github.thedeathlycow.frostiful.client.mixin.gui;

import com.github.thedeathlycow.frostiful.client.gui.FrostOverlayRenderer;
import com.github.thedeathlycow.frostiful.client.gui.RootedOverlayRenderer;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);


    @WrapWithCondition(
            method = "renderMiscOverlays",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V",
                    ordinal = 0
            )
    )
    private boolean blockVanillaFrozenOverlayRender(
            InGameHud instance,
            DrawContext context, Identifier texture, float opacity
    ) {
        return false;
    }


    @Inject(
            method = "renderMiscOverlays",
            at = @At("TAIL")
    )
    private void renderRootedOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (this.client.player != null) {
            RootedOverlayRenderer.render(
                    this.client.player,
                    context, tickCounter,
                    this::renderOverlay
            );
            FrostOverlayRenderer.renderFrostOverlay(
                    context,
                    this.client.player,
                    this::renderOverlay
            );
        }
    }

}
