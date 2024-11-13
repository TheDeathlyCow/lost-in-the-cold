package com.github.thedeathlycow.frostiful.client.render.feature;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.client.model.IceSkateModel;
import com.github.thedeathlycow.frostiful.registry.tag.FItemTags;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class IceSkateFeatureRenderer<
        T extends LivingEntity,
        M extends BipedEntityModel<T>,
        I extends IceSkateModel<T>
        > extends FeatureRenderer<T, M> {

    private final I model;

    private static final Identifier SKATE_TEXTURE = Frostiful.id("textures/entity/skates.png");

    public IceSkateFeatureRenderer(
            FeatureRendererContext<T, M> context,
            I model
    ) {
        super(context);
        this.model = model;
    }


    @Override
    public void render(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            T entity,
            float limbAngle, float limbDistance,
            float tickDelta, float animationProgress,
            float headYaw, float headPitch
    ) {
        if (entity.getEquippedStack(EquipmentSlot.FEET).isIn(FItemTags.ICE_SKATES)) {
            this.getContextModel().copyBipedStateTo(model);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(SKATE_TEXTURE));
            this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        }
    }
}
