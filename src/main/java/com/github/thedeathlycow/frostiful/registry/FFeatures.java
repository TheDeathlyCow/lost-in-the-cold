package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.server.world.gen.feature.IcicleFeature;
import com.github.thedeathlycow.frostiful.server.world.gen.feature.coveredrock.CoveredRockFeature;
import com.github.thedeathlycow.frostiful.server.world.gen.feature.coveredrock.CoveredRockFeatureConfig;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class FFeatures {

    public static final Feature<CoveredRockFeatureConfig> COVERED_ROCK = register(
            "covered_rock",
            new CoveredRockFeature(CoveredRockFeatureConfig.CODEC)
    );
    public static final Feature<IcicleFeature.IcicleFeatureConfig> ICICLE_PATCH = register(
            "icicle_patch",
            new IcicleFeature(IcicleFeature.IcicleFeatureConfig.CODEC)
    );

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful features");
    }

    private static <C extends FeatureConfig> Feature<C> register(String name, Feature<C> feature) {
        return Registry.register(Registries.FEATURE, Frostiful.id(name), feature);
    }

    private FFeatures() {

    }
}
