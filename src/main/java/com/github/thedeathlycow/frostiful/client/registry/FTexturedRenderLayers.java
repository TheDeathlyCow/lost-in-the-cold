package com.github.thedeathlycow.frostiful.client.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.HashMap;
import java.util.Map;

public class FTexturedRenderLayers {

    public static final Identifier ARMOR_TRIMS_ATLAS_TEXTURE = Frostiful.id("textures/atlas/custom_armor_trims.png");
    public static final RenderLayer ARMOR_TRIMS_RENDER_LAYER = RenderLayer.getArmorCutoutNoCull(ARMOR_TRIMS_ATLAS_TEXTURE);
    public static final Map<Identifier, Identifier> LAYERS_TO_LOADERS = Util.make(
            () -> {
                Map<Identifier, Identifier> map = new HashMap<>();
                map.put(ARMOR_TRIMS_ATLAS_TEXTURE, Frostiful.id("custom_armor_trims"));
                return map;
            }
    );

    private FTexturedRenderLayers() {

    }
}
