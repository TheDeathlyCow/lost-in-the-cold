package com.github.thedeathlycow.frostiful.entity.component;

import com.github.thedeathlycow.frostiful.registry.FComponents;
import com.github.thedeathlycow.frostiful.registry.FLootTables;
import com.github.thedeathlycow.frostiful.registry.tag.FEntityTypeTags;
import com.github.thedeathlycow.frostiful.util.FLootHelper;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class BrushableComponent implements Component, AutoSyncedComponent {

    private static final String LAST_BRUSHED_TIME_KEY = "last_brushed_time";
    private static final int BRUSH_COOLDOWN = 20 * 300;
    private long lastBrushTime = -1;

    private final AnimalEntity provider;

    public BrushableComponent(AnimalEntity provider) {
        this.provider = provider;
    }

    /**
     * @param animal The mob that was interacted with
     * @param player The player who interacted with the mob
     * @param hand   The hand they used to interact
     * @param base   Original action result for the interaction
     */
    public static ActionResult interactWithMob(MobEntity animal, PlayerEntity player, Hand hand, ActionResult base) {
        if (base == ActionResult.FAIL) {
            return ActionResult.FAIL;
        }

        ItemStack heldItem = player.getStackInHand(hand);
        BrushableComponent component = FComponents.BRUSHABLE_COMPONENT.getNullable(animal);
        if (component != null && component.isBrushable() && heldItem.isIn(ConventionalItemTags.BRUSH_TOOLS)) {
            component.brush(player);

            if (!animal.getWorld().isClient) {
                heldItem.damage(16, player, LivingEntity.getSlotForHand(hand));
            }
        }

        return ActionResult.success(animal.getWorld().isClient);
    }

    @Override
    public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeLong(this.lastBrushTime);
    }

    @Override
    public void applySyncPacket(RegistryByteBuf buf) {
        this.lastBrushTime = buf.readLong();
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (tag.contains(LAST_BRUSHED_TIME_KEY, NbtElement.LONG_TYPE)) {
            this.lastBrushTime = tag.getLong(LAST_BRUSHED_TIME_KEY);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.wasBrushed()) {
            tag.putLong(LAST_BRUSHED_TIME_KEY, this.getLastBrushTime());
        }
    }

    public long getLastBrushTime() {
        return lastBrushTime;
    }

    public void setLastBrushTime(long lastBrushTime) {
        if (this.lastBrushTime != lastBrushTime) {
            this.lastBrushTime = lastBrushTime;
            FComponents.BRUSHABLE_COMPONENT.sync(this.provider);
        }
    }

    public boolean isBrushable() {
        return this.provider.isAlive()
                && !this.provider.isBaby()
                && !this.wasBrushed();
    }

    public boolean wasBrushed() {
        return lastBrushTime >= 0L
                && this.provider.getWorld().getTimeOfDay() - lastBrushTime <= BRUSH_COOLDOWN;
    }

    private void brush(PlayerEntity brusher) {
        World world = provider.getWorld();
        world.playSoundFromEntity(
                null,
                provider,
                SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC,
                SoundCategory.PLAYERS,
                1.0f, 1.0f
        );
        provider.emitGameEvent(GameEvent.SHEAR, brusher);

        if (!world.isClient) {
            RegistryKey<LootTable> furLootTable = getLootTableForAnimal(provider);
            if (furLootTable != null) {
                FLootHelper.dropLootFromEntity(provider, furLootTable);
            }
        }

        this.setLastBrushTime(world.getTime());
    }

    @Nullable
    private static RegistryKey<LootTable> getLootTableForAnimal(AnimalEntity animal) {
        EntityType<?> type = animal.getType();
        if (type.isIn(FEntityTypeTags.BRUSHING_DROPS_POLAR_BEAR_FUR)) {
            return FLootTables.POLAR_BEAR_BRUSHING_GAMEPLAY;
        } else if (type.isIn(FEntityTypeTags.BRUSHING_DROPS_WOLF_FUR)) {
            return FLootTables.WOLF_BRUSHING_GAMEPLAY;
        } else if (type.isIn(FEntityTypeTags.BRUSHING_DROPS_OCELOT_FUR)) {
            return FLootTables.OCELOT_BRUSHING_GAMEPLAY;
        } else {
            return null;
        }
    }
}
