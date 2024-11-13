package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.server.world.gen.feature.IcicleFeature;
import com.github.thedeathlycow.frostiful.server.world.gen.feature.coveredrock.CoveredRockFeature;
import com.github.thedeathlycow.frostiful.server.world.gen.feature.coveredrock.CoveredRockFeatureConfig;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.Feature;

public class FFeatures {

    public static final Feature<CoveredRockFeatureConfig> COVERED_ROCK = new CoveredRockFeature(CoveredRockFeatureConfig.CODEC);
    public static final Feature<IcicleFeature.IcicleFeatureConfig> ICICLE_PATCH = new IcicleFeature(IcicleFeature.IcicleFeatureConfig.CODEC);

    private static void register(String name, Feature<?> feature) {
        Registry.register(Registries.FEATURE, Frostiful.id(name), feature);
    }

    public static void registerAll() {
        register("covered_rock", COVERED_ROCK);
        register("icicle_patch", ICICLE_PATCH);
    }
}
