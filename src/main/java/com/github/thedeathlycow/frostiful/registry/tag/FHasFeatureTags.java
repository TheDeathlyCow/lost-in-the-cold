package com.github.thedeathlycow.frostiful.registry.tag;

import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public class FHasFeatureTags {

    public static final TagKey<Biome> SUN_LICHEN_COVERED_ROCK = FBiomeTags.register("has_feature/sun_lichen_covered_rock");
    public static final TagKey<Biome> ICICLE_CLUSTER = FBiomeTags.register("has_feature/icicle_cluster");
    public static final TagKey<Biome> BRITTLE_ICE = FBiomeTags.register("has_feature/brittle_ice");

    private FHasFeatureTags() {

    }

}
