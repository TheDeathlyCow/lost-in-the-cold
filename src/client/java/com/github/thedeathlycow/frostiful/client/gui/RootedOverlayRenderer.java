package com.github.thedeathlycow.frostiful.client.gui;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.FrostifulConfig;
import com.github.thedeathlycow.frostiful.entity.RootedEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class RootedOverlayRenderer {


    private static final Identifier FROSTIFUL_ROOTED_OVERLAY = Identifier.ofVanilla("textures/block/ice.png");

    public static void render(
            RootedEntity rooted,
            DrawContext context,
            RenderTickCounter tickCounter,
            OverlayRenderCallback callback
    ) {
        if (rooted.frostiful$isRooted()) {
            FrostifulConfig config = Frostiful.getConfig();
            float opacity = ((float) rooted.frostiful$getRootedTicks()) / config.combatConfig.getFrostWandRootTime();
            callback.renderOverlay(context, FROSTIFUL_ROOTED_OVERLAY, opacity);
        }
    }

    private RootedOverlayRenderer() {
    }
    
}
