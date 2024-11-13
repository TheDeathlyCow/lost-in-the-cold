package com.github.thedeathlycow.frostiful.entity.loot;

import com.github.thedeathlycow.frostiful.item.cloak.AbstractFrostologyCloakItem;
import com.github.thedeathlycow.frostiful.registry.FLootConditionTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.item.ItemPredicate;

public record ChestEquippedWithTrinketLootCondition(
        ItemPredicate items
) implements LootCondition {

    public static final MapCodec<ChestEquippedWithTrinketLootCondition> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ItemPredicate.CODEC
                            .fieldOf("items")
                            .forGetter(ChestEquippedWithTrinketLootCondition::items)
            ).apply(instance, ChestEquippedWithTrinketLootCondition::new)
    );

    @Override
    public LootConditionType getType() {
        return FLootConditionTypes.CHEST_EQUPPED_WITH_TRINKET;
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
        if (entity instanceof PlayerEntity player) {
            return AbstractFrostologyCloakItem.isWearing(player, this.items);
        }

        return false;
    }
}
