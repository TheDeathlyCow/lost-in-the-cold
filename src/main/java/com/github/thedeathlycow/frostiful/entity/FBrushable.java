package com.github.thedeathlycow.frostiful.entity;

import com.github.thedeathlycow.frostiful.registry.FComponents;
import com.github.thedeathlycow.frostiful.util.FLootHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ArmadilloEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public interface FBrushable {

    int BRUSH_COOLDOWN = 20 * 300;

    void frostiful$brush(PlayerEntity source, SoundCategory shearedSoundCategory);

    boolean frostiful$isBrushable();

    boolean frostiful$wasBrushed();

    /**
     * @param animal The mob that was interacted with
     * @param player The player who interacted with the mob
     * @param hand   The hand they used to interact
     * @param base   Original action result for the interaction
     */
    static ActionResult interact(MobEntity animal, PlayerEntity player, Hand hand, ActionResult base) {
        if (base == ActionResult.FAIL) {
            return ActionResult.FAIL;
        }

        ItemStack heldItem = player.getStackInHand(hand);
        if (heldItem.isOf(Items.BRUSH) && animal instanceof FBrushable brushable && brushable.frostiful$isBrushable()) {
            brushable.frostiful$brush(player, SoundCategory.PLAYERS);
            animal.emitGameEvent(GameEvent.SHEAR, player);

            if (!animal.getWorld().isClient) {
                heldItem.damage(16, player, LivingEntity.getSlotForHand(hand));
            }
        }

        return ActionResult.success(animal.getWorld().isClient);
    }

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
