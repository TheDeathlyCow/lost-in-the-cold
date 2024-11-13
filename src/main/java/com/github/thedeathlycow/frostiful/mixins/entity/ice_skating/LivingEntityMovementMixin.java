package com.github.thedeathlycow.frostiful.mixins.entity.ice_skating;

import com.github.thedeathlycow.frostiful.entity.IceSkater;
import com.github.thedeathlycow.frostiful.entity.component.LivingEntityComponents;
import com.github.thedeathlycow.frostiful.entity.damage.FDamageSources;
import com.github.thedeathlycow.frostiful.registry.FComponents;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import com.github.thedeathlycow.frostiful.registry.tag.FItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
@Debug(export = true)
public abstract class LivingEntityMovementMixin extends Entity implements IceSkater {


    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow
    protected abstract float getVelocityMultiplier();

    public LivingEntityMovementMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private static final int FROSTIFUL_IS_SKATING_INDEX = 0;

    @Unique
    private static final int FROSTIFUL_IS_GLIDING_INDEX = 1;

    @Unique
    private boolean frostiful$wasSlowed = false;

    @Unique
    private boolean frostiful$getSkateFlag(int index) {
        byte flags = FComponents.ENTITY_COMPONENTS.get(this).getSkateFlags();
        return (flags & 1 << index) != 0;
    }

    @Unique
    private void frostiful$setSkateFlag(int index, boolean value) {
        LivingEntityComponents component = FComponents.ENTITY_COMPONENTS.get(this);
        byte data = component.getSkateFlags();
        if (value) {
            component.setSkateFlags((byte) (data | 1 << index));
        } else {
            component.setSkateFlags((byte) (data & ~(1 << index)));
        }
    }

    @Override
    @Unique
    public boolean frostiful$isIceSkating() {
        return frostiful$getSkateFlag(FROSTIFUL_IS_SKATING_INDEX);
    }

    @Override
    @Unique
    public void frostiful$setSkating(boolean value) {
        this.frostiful$setSkateFlag(FROSTIFUL_IS_SKATING_INDEX, value);
    }

    @Override
    @Unique
    public boolean frostiful$isWearingSkates() {
        return this.getEquippedStack(EquipmentSlot.FEET).isIn(FItemTags.ICE_SKATES);
    }

    @Override
    @Unique
    public boolean frostiful$isGliding() {
        return frostiful$getSkateFlag(FROSTIFUL_IS_GLIDING_INDEX);
    }

    @Inject(
            method = "tickMovement",
            at = @At("TAIL")
    )
    private void updateIsIceSkating(CallbackInfo ci) {

        World world = this.getWorld();
        Profiler profiler = world.getProfiler();
        profiler.push("frostiful.ice_skate_tick");

        BlockState velocityAffectingBlock = world.getBlockState(this.getVelocityAffectingPos());

        this.frostiful$setSkating(
                velocityAffectingBlock.isIn(BlockTags.ICE)
                        && IceSkater.frostiful$isInSkatingPose(this)
                        && this.frostiful$isWearingSkates()
        );

        this.updateSlowness(velocityAffectingBlock);

        if (this.frostiful$isIceSkating() && IceSkater.frostiful$isMoving(this)) {
            this.spawnSprintingParticles();
            if (this.isSneaking()) {
                this.applyStopEffects(velocityAffectingBlock);
            }
        }

        profiler.pop();
    }

    @ModifyVariable(
            method = "travel",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/LivingEntity;isOnGround()Z"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/block/Block;getSlipperiness()F"
                    )
            )
    )
    private float getSlipperinessForIceSkates(float slipperiness) {
        if (this.frostiful$isIceSkating()) {
            slipperiness = IceSkater.frostiful$getSlipperinessForEntity(this);
        }
        return slipperiness;
    }

    @Inject(
            method = "applyMovementInput",
            at = @At("HEAD")
    )
    private void updateGliding(Vec3d movementInput, float slipperiness, CallbackInfoReturnable<Vec3d> cir) {
        this.frostiful$setSkateFlag(FROSTIFUL_IS_GLIDING_INDEX, movementInput.horizontalLengthSquared() < 1e-3);
    }

    @Inject(
            method = "pushAwayFrom",
            at = @At("HEAD")
    )
    private void damageOnLandingUponEntity(Entity entity, CallbackInfo ci) {

        if (!this.getEquippedStack(EquipmentSlot.FEET).isIn(FItemTags.ICE_SKATES)) {
            return;
        }

        if (entity instanceof LivingEntity target) {
            double attackerHeight = this.getPos().y;
            double targetEyeHeight = target.getEyePos().y;
            if (attackerHeight > targetEyeHeight) {
                FDamageSources damageSources = FDamageSources.getDamageSources(this.getWorld());
                target.damage(damageSources.frostiful$iceSkate(this), 1.0f);
            }
        }
    }

    private void updateSlowness(BlockState velocityAffectingBlock) {

        boolean shouldBeSlowed = this.isOnGround()
                && this.frostiful$isWearingSkates()
                && !velocityAffectingBlock.isIn(BlockTags.ICE);

        if (shouldBeSlowed != frostiful$wasSlowed) {
            IceSkater.frostiful$updateSkateWalkPenalityModifier((LivingEntity) (Object) this, shouldBeSlowed);
        }

        frostiful$wasSlowed = shouldBeSlowed;
    }

    private void applyStopEffects(BlockState velocityAffectingBlock) {
        float pitch = this.random.nextFloat() * 0.75f + 0.5f;
        this.playSound(FSoundEvents.ENTITY_GENERIC_ICE_SKATE_STOP, 1.0f, pitch);

        World world = this.getWorld();

        if (!world.isClient) {
            return;
        }
        ParticleEffect iceParticles = new BlockStateParticleEffect(ParticleTypes.BLOCK, velocityAffectingBlock);

        Vec3d velocity = this.getVelocity();
        Vec3d pos = this.getPos();

        for (int i = 0; i < 25; i++) {
            world.addParticle(
                    iceParticles,
                    pos.x + random.nextFloat() - 0.5f,
                    pos.y + random.nextFloat() - 0.5f,
                    pos.z + random.nextFloat() - 0.5f,
                    velocity.x,
                    velocity.y,
                    velocity.z
            );
        }
    }
}
