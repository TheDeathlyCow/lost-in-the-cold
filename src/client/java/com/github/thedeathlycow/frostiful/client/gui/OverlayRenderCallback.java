package com.github.thedeathlycow.frostiful.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

@FunctionalInterface
public interface OverlayRenderCallback {

    void renderOverlay(DrawContext context, Identifier texture, float opacity);

}