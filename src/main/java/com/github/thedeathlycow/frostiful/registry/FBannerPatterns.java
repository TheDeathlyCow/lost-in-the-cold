package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BannerPatterns;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class FBannerPatterns {

    public static final RegistryKey<BannerPattern> SNOWFLAKE = key("snowflake");
    public static final RegistryKey<BannerPattern> ICICLE = key("icicle");
    public static final RegistryKey<BannerPattern> FROSTOLOGY = key("frostology");

    public static void bootstrap(Registerable<BannerPattern> registry) {
        BannerPatterns.register(registry, SNOWFLAKE);
        BannerPatterns.register(registry, ICICLE);
        BannerPatterns.register(registry, FROSTOLOGY);
    }

    private static RegistryKey<BannerPattern> key(String id) {
        return RegistryKey.of(RegistryKeys.BANNER_PATTERN, Frostiful.id(id));
    }

    private FBannerPatterns() {

    }

}
