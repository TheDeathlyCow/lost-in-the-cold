package com.github.thedeathlycow.frostiful.client.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.client.model.BiterEntityModel;
import com.github.thedeathlycow.frostiful.client.model.FrostWandItemModel;
import com.github.thedeathlycow.frostiful.client.model.FrostologerEntityModel;
import com.github.thedeathlycow.frostiful.client.model.IceSkateModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;

@Environment(EnvType.CLIENT)
public class FEntityModelLayers {

    public static final EntityModelLayer FROST_WAND = new EntityModelLayer(Frostiful.id("frost_wand"), "main");
    public static final EntityModelLayer FROSTOLOGER = new EntityModelLayer(Frostiful.id("frostologer"), "main");
    public static final EntityModelLayer CHILLAGER = new EntityModelLayer(Frostiful.id("chillager"), "main");

    public static final EntityModelLayer BITER = new EntityModelLayer(Frostiful.id("biter"), "main");

    public static final EntityModelLayer ICE_SKATES = new EntityModelLayer(Frostiful.id("ice_skates"), "main");

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful entity model layers");
        EntityModelLayerRegistry.registerModelLayer(FROST_WAND, FrostWandItemModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(FROSTOLOGER, FrostologerEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CHILLAGER, IllagerEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BITER, BiterEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ICE_SKATES, IceSkateModel::getTexturedModelData);
    }

    private FEntityModelLayers() {

    }
}
