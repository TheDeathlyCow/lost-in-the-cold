package com.github.thedeathlycow.frostiful.client;

import com.github.thedeathlycow.frostiful.client.render.FrostWandItemRenderer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public class FrostifulModelLoadingPlugin implements ModelLoadingPlugin {

    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.addModels(FrostWandItemRenderer.INVENTORY_MODEL_ID);
    }

}
