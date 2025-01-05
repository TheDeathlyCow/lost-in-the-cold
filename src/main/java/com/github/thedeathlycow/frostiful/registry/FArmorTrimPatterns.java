package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class FArmorTrimPatterns {

    public static final RegistryKey<ArmorTrimPattern> FROSTY = of("frosty");

    public static final RegistryKey<ArmorTrimPattern> GLACIAL = of("glacial");

    public static final RegistryKey<ArmorTrimPattern> SNOW_MAN = of("snow_man");

    public static void bootstrap(Registerable<ArmorTrimPattern> registry) {
        Frostiful.LOGGER.debug("Bootstrap Frostiful armor trim patterns");
        register(registry, FItems.FROSTY_ARMOR_TRIM_SMITHING_TEMPLATE, FROSTY);
        register(registry, FItems.GLACIAL_ARMOR_TRIM_SMITHING_TEMPLATE, GLACIAL);
        register(registry, FItems.SNOW_MAN_ARMOR_TRIM_SMITHING_TEMPLATE, SNOW_MAN);
    }

    private static void register(Registerable<ArmorTrimPattern> registry, Item template, RegistryKey<ArmorTrimPattern> key) {
        ArmorTrimPattern armorTrimPattern = new ArmorTrimPattern(
                key.getValue(),
                Registries.ITEM.getEntry(template),
                Text.translatable(Util.createTranslationKey("trim_pattern", key.getValue())),
                false
        );
        registry.register(key, armorTrimPattern);
    }
    private static RegistryKey<ArmorTrimPattern> of(String id) {
        return RegistryKey.of(RegistryKeys.TRIM_PATTERN, Frostiful.id(id));
    }

    private FArmorTrimPatterns() {

    }
}
