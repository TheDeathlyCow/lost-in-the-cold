package com.github.thedeathlycow.frostiful.client.gui;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.FrostifulConfig;
import com.github.thedeathlycow.frostiful.registry.FItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public final class FrostOverlayRenderer {

    private static final Identifier POWDER_SNOW_OUTLINE = Identifier.ofVanilla("textures/misc/powder_snow_outline.png");

    private FrostOverlayRenderer() {
    }

    /**
     * Renders the frost overlay in this frame for the main client player
     *
     * @param player         The main client player to render the frost overlay for
     * @param renderCallback A callback that renders the frost overlay texture
     */
    public static void renderFrostOverlay(
            DrawContext context,
            ClientPlayerEntity player,
            OverlayRenderCallback renderCallback
    ) {
        float freezeScale = player.thermoo$getTemperatureScale();
        if (freezeScale > 0) {
            return;
        }
        freezeScale = -freezeScale;


        FrostifulConfig config = Frostiful.getConfig();

        // disable frost overlay when wearing frostology cloak
        boolean isOverlayDisabled = config.clientConfig.isDisableFrostOverlayWhenWearingFrostologyCloak()
                && player.getEquippedStack(EquipmentSlot.CHEST).isOf(FItems.FROSTOLOGY_CLOAK);

        if (isOverlayDisabled) {
            return;
        }

        float renderThreshold = config.clientConfig.getFrostOverlayStart();

        if (freezeScale >= renderThreshold) {
            // scale opacity to temp scale
            float opacity = renderThreshold >= 1.0f
                    ? 0.0f
                    : (freezeScale - renderThreshold) / (1.0f - renderThreshold);
            renderCallback.renderOverlay(context, POWDER_SNOW_OUTLINE, opacity);
        }
    }
}
