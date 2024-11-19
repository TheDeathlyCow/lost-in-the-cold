package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class FLootTables {


    public static final RegistryKey<LootTable> POLAR_BEAR_BRUSHING_GAMEPLAY = createKey("gameplay/polar_bear_brushing");
    public static final RegistryKey<LootTable> OCELOT_BRUSHING_GAMEPLAY = createKey("gameplay/ocelot_brushing");
    public static final RegistryKey<LootTable> WOLF_BRUSHING_GAMEPLAY = createKey("gameplay/wolf_brushing");

    public static final RegistryKey<LootTable> POLAR_BEAR_PLAYFIGHT_GAMEPLAY = createKey("gameplay/polar_bear_playfight");
    public static final RegistryKey<LootTable> OCELOT_PLAYFIGHT_GAMEPLAY = createKey("gameplay/ocelot_playfight");
    public static final RegistryKey<LootTable> WOLF_PLAYFIGHT_GAMEPLAY = createKey("gameplay/wolf_playfight");

    private static RegistryKey<LootTable> createKey(String name) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, Frostiful.id(name));
    }

    private FLootTables() {
    }
}
