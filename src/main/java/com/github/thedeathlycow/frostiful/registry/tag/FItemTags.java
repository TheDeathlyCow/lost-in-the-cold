package com.github.thedeathlycow.frostiful.registry.tag;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class FItemTags {

    public static final TagKey<Item> FUR = register("fur");

    public static final TagKey<Item> POWDER_SNOW_WALKABLE = register("powder_snow_walkable");

    public static final TagKey<Item> SUN_LICHENS = register("sun_lichens");

    public static final TagKey<Item> ICE_SKATES = register("ice_skates");

    public static final TagKey<Item> WARM_FOODS = register("warm_foods");

    public static final TagKey<Item> FROSTOLOGY_CLOAKS = register("frostology_cloaks");

    private static TagKey<Item> register(String id) {
        return TagKey.of(RegistryKeys.ITEM, Frostiful.id(id));
    }

}
