package com.github.thedeathlycow.frostiful.survival;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.FrostifulConfig;
import com.github.thedeathlycow.frostiful.registry.FEntityTypes;
import com.github.thedeathlycow.frostiful.registry.FItems;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.ThermooTags;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;

public class ModifyTemperatureController extends EnvironmentControllerDecorator {

    public ModifyTemperatureController(EnvironmentController controller) {
        super(controller);
    }

    @Override
    public double getBaseValueForAttribute(RegistryEntry<EntityAttribute> attribute, LivingEntity entity) {
        double base = controller.getBaseValueForAttribute(attribute, entity);
        if (attribute == ThermooAttributes.MIN_TEMPERATURE && entity.getType() == EntityType.PLAYER) {
            base += 5.0;
        }
        if (attribute == ThermooAttributes.MAX_TEMPERATURE && entity.getType() == FEntityTypes.FROSTOLOGER) {
            base = 0;
        }
        return base;
    }

    @Override
    public int getTemperatureEffectsChange(LivingEntity entity) {
        int warmth = controller.getTemperatureEffectsChange(entity);
        if (entity.thermoo$getTemperature() > 0) {
            return warmth;
        }

        FrostifulConfig config = Frostiful.getConfig();

        boolean applyConduitPowerWarmth = entity.isSubmergedInWater()
                && entity.hasStatusEffect(StatusEffects.CONDUIT_POWER);

        if (applyConduitPowerWarmth) {
            warmth += config.freezingConfig.getConduitWarmthPerTick();
        }
        if (SurvivalUtils.isShivering(entity)) {
            warmth += this.updateShivering(entity, config);
        }

        return warmth;
    }

    private int updateShivering(LivingEntity entity, FrostifulConfig config) {

        boolean benefitsFromCold = entity.getType().isIn(ThermooTags.BENEFITS_FROM_COLD_ENTITY_TYPE)
                || entity.getEquippedStack(EquipmentSlot.CHEST).isOf(FItems.FROSTOLOGY_CLOAK);

        if (benefitsFromCold) {
            return 0;
        }

        int shiverWarmth = config.freezingConfig.getShiverWarmth();
        if (entity instanceof PlayerEntity player) {
            if (player.getHungerManager().getFoodLevel() <= config.freezingConfig.getStopShiverWarmingBelowFoodLevel()) {
                return 0;
            }

            player.addExhaustion(0.04f * shiverWarmth);
        }

        return shiverWarmth;
    }

}
