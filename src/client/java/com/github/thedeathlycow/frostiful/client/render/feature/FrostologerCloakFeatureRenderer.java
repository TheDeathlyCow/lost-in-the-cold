package com.github.thedeathlycow.frostiful.client.render.feature;

import com.github.thedeathlycow.frostiful.client.model.FrostologerEntityModel;
import com.github.thedeathlycow.frostiful.entity.FrostologerEntity;
import com.github.thedeathlycow.frostiful.item.cloak.AbstractFrostologyCloakItem;
import com.github.thedeathlycow.frostiful.registry.FItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class FrostologerCloakFeatureRenderer extends FeatureRenderer<FrostologerEntity, FrostologerEntityModel<FrostologerEntity>> {
    public FrostologerCloakFeatureRenderer(
            FeatureRendererContext<FrostologerEntity, FrostologerEntityModel<FrostologerEntity>> featureRendererContext
    ) {
        super(featureRendererContext);
    }

    public void render(
            MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider,
            int light,
            FrostologerEntity frostologer,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        ItemStack cloak = frostologer.getEquippedStack(EquipmentSlot.CHEST);

        if (!cloak.isOf(FItems.FROSTOLOGY_CLOAK)) {
            return;
        }

        matrixStack.push();
        matrixStack.translate(0.0, 0.0, 3f / 16f);
        double capeX = MathHelper.lerp(tickDelta, frostologer.prevCapeX, frostologer.capeX) - MathHelper.lerp(tickDelta, frostologer.prevX, frostologer.getX());
        double capeY = MathHelper.lerp(tickDelta, frostologer.prevCapeY, frostologer.capeY) - MathHelper.lerp(tickDelta, frostologer.prevY, frostologer.getY());
        double capeZ = MathHelper.lerp(tickDelta, frostologer.prevCapeZ, frostologer.capeZ) - MathHelper.lerp(tickDelta, frostologer.prevZ, frostologer.getZ());
        float yawDelta = frostologer.prevBodyYaw + (frostologer.bodyYaw - frostologer.prevBodyYaw);
        double rotZ = MathHelper.sin(yawDelta * MathHelper.PI / 180);
        double rotX = -MathHelper.cos(yawDelta * MathHelper.PI / 180);
        float q = MathHelper.clamp((float) capeY * 10.0F, -6.0F, 32.0F);
        float r = MathHelper.clamp((float) (capeX * rotZ + capeZ * rotX) * 100.0F, 0.0F, 150.0F);
        float s = MathHelper.clamp((float) (capeX * rotX - capeZ * rotZ) * 100.0F, -20.0F, 20.0F);

        if (r < 0.0F) {
            r = 0.0F;
        }

        float t = MathHelper.lerp(tickDelta, frostologer.prevStrideDistance, frostologer.strideDistance);
        q += MathHelper.sin(MathHelper.lerp(tickDelta, frostologer.prevHorizontalSpeed, frostologer.horizontalSpeed) * 6.0F) * 32.0F * t;
        if (frostologer.isInSneakingPose()) {
            q += 25.0F;
        }

        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + r / 2.0F + q));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(s / 2.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - s / 2.0F));
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(AbstractFrostologyCloakItem.MODEL_TEXTURE_ID));
        this.getContextModel().forceRenderCloak(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }
}
