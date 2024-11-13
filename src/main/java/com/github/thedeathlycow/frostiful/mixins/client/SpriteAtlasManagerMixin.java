package com.github.thedeathlycow.frostiful.mixins.client;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.client.registry.FTexturedRenderLayers;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.Map;

@Mixin(SpriteAtlasManager.class)
public class SpriteAtlasManagerMixin {

    @ModifyVariable(
            method = "<init>",
            argsOnly = true,
            at = @At("HEAD")
    )
    private static Map<Identifier, Identifier> setAtlases(Map<Identifier, Identifier> loaders) {
        Frostiful.LOGGER.debug("Registered Frostiful atlas loaders");
        Map<Identifier, Identifier> mutableLoaders = new HashMap<>();
        mutableLoaders.putAll(FTexturedRenderLayers.LAYERS_TO_LOADERS);
        mutableLoaders.putAll(loaders);
        return mutableLoaders;
    }

}
