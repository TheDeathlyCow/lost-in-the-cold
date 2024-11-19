package com.github.thedeathlycow.frostiful.entity;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;

import java.util.EnumSet;
import java.util.Set;

public interface IceSkater {

    boolean frostiful$isIceSkating();

    boolean frostiful$isGliding();

    void frostiful$setSkating(boolean value);

    boolean frostiful$isWearingSkates();

    static boolean frostiful$isInSkatingPose(Entity entity) {
        return IceSkaterSettings.VALID_POSES_FOR_SKATING.contains(entity.getPose());
    }

    static void frostiful$updateSkateWalkPenalityModifier(LivingEntity entity, boolean shouldBeSlowed) {
        EntityAttributeInstance movementSpeed = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (movementSpeed == null) {
            return;
        }

        movementSpeed.removeModifier(IceSkaterSettings.SKATE_WALK_PENALITY_ID);
        if (shouldBeSlowed) {
            movementSpeed.addTemporaryModifier(IceSkaterSettings.SKATE_WALK_PENALTY);
        }
    }

    /**
     * Checks if the given entity is travelling at a fast enough speed to be considered moving, for the purpose
     * of ice skating effects like sound and particles. If the entity is effectively standing still (or moving very slowly)
     * these effects would be annoying and so should not be applied.
     *
     * @param entity The entity to check
     * @return Returns true if the speed of the entity is greater than or equal to {@link IceSkaterSettings#MIN_SPEED}
     */
    static boolean frostiful$isMoving(Entity entity) {
        return entity.getVelocity().lengthSquared() >= IceSkaterSettings.MIN_SPEED * IceSkaterSettings.MIN_SPEED;
    }

    /**
     * Gets the block slipperiness value for an entity that is ice skating. Value is used the same as in
     * {@link Block#getSlipperiness()}, but for entities that are skating on ice. Entities may have different slipperiness
     * values depending on what they are doing, such as sneaking or sprinting.
     * <p>
     * Pre-condition: THe given entity is ice skating.
     *
     * @param entity The entity
     * @return Return the slipperiness value for the ice skating entity.
     */
    static float frostiful$getSlipperinessForEntity(Entity entity) {
        float slipperiness;
        if (entity.isSneaking()) {
            slipperiness = IceSkaterSettings.BRAKE_SLIPPERINESS;
        } else if (entity.isSprinting()) {
            slipperiness = IceSkaterSettings.SPRINT_SLIPPERINESS;
        } else {
            slipperiness = IceSkaterSettings.SKATE_SLIPPERINESS;
        }

        return slipperiness;
    }


    class IceSkaterSettings {
        private static final float MIN_SPEED = 0.2f;
        private static final float SKATE_SLIPPERINESS = 1.075f;
        private static final float SPRINT_SLIPPERINESS = 1.078f;
        private static final float BRAKE_SLIPPERINESS = 1.0f;

        private static final Identifier SKATE_WALK_PENALITY_ID = Frostiful.id("gameplay.skate_walk_penalty");

        private static final EntityAttributeModifier SKATE_WALK_PENALTY = new EntityAttributeModifier(
                SKATE_WALK_PENALITY_ID,
                -0.5,
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        private static final Set<EntityPose> VALID_POSES_FOR_SKATING = EnumSet.of(EntityPose.STANDING, EntityPose.CROUCHING);

        private IceSkaterSettings() {
        }
    }
}
