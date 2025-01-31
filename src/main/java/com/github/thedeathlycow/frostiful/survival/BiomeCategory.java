package com.github.thedeathlycow.frostiful.survival;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.group.EnvironmentConfigGroup;
import com.github.thedeathlycow.frostiful.registry.tag.FBiomeTags;
import com.github.thedeathlycow.frostiful.registry.tag.SeasonalBiomeTags;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.function.ToIntFunction;

public enum BiomeCategory {

    NORMAL(c -> 0, false),
    COLD_AT_NIGHT(c -> 0, true),
    COLD(EnvironmentConfigGroup::getColdBiomeTemperatureChange, true),
    FREEZING(EnvironmentConfigGroup::getFreezingBiomeTemperatureChange, true);

    private static final float SNOW_TEMPERATURE = 0.15f;

    private static final int NIGHT_TIME_SURFACE_LIGHT = 4;

    private final ToIntFunction<EnvironmentConfigGroup> temperatureChange;
    private final boolean coldAtNight;

    BiomeCategory(ToIntFunction<EnvironmentConfigGroup> temperatureChange, boolean coldAtNight) {
        this.temperatureChange = temperatureChange;
        this.coldAtNight = coldAtNight;
    }

    public static BiomeCategory fromBiome(RegistryEntry<Biome> biomeEntry, ThermooSeason season) {
        Biome biome = biomeEntry.value();
        EnvironmentConfigGroup config = Frostiful.getConfig().environmentConfig;

        // +inf means only tags will be considered
        float temperature = biome.getTemperature();
        SeasonalBiomeTags tags = SeasonalBiomeTags.forSeason(season);

        BiomeCategory category;
        if (biomeEntry.isIn(tags.normal()) || biomeEntry.isIn(FBiomeTags.FREEZING_BLACKLIST_BIOMES)) {
            category = NORMAL;
        } else if (biomeEntry.isIn(tags.freezing())) {
            category = FREEZING;
        } else if (temperature <= SNOW_TEMPERATURE || biomeEntry.isIn(tags.cold())) {
            category = COLD;
        } else if (
                biomeEntry.isIn(tags.cool())
                        || (biomeEntry.isIn(FBiomeTags.DRY_BIOMES) && config.doDryBiomeNightFreezing())
        ) {
            category = COLD_AT_NIGHT;
        } else {
            category = NORMAL;
        }

        return category;
    }

    public int getTemperatureChange(World world, BlockPos pos) {
        EnvironmentConfigGroup config = Frostiful.getConfig().environmentConfig;
        int change = this.temperatureChange.applyAsInt(config);

        int sunlight = world.getLightLevel(LightType.SKY, pos) - world.getAmbientDarkness();
        if (coldAtNight && sunlight <= NIGHT_TIME_SURFACE_LIGHT) {
            change += config.getNightTemperatureShift();
        }

        return change;
    }
}
