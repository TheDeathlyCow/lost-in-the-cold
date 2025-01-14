package com.github.thedeathlycow.frostiful.registry.tag;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class FTrimTags {

    public static final TagKey<ArmorTrimPattern> CUSTOM_PATTERNS = pattern("custom_patterns");

    private static TagKey<ArmorTrimPattern> pattern(String id) {
        return TagKey.of(RegistryKeys.TRIM_PATTERN, Frostiful.id(id));
    }

    private FTrimTags() {

    }

}
