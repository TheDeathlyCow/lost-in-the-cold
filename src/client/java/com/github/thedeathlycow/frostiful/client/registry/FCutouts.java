package com.github.thedeathlycow.frostiful.client.registry;

import com.github.thedeathlycow.frostiful.registry.FBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class FCutouts {

    public static void initialize() {
        BlockRenderLayerMap.INSTANCE.putBlock(FBlocks.ICICLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FBlocks.COLD_SUN_LICHEN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FBlocks.COOL_SUN_LICHEN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FBlocks.WARM_SUN_LICHEN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FBlocks.HOT_SUN_LICHEN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FBlocks.FROZEN_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FBlocks.FROZEN_WALL_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FBlocks.ICE_PANE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(FBlocks.ICY_TRIAL_SPAWNER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FBlocks.ICY_VAULT, RenderLayer.getCutout());
    }

    private FCutouts() {

    }
}
