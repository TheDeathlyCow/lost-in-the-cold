package com.github.thedeathlycow.frostiful.client.gui;

import com.github.thedeathlycow.frostiful.entity.component.FrostWandRootComponent;
import com.github.thedeathlycow.frostiful.registry.FComponents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class RootedOverlayRenderer {


    private static final Identifier FROSTIFUL_ROOTED_OVERLAY = Identifier.ofVanilla("textures/block/ice.png");

    public static void render(
            LivingEntity entity,
            DrawContext context,
            RenderTickCounter tickCounter,
            OverlayRenderCallback callback
    ) {
        FrostWandRootComponent component = FComponents.FROST_WAND_ROOT_COMPONENT.get(entity);
        if (component.isRooted()) {
            callback.renderOverlay(context, FROSTIFUL_ROOTED_OVERLAY, component.getRootProgress());
        }
    }

    private RootedOverlayRenderer() {
    }

}
