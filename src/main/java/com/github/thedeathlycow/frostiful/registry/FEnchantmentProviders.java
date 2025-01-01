package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.enchantment.provider.EnchantmentProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class FEnchantmentProviders {

    public static final RegistryKey<EnchantmentProvider> FROSTOLOGER_SPAWN_FROST_WAND = key("frostologer_spawn_frost_wand");

    private static RegistryKey<EnchantmentProvider> key(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT_PROVIDER, Frostiful.id(id));
    }

    private FEnchantmentProviders() {

    }
}