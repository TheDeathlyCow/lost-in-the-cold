package com.github.thedeathlycow.frostiful.client.render.feature;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.entity.frostologer.FrostologerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Comparator;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public enum FrostLayers {

    NONE(0.0f, null),
    LOW(-0.25f, Frostiful.id("textures/entity/illager/frostologer/low_frost.png")),
    MEDIUM(-0.5f, Frostiful.id("textures/entity/illager/frostologer/medium_frost.png")),
    HIGH(FrostologerEntity.MAX_POWER_SCALE_START, Frostiful.id("textures/entity/illager/frostologer/high_frost.png"));

    public static final FrostLayers[] LAYERS_WITHOUT_NONE = Stream.of(FrostLayers.values())
            .filter(layer -> layer != NONE)
            .sorted(Comparator.comparingDouble(layer -> layer.maximumTemperatureScale))
            .toArray(FrostLayers[]::new);

    private final float maximumTemperatureScale;
    private final Identifier texture;

    FrostLayers(float maximumTemperatureScale, Identifier texture) {
        this.maximumTemperatureScale = maximumTemperatureScale;
        this.texture = texture;
    }

    public static FrostLayers fromFrostologer(FrostologerEntity frostologer) {
        float scale = frostologer.thermoo$getTemperatureScale();
        for (var layer : LAYERS_WITHOUT_NONE) {
            if (scale < layer.maximumTemperatureScale) {
                return layer;
            }
        }
        return NONE;
    }

    public Identifier getTexture() {
        return texture;
    }
}
