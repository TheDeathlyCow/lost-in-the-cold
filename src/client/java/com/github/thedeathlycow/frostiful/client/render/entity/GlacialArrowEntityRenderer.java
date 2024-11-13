package com.github.thedeathlycow.frostiful.client.render.entity;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.entity.GlacialArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GlacialArrowEntityRenderer extends ProjectileEntityRenderer<GlacialArrowEntity> {

    public static final Identifier TEXTURE = Frostiful.id("textures/entity/projectiles/glacial_arrow.png");

    public GlacialArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(GlacialArrowEntity entity) {
        return TEXTURE;
    }
}
