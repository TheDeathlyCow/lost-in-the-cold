package com.github.thedeathlycow.frostiful.entity.frostologer;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.registry.FItems;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.util.Hand;

class FrostWandCastGoal extends ProjectileAttackGoal {

    private final FrostologerEntity frostologerEntity;

    public FrostWandCastGoal(FrostologerEntity frostologer, double mobSpeed, int intervalTicks, float maxShootRange) {
        super(frostologer, mobSpeed, intervalTicks, maxShootRange);
        this.frostologerEntity = frostologer;
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
            int cooling = -Frostiful.getConfig().combatConfig.getFrostologerCoolingFromFrostWandHit();
            frostologerEntity.thermoo$addTemperature(cooling);
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