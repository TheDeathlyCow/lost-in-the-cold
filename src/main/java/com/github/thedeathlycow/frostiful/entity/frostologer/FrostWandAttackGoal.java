package com.github.thedeathlycow.frostiful.entity.frostologer;

import net.minecraft.entity.ai.goal.AttackGoal;

class FrostWandAttackGoal extends AttackGoal {
    private final FrostologerEntity frostologerEntity;

    public FrostWandAttackGoal(FrostologerEntity frostologerEntity) {
        super(frostologerEntity);
        this.frostologerEntity = frostologerEntity;
    }

    @Override
    public boolean canStart() {
        return frostologerEntity.isTargetRooted()
                && super.canStart();
    }

}