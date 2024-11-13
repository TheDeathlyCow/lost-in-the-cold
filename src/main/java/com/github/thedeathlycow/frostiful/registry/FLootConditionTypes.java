package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.entity.loot.ChestEquippedWithTrinketLootCondition;
import com.github.thedeathlycow.frostiful.entity.loot.RootedLootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class FLootConditionTypes {


    public static final LootConditionType ROOTED = new LootConditionType(RootedLootCondition.CODEC);
    public static final LootConditionType CHEST_EQUPPED_WITH_TRINKET = new LootConditionType(
            ChestEquippedWithTrinketLootCondition.CODEC
    );

    public static void registerAll() {
        Frostiful.LOGGER.debug("Initialized Frostiful loot condition types");
        register("rooted", ROOTED);
        register("chest_equipped_with_trinket", CHEST_EQUPPED_WITH_TRINKET);
    }

    private static void register(String name, LootConditionType lootCondition) {
        Registry.register(Registries.LOOT_CONDITION_TYPE, Frostiful.id(name), lootCondition);
    }

    private FLootConditionTypes() {

    }
}
