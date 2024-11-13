package com.github.thedeathlycow.frostiful.client.model;

import com.github.thedeathlycow.frostiful.entity.FrostologerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;

@Environment(EnvType.CLIENT)
public class FrostologerEntityModel<F extends FrostologerEntity> extends IllagerEntityModel<F> {


    protected final ModelPart head;
    protected final ModelPart rightArm;
    protected final ModelPart leftArm;

    private float rgColourMul = 0f;

    private final ModelPart cloak;

    public FrostologerEntityModel(ModelPart root) {
        super(root);
        ModelPart hat = this.getHead().getChild("hat");
        hat.visible = true;
        this.head = this.getHead();
        this.rightArm = this.getPart().getChild("right_arm");
        this.leftArm = this.getPart().getChild("left_arm");

        this.cloak = root.getChild("cloak");
        this.cloak.visible = false;
    }


    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color, boolean applyColdOverlay) {
        if (this.rgColourMul < 1 && applyColdOverlay) {
            float alpha = ColorHelper.Argb.getAlpha(color) / 255f;
            float red = ColorHelper.Argb.getRed(color) / 255f * this.rgColourMul;
            float green = ColorHelper.Argb.getGreen(color) / 255f * this.rgColourMul;
            float blue = ColorHelper.Argb.getBlue(color) / 255f;
            color = ColorHelper.Argb.fromFloats(alpha, red, green, blue);
        }

        super.render(matrices, vertices, light, overlay, color);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.render(matrices, vertices, light, overlay, color, true);
    }

    public void forceRenderCloak(MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
        this.cloak.visible = true;
        this.cloak.render(matrices, vertices, light, overlay);
        this.cloak.visible = false;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        head.addChild("hat", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, new Dilation(0.45F)), ModelTransform.NONE);
        head.addChild("nose", ModelPartBuilder.create().uv(24, 0).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), ModelTransform.pivot(0.0F, -2.0F, 0.0F));

        root.addChild("body", ModelPartBuilder.create().uv(16, 20).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F).uv(0, 38).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData arms = root.addChild("arms", ModelPartBuilder.create().uv(44, 22).cuboid(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F).uv(40, 38).cuboid(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F), ModelTransform.of(0.0F, 3.0F, -1.0F, -0.75F, 0.0F, 0.0F));
        arms.addChild("left_shoulder", ModelPartBuilder.create().uv(44, 22).mirrored().cuboid(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F), ModelTransform.NONE);

        root.addChild("right_leg", ModelPartBuilder.create().uv(0, 22).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), ModelTransform.pivot(-2.0F, 12.0F, 0.0F));
        root.addChild("left_leg", ModelPartBuilder.create().uv(0, 22).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), ModelTransform.pivot(2.0F, 12.0F, 0.0F));
        root.addChild("right_arm", ModelPartBuilder.create().uv(40, 46).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
        root.addChild("left_arm", ModelPartBuilder.create().uv(40, 46).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        root.addChild("cloak", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, Dilation.NONE, 1.0F, 0.5F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(
            F frostologer,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        super.setAngles(frostologer, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        this.rgColourMul = (0.625f * (frostologer.thermoo$getTemperatureScale() + 1f)) + 0.5f;

        if (frostologer.isUsingFrostWand()) {
            if (frostologer.isLeftHanded()) {
                this.leftArm.yaw = 0.1f + this.head.yaw;
                this.leftArm.pitch = -1.57f + this.head.pitch;
            } else {
                this.rightArm.yaw = -0.1f + this.head.yaw;
                this.rightArm.pitch = -1.57f + this.head.pitch;
            }
        }
    }
}
