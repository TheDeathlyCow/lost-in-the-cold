package com.github.thedeathlycow.frostiful.entity;

import com.github.thedeathlycow.frostiful.registry.FComponents;
import com.github.thedeathlycow.frostiful.util.FLootHelper;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public interface FBrushable {

    int BRUSH_COOLDOWN = 20 * 300;

    void frostiful$brush(PlayerEntity source, SoundCategory shearedSoundCategory);

    boolean frostiful$isBrushable();

    boolean frostiful$wasBrushed();

    static void brushEntity(
            AnimalEntity animalEntity,
            SoundCategory shearedSoundCategory,
            RegistryKey<LootTable> furLootTable
    ) {
        World world = animalEntity.getWorld();
        world.playSoundFromEntity(
                null,
                animalEntity,
                SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC,
                shearedSoundCategory,
                1.0f, 1.0f
        );

        if (!world.isClient) {
            FLootHelper.dropLootFromEntity(animalEntity, furLootTable);
        }

        FComponents.BRUSHABLE_COMPONENT.get(animalEntity).setLastBrushTime(world.getTime());
    }

}
