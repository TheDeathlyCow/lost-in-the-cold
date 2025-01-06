package com.github.thedeathlycow.frostiful.registry.tag;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class FBannerPatternTags {

    public static final TagKey<BannerPattern> SNOWFLAKE_PATTERN_ITEM = key("pattern_item/snowflake");
    public static final TagKey<BannerPattern> ICICLE_PATTERN_ITEM = key("pattern_item/icicle");

    private static TagKey<BannerPattern> key(String id) {
        return TagKey.of(RegistryKeys.BANNER_PATTERN, Frostiful.id(id));
    }

    private FBannerPatternTags() {

    }
}
