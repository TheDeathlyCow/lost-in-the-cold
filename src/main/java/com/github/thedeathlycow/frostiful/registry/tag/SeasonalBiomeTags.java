package com.github.thedeathlycow.frostiful.registry.tag;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public record SeasonalBiomeTags(
        TagKey<Biome> freezing,
        TagKey<Biome> cold,
        TagKey<Biome> cool,
        TagKey<Biome> normal
) {

    private static final SeasonalBiomeTags SPRING_TAGS = new SeasonalBiomeTags(
            FBiomeTags.register("temperature/spring/freezing"),
            FBiomeTags.register("temperature/spring/cold"),
            FBiomeTags.register("temperature/spring/cool"),
            FBiomeTags.register("temperature/spring/normal")
    );
    private static final SeasonalBiomeTags SUMMER_TAGS = new SeasonalBiomeTags(
            FBiomeTags.register("temperature/summer/freezing"),
            FBiomeTags.register("temperature/summer/cold"),
            FBiomeTags.register("temperature/summer/cool"),
            FBiomeTags.register("temperature/summer/normal")
    );
    private static final SeasonalBiomeTags AUTUMN_TAGS = new SeasonalBiomeTags(
            FBiomeTags.register("temperature/autumn/freezing"),
            FBiomeTags.register("temperature/autumn/cold"),
            FBiomeTags.register("temperature/autumn/cool"),
            FBiomeTags.register("temperature/autumn/normal")
    );
    private static final SeasonalBiomeTags WINTER_TAGS = new SeasonalBiomeTags(
            FBiomeTags.register("temperature/winter/freezing"),
            FBiomeTags.register("temperature/winter/cold"),
            FBiomeTags.register("temperature/winter/cool"),
            FBiomeTags.register("temperature/winter/normal")
    );

    /**
     * Returns the biome tags for each season. If no season is provided, then returns the tags for spring
     *
     * @param season The season
     * @return The tags of that season, or spring if no season is given
     */
    public static SeasonalBiomeTags forSeason(ThermooSeason season) {
        return switch (season) {
            case SUMMER -> SUMMER_TAGS;
            case WINTER -> WINTER_TAGS;
            case AUTUMN -> AUTUMN_TAGS;
            case null, default -> SPRING_TAGS;
        };
    }

}
