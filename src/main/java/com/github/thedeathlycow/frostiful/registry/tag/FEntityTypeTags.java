package com.github.thedeathlycow.frostiful.registry.tag;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class FEntityTypeTags {

    public static final TagKey<EntityType<?>> ROOT_IMMUNE = register("root_immune");

    public static final TagKey<EntityType<?>> HEAVY_ENTITY_TYPES = register("heavy_entity_types");

    public static final TagKey<EntityType<?>> DOES_NOT_BREAK_BRITTLE_ICE = register("does_not_break_brittle_ice");

    public static final TagKey<EntityType<?>> IS_BRUSHABLE = register("is_brushable");

    public static final TagKey<EntityType<?>> BRUSHING_DROPS_POLAR_BEAR_FUR = register("brushing/drops_polar_bear_fur");

    public static final TagKey<EntityType<?>> BRUSHING_DROPS_WOLF_FUR = register("brushing/drops_wolf_fur");

    public static final TagKey<EntityType<?>> BRUSHING_DROPS_OCELOT_FUR = register("brushing/drops_ocelot_fur");

    private static TagKey<EntityType<?>> register(String id) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Frostiful.id(id));
    }

    private static TagKey<EntityType<?>> registerCommon(String id) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("c", id));
    }

    private FEntityTypeTags() {

    }
}
