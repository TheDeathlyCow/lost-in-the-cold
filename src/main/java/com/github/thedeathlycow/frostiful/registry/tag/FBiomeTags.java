package com.github.thedeathlycow.frostiful.registry.tag;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public class FBiomeTags {
    public static final TagKey<Biome> FREEZING_WIND_NEVER_SPAWNS = FBiomeTags.register("freezing_wind_never_spawns");
    public static final TagKey<Biome> FREEZING_WIND_ALWAYS_SPAWNS = FBiomeTags.register("freezing_wind_always_spawns");
    public static final TagKey<Biome> FREEZING_WIND_SPAWNS_IN_STORMS = FBiomeTags.register("freezing_wind_spawns_in_storms");
    public static final TagKey<Biome> DRY_BIOMES = FBiomeTags.register("dry_biomes");
    public static final TagKey<Biome> FREEZING_BLACKLIST_BIOMES = FBiomeTags.register("freezing_blacklist_biomes");

    static TagKey<Biome> register(String id) {
        return TagKey.of(RegistryKeys.BIOME, Frostiful.id(id));
    }

    private FBiomeTags() {
    }

}
