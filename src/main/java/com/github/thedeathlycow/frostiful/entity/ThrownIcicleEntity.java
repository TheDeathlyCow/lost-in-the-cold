package com.github.thedeathlycow.frostiful.entity;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.group.IcicleConfigGroup;
import com.github.thedeathlycow.frostiful.registry.FEntityTypes;
import com.github.thedeathlycow.frostiful.registry.FItems;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class ThrownIcicleEntity extends PersistentProjectileEntity {

    public ThrownIcicleEntity(EntityType<? extends ThrownIcicleEntity> entityType, World world) {
        super(entityType, world);
    }

    public ThrownIcicleEntity(World world, double x, double y, double z, ItemStack stack) {
        super(FEntityTypes.THROWN_ICICLE, x, y, z, world, stack, stack);
    }

    public ThrownIcicleEntity(World world, LivingEntity owner, ItemStack stack) {
        super(FEntityTypes.THROWN_ICICLE, owner, world, stack, null);
    }

    @Override
    public void tick() {
        super.tick();
        World world = getWorld();
        if (world.isClient && !this.inGround) {
            world.addParticle(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {

        if (entityHitResult.getEntity().getType() == FEntityTypes.FROSTOLOGER) {
            return;
        }

        IcicleConfigGroup config = Frostiful.getConfig().icicleConfig;

        float damage = entityHitResult.getEntity().getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)
                ? config.getThrownIcicleExtraDamage()
                : config.getThrownIcicleDamage();
        this.setDamage(damage);

        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        IcicleConfigGroup config = Frostiful.getConfig().icicleConfig;
        int freezeAmount = config.getThrownIcicleFreezeAmount();

        target.thermoo$addTemperature(-freezeAmount, HeatingModes.ACTIVE);
    }

    @Override
    protected SoundEvent getHitSound() {
        return FSoundEvents.ENTITY_THROWN_ICICLE_HIT;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(FItems.ICICLE);
    }
}
