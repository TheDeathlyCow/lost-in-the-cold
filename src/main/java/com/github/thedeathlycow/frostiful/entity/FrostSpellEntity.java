package com.github.thedeathlycow.frostiful.entity;

import com.github.thedeathlycow.frostiful.registry.FComponents;
import com.github.thedeathlycow.frostiful.registry.FCriteria;
import com.github.thedeathlycow.frostiful.registry.FEntityTypes;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FrostSpellEntity extends SpellEntity {

    private static final double EFFECT_CLOUD_SIZE = 3.0;

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
        List<LivingEntity> targetsFrozen = new ArrayList<>();
        for (var target : targets) {
            Entity owner = this.getOwner();
            boolean isTargetable = owner == null || !target.getUuid().equals(owner.getUuid());
            if (isTargetable && this.applySingleTargetEffect(target)) {
                targetsFrozen.add(target);
            }
        }

        if (!targetsFrozen.isEmpty() && this.getOwner() instanceof ServerPlayerEntity serverPlayer) {
            FCriteria.FROZEN_BY_FROST_WAND.trigger(serverPlayer, targetsFrozen);
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

    protected boolean applySingleTargetEffect(Entity target) {
        World world = target.getWorld();
        if (!world.isClient) {
            if (FComponents.FROST_WAND_ROOT_COMPONENT.get(target).tryRootFromFrostWand(this.getOwner())) {
                world.playSound(
                        null,
                        target.getX(), target.getY(), target.getZ(),
                        FSoundEvents.ENTITY_FROST_SPELL_FREEZE, SoundCategory.AMBIENT,
                        1.0f, 1.0f
                );
                return true;
            }
        }
        return false;
    }
}
