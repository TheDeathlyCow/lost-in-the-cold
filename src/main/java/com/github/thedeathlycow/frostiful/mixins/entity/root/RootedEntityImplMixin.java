package com.github.thedeathlycow.frostiful.mixins.entity.root;

import com.github.thedeathlycow.frostiful.entity.RootedEntity;
import com.github.thedeathlycow.frostiful.entity.damage.FDamageTypes;
import com.github.thedeathlycow.frostiful.registry.FComponents;
import com.github.thedeathlycow.frostiful.registry.tag.FEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class RootedEntityImplMixin extends Entity implements RootedEntity {

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);


    public RootedEntityImplMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public int frostiful$getRootedTicks() {
        return FComponents.ENTITY_COMPONENTS.get(this).getRootedTicks();
    }

    @Override
    public void frostiful$setRootedTicks(int amount) {
        FComponents.ENTITY_COMPONENTS.get(this).setRootedTicks(amount);
    }

    @Override
    public boolean frostiful$canRoot(@Nullable Entity attacker) {
        if (this.frostiful$isRooted()) {
            return false;
        }

        final LivingEntity instance = (LivingEntity) (Object) this;

        if (instance.getType().isIn(FEntityTypeTags.ROOT_IMMUNE)) {
            return false;
        }

        if (attacker != null && this.isTeammate(attacker)) {
            return false;
        }

        return instance.thermoo$canFreeze();
    }

    @Inject(
            method = "tickMovement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/profiler/Profiler;pop()V",
                    ordinal = 1
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V"
                    )
            )
    )
    private void tickRoot(CallbackInfo ci) {
        World world = getWorld();
        Profiler profiler = world.getProfiler();
        profiler.push("frostiful.rooted_tick");
        if (this.isSpectator()) {
            this.frostiful$setRootedTicks(0);
        } else if (this.frostiful$isRooted()) {
            // remove tick
            int ticksLeft = this.frostiful$getRootedTicks();
            this.frostiful$setRootedTicks(--ticksLeft);

            if (!world.isClient && this.isOnFire()) {
                this.frostiful$breakRoot();
                this.extinguish();
                ServerWorld serverWorld = (ServerWorld) world;
                serverWorld.spawnParticles(
                        ParticleTypes.POOF,
                        this.getX(), this.getY(), this.getZ(),
                        100,
                        0.5, 0.5, 0.5,
                        0.1f
                );
                this.playExtinguishSound();
            }
        }

        profiler.pop();
    }

}


