package com.github.thedeathlycow.frostiful.entity;

import com.github.thedeathlycow.frostiful.registry.FEntityTypes;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class FrostSpellEntity extends SpellEntity {

    private static final double EFFECT_CLOUD_SIZE = 4.0;

    public FrostSpellEntity(World world, LivingEntity owner, Vec3d velocity) {
        super(FEntityTypes.FROST_SPELL, world, owner, velocity);
    }

    public FrostSpellEntity(World world, LivingEntity owner, Vec3d velocity, double maxDistance) {
        super(FEntityTypes.FROST_SPELL, world, owner, velocity, maxDistance);
    }

    public FrostSpellEntity(EntityType<? extends SpellEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void applyEffectCloud() {
        World world = getWorld();
        if (this.isRemoved() || world.isClient) {
            return;
        }

        Box box = this.getBoundingBox().expand(EFFECT_CLOUD_SIZE, EFFECT_CLOUD_SIZE, EFFECT_CLOUD_SIZE);
        List<LivingEntity> targets = world.getNonSpectatingEntities(LivingEntity.class, box);
        for (var target : targets) {
            Entity owner = this.getOwner();
            if (owner == null || !target.getUuid().equals(owner.getUuid())) {
                this.applySingleTargetEffect(target);
            }
        }
        world.playSound(
                null,
                this.getX(), this.getY(), this.getZ(),
                SoundEvents.ENTITY_GENERIC_EXPLODE,
                SoundCategory.AMBIENT,
                2.0f, 1.0f
        );

        ServerWorld serverWorld = (ServerWorld) world;

        serverWorld.spawnParticles(
                ParticleTypes.EXPLOSION,
                this.getX(), this.getY(), this.getZ(),
                10,
                2.0, 2.0, 2.0,
                0.3
        );

        this.discard();
    }

    @Override
    protected void applySingleTargetEffect(Entity target) {
        World world = target.getWorld();
        if (!world.isClient && target instanceof RootedEntity rootedEntity) {
            if (rootedEntity.frostiful$root(this.getOwner())) {
                world.playSound(
                        null,
                        target.getX(), target.getY(), target.getZ(),
                        FSoundEvents.ENTITY_FROST_SPELL_FREEZE, SoundCategory.AMBIENT,
                        1.0f, 1.0f
                );
            }
        }
    }
}
