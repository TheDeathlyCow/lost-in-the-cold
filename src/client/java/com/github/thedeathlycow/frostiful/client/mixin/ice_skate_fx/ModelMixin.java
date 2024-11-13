package com.github.thedeathlycow.frostiful.client.mixin.ice_skate_fx;

import com.github.thedeathlycow.frostiful.entity.IceSkater;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
@Debug(export = true)
public class ModelMixin<T extends LivingEntity> {

    @Shadow
    @Final
    public ModelPart leftLeg;
    @Shadow
    @Final
    public ModelPart rightLeg;

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Unique
    private boolean frostiful$freezeLegs = false;

    @Unique
    private final float[] frostiful$LimbPitches = new float[4];

    @Unique
    private static final float FROSTIFUL_PITCH_REDUCTION = 0.75f;

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At("HEAD")
    )
    private void collectSkaterData(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (livingEntity instanceof IceSkater iceSkater) {

            boolean wasLegsFrozen = frostiful$freezeLegs;
            frostiful$freezeLegs = iceSkater.frostiful$isIceSkating()
                    && iceSkater.frostiful$isGliding()
                    && IceSkater.frostiful$isMoving(livingEntity);

            if (!wasLegsFrozen && frostiful$freezeLegs) {
                frostiful$LimbPitches[0] = FROSTIFUL_PITCH_REDUCTION * this.leftLeg.pitch;
                frostiful$LimbPitches[1] = FROSTIFUL_PITCH_REDUCTION * this.rightLeg.pitch;
                frostiful$LimbPitches[2] = FROSTIFUL_PITCH_REDUCTION * this.leftArm.pitch;
                frostiful$LimbPitches[3] = FROSTIFUL_PITCH_REDUCTION * this.rightArm.pitch;
            }

        }
    }

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/model/ModelPart;yaw:F",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/util/math/MathHelper;cos(F)F",
                            ordinal = 3
                    )
            )
    )
    private void resetLegPitchIfGliding(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (frostiful$freezeLegs) {
            this.leftLeg.pitch = frostiful$LimbPitches[0];
            this.rightLeg.pitch = frostiful$LimbPitches[1];
            this.leftArm.pitch = frostiful$LimbPitches[2];
            this.rightArm.pitch = frostiful$LimbPitches[3];
        }
    }

}
