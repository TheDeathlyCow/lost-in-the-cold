package com.github.thedeathlycow.frostiful.client.render.feature;

import com.github.thedeathlycow.frostiful.client.model.FrostologerEntityModel;
import com.github.thedeathlycow.frostiful.entity.FrostologerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FrostologerEyesFeatureRenderer<T extends FrostologerEntity, M extends FrostologerEntityModel<T>> extends FeatureRenderer<T, M> {

    private final RenderLayer skin;

    public FrostologerEyesFeatureRenderer(FeatureRendererContext<T, M> context, Identifier id) {
        super(context);
        this.skin = RenderLayer.getEntityTranslucentEmissive(id);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isAtMaxPower()) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.skin);
            this.getContextModel()
                    .render(
                            matrices,
                            vertexConsumer,
                            0x00F000F0,
                            OverlayTexture.DEFAULT_UV
                    );
        }
    }

}
