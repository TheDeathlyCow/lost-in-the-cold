package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.registry.tag.FHasFeatureTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class FPlacedFeatures {

    public static final RegistryKey<PlacedFeature> SUN_LICHEN_COVERED_ROCK = of("sun_lichen_covered_rock");
    public static final RegistryKey<PlacedFeature> ICICLE_CLUSTER = of("icicle_cluster");
    public static final RegistryKey<PlacedFeature> BRITTLE_ICE = of("brittle_ice");

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful placed features");

        BiomeModification modification = BiomeModifications.create(Frostiful.id("vegetation"));

        modification.add(
                ModificationPhase.ADDITIONS,
                BiomeSelectors.tag(FHasFeatureTags.SUN_LICHEN_COVERED_ROCK),
                (biomeSelectionContext, biomeModificationContext) -> {
                    biomeModificationContext.getGenerationSettings().addFeature(
                            GenerationStep.Feature.VEGETAL_DECORATION,
                            FPlacedFeatures.SUN_LICHEN_COVERED_ROCK
                    );
                }
        );

        modification.add(
                ModificationPhase.ADDITIONS,
                BiomeSelectors.tag(FHasFeatureTags.ICICLE_CLUSTER),
                (biomeSelectionContext, biomeModificationContext) -> {
                    biomeModificationContext.getGenerationSettings().addFeature(
                            GenerationStep.Feature.UNDERGROUND_DECORATION,
                            FPlacedFeatures.ICICLE_CLUSTER
                    );
                }
        );

        modification.add(
                ModificationPhase.ADDITIONS,
                BiomeSelectors.tag(FHasFeatureTags.BRITTLE_ICE),
                (biomeSelectionContext, biomeModificationContext) -> {
                    biomeModificationContext.getGenerationSettings().addFeature(
                            GenerationStep.Feature.TOP_LAYER_MODIFICATION,
                            FPlacedFeatures.BRITTLE_ICE
                    );
                }
        );
    }

    private static RegistryKey<PlacedFeature> of(String id) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Frostiful.id(id));
    }

    private FPlacedFeatures() {

    }
}
