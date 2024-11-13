package com.github.thedeathlycow.frostiful.client.model;

import com.github.thedeathlycow.frostiful.client.anim.BiterAnimations;
import com.github.thedeathlycow.frostiful.entity.BiterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BiterEntityModel extends SinglePartEntityModel<BiterEntity> {


    private final ModelPart modelPart;

    private final ModelPart head;
    private final ModelPart mouthTop;
    private final ModelPart mouthBottom;
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public BiterEntityModel(ModelPart modelPart) {
        this.modelPart = modelPart;

        ModelPart root = modelPart.getChild("root");

        this.head = root.getChild("head");
        ModelPart mouth = this.head.getChild("mouth");

        this.mouthTop = mouth.getChild("top");
        this.mouthBottom = mouth.getChild("bottom");

        this.leftArm = root.getChild("leftArm");
        this.rightArm = root.getChild("rightArm");

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData head = root.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 21.0F, 0.0F));

        ModelPartData mouth = head.addChild("mouth", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bottom = mouth.addChild("bottom", ModelPartBuilder.create().uv(54, 28).cuboid(-6.0F, 0.0F, 0.0F, 12.0F, 4.0F, 0.0F, new Dilation(0.0F))
                .uv(50, 0).cuboid(-6.0F, 0.0F, -12.0F, 12.0F, 4.0F, 0.0F, new Dilation(0.0F))
                .uv(40, 20).cuboid(6.0F, 0.0F, -12.0F, 0.0F, 4.0F, 12.0F, new Dilation(0.0F))
                .uv(30, 16).cuboid(-6.0F, 0.0F, -12.0F, 0.0F, 4.0F, 12.0F, new Dilation(0.0F))
                .uv(0, 12).cuboid(-6.0F, 4.0F, -12.0F, 12.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -18.0F, 6.0F));

        ModelPartData top = mouth.addChild("top", ModelPartBuilder.create().uv(54, 44).cuboid(-6.0F, -8.0F, 0.0F, 12.0F, 8.0F, 0.0F, new Dilation(0.0F))
                .uv(48, 36).cuboid(-6.0F, -8.0F, -12.0F, 12.0F, 8.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 40).cuboid(6.0F, -8.0F, -12.0F, 0.0F, 8.0F, 12.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-6.0F, -8.0F, -12.0F, 0.0F, 8.0F, 12.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.0F, -8.0F, -12.0F, 12.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -18.0F, 6.0F));

        ModelPartData nose = top.addChild("nose", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -22.0F, -8.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 8).cuboid(-1.0F, -16.0F, -8.0F, 2.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 18.0F, -6.0F));

        ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(0, 24).cuboid(-5.0F, -14.0F, -5.0F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, 0.0F));

        ModelPartData leftArm = root.addChild("leftArm", ModelPartBuilder.create().uv(36, 0).cuboid(-2.0F, 3.0F, -3.0F, 4.0F, 22.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, -1.0F, 0.0F));

        ModelPartData rightArm = root.addChild("rightArm", ModelPartBuilder.create().uv(36, 0).mirrored().cuboid(-2.0F, 3.0F, -3.0F, 4.0F, 22.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-8.0F, -1.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public ModelPart getPart() {
        return this.modelPart;
    }

    @Override
    public void setAngles(
            BiterEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.rightArm.pitch = -1.5F * MathHelper.wrap(limbAngle, 10.0F) * limbDistance;
        this.leftArm.pitch = 1.5F * MathHelper.wrap(limbAngle, 10.0F) * limbDistance;
        this.rightArm.yaw = 0.0F;
        this.leftArm.yaw = 0.0F;

        this.updateAnimation(entity.bitingAnimation, BiterAnimations.BITE, animationProgress);
    }
}
