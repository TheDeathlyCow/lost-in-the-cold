package com.github.thedeathlycow.frostiful.entity;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.FrostifulConfig;
import com.github.thedeathlycow.frostiful.entity.damage.FDamageTypes;
import com.github.thedeathlycow.frostiful.registry.tag.FDamageTypeTags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface RootedEntity {

    int frostiful$getRootedTicks();

    void frostiful$setRootedTicks(int amount);

    boolean frostiful$canRoot(@Nullable Entity attacker);

    default boolean frostiful$root(@Nullable Entity attacker) {
        if (this.frostiful$canRoot(attacker)) {
            FrostifulConfig config = Frostiful.getConfig();
            this.frostiful$setRootedTicks(config.combatConfig.getFrostWandRootTime());
            return true;
        }
        return false;
    }

    default void frostiful$breakRoot() {
        this.frostiful$setRootedTicks(1);
    }

    default boolean frostiful$isRooted() {
        return this.frostiful$getRootedTicks() > 0;
    }

    @Nullable
    static Vec3d getMovementWhenRooted(MovementType type, Vec3d movement, Entity entity) {

        if (!(entity instanceof RootedEntity rootedEntity && rootedEntity.frostiful$isRooted())) {
            return null;
        }

        return switch (type) {
            case SELF, PLAYER -> Vec3d.ZERO.add(0, movement.y < 0 && !entity.hasNoGravity() ? movement.y : 0, 0);
            default -> null;
        };
    }

    static void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
        boolean breakRoot = !blocked
                && damageTaken > 0f
                && !source.isIn(FDamageTypeTags.DOES_NOT_BREAK_ROOT)
                && entity instanceof RootedEntity rooted
                && rooted.frostiful$isRooted();

        if (breakRoot) {
            RootedEntity.breakRootOnEntity(entity);
        }
    }

    static void breakRootOnEntity(LivingEntity victim) {
        RootedEntity rootedEntity = (RootedEntity) victim;
        World world = victim.getWorld();
        if (!world.isClient && rootedEntity.frostiful$isRooted()) {
            rootedEntity.frostiful$breakRoot();
            spawnShatterParticlesAndSound(victim, (ServerWorld) world);
        }
    }

    static void spawnShatterParticlesAndSound(LivingEntity victim, ServerWorld serverWorld) {
        ParticleEffect shatteredIce = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.BLUE_ICE.getDefaultState());

        serverWorld.spawnParticles(
                shatteredIce,
                victim.getX(), victim.getY(), victim.getZ(),
                500,
                0.5, 1.0, 0.5,
                1.0
        );

        victim.getWorld().playSound(
                null,
                victim.getBlockPos(),
                SoundEvents.BLOCK_GLASS_BREAK,
                SoundCategory.AMBIENT,
                1.0f, 0.75f
        );
    }
}
