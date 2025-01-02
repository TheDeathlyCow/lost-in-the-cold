package com.github.thedeathlycow.frostiful.client.render.feature;

import com.github.thedeathlycow.frostiful.client.model.FrostologerEntityModel;
import com.github.thedeathlycow.frostiful.entity.frostologer.FrostologerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FrostologerFrostFeatureRenderer extends FeatureRenderer<FrostologerEntity, FrostologerEntityModel<FrostologerEntity>> {

    public FrostologerFrostFeatureRenderer(
            FeatureRendererContext<FrostologerEntity, FrostologerEntityModel<FrostologerEntity>> context
    ) {
        super(context);
    }

    @Override
    public void render(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            FrostologerEntity frostologer,
            float limbAngle, float limbDistance,
            float tickDelta, float animationProgress,
            float headYaw, float headPitch
    ) {
        if (frostologer.isInvisible()) {
            return;
        }

        FrostLayers layer = FrostLayers.fromFrostologer(frostologer);

        if (layer == FrostLayers.NONE) {
            return;
        }

        Identifier identifier = layer.getTexture();
        FeatureRenderer.renderModel(
                this.getContextModel(),
                identifier,
                matrices,
                vertexConsumers,
                light,
                frostologer,
                -1
        );
    }
}
