package com.github.thedeathlycow.frostiful.entity.frostologer;

import com.github.thedeathlycow.frostiful.registry.FItems;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.util.Hand;

class FrostWandCastGoal extends ProjectileAttackGoal {

    private final FrostologerEntity frostologerEntity;

    public FrostWandCastGoal(FrostologerEntity frostologerEntity, RangedAttackMob mob, double mobSpeed, int intervalTicks, float maxShootRange) {
        super(mob, mobSpeed, intervalTicks, maxShootRange);
        this.frostologerEntity = frostologerEntity;
    }

    @Override
    public boolean canStart() {
        return super.canStart()
                && frostologerEntity.hasTarget()
                && !frostologerEntity.isTargetRooted()
                && frostologerEntity.getMainHandStack().isOf(FItems.FROST_WAND);
    }

    @Override
    public void start() {
        super.start();
        frostologerEntity.setAttacking(true);
        frostologerEntity.setCurrentHand(Hand.MAIN_HAND);
        this.startUsingFrostWand();
    }

    @Override
    public void stop() {
        super.stop();
        frostologerEntity.setAttacking(false);
        frostologerEntity.clearActiveItem();
        this.stopUsingFrostWand();
        if (frostologerEntity.isTargetRooted()) {
            frostologerEntity.thermoo$addTemperature(-500);
        }
    }

    private void startUsingFrostWand() {
        frostologerEntity.playSound(
                FSoundEvents.ITEM_FROST_WAND_PREPARE_CAST,
                1.0f, 1.0f
        );
        frostologerEntity.getDataTracker().set(FrostologerEntity.IS_USING_FROST_WAND, true);
    }

    private void stopUsingFrostWand() {
        frostologerEntity.getDataTracker().set(FrostologerEntity.IS_USING_FROST_WAND, false);
    }
}