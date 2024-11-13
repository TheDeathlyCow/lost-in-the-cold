package com.github.thedeathlycow.frostiful.client.render.entity;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.client.model.BiterEntityModel;
import com.github.thedeathlycow.frostiful.client.registry.FEntityModelLayers;
import com.github.thedeathlycow.frostiful.entity.BiterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BiterEntityRenderer extends MobEntityRenderer<BiterEntity, BiterEntityModel> {

    public static final Identifier TEXTURE = Frostiful.id("textures/entity/biter.png");

    public BiterEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BiterEntityModel(context.getPart(FEntityModelLayers.BITER)), 0.5F);
    }

    @Override
    public Identifier getTexture(BiterEntity entity) {
        return TEXTURE;
    }

}
