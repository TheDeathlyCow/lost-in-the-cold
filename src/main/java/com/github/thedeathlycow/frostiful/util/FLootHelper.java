package com.github.thedeathlycow.frostiful.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class FLootHelper {

    public static <E extends LivingEntity> void dropLootFromEntity(E entity, RegistryKey<LootTable> lootTableId) {
        World world = entity.getWorld();
        if (world instanceof ServerWorld serverWorld && world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            LootTable lootTable = Objects.requireNonNull(world.getServer())
                    .getReloadableRegistries().getLootTable(lootTableId);
            List<ItemStack> generatedItems = lootTable.generateLoot(new LootContextParameterSet.Builder(serverWorld)
                    .add(LootContextParameters.THIS_ENTITY, entity)
                    .add(LootContextParameters.ORIGIN, entity.getPos())
                    .build(LootContextTypes.SELECTOR));

            for (ItemStack stack : generatedItems) {
                entity.dropStack(stack);
            }
        }
    }

    private FLootHelper() {

    }

}
