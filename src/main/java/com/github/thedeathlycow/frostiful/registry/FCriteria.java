package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.entity.advancement.FrozenByFrostWandCriterion;
import com.github.thedeathlycow.frostiful.entity.advancement.SunLichenDischargeCriterion;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class FCriteria {
    public static final SunLichenDischargeCriterion SUN_LICHEN_DISCHARGE = register(
            "sun_lichen_discharge",
            new SunLichenDischargeCriterion()
    );

    public static final FrozenByFrostWandCriterion FROZEN_BY_FROST_WAND = register(
            "frozen_by_frost_wand",
            new FrozenByFrostWandCriterion()
    );

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful advancement criteria");
    }

    public static <T extends Criterion<?>> T register(String name, T criterion) {
        return Registry.register(Registries.CRITERION, Frostiful.id(name), criterion);
    }

    private FCriteria() {

    }
}