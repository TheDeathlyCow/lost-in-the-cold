package com.github.thedeathlycow.frostiful.item;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.FrostifulConfig;
import com.github.thedeathlycow.frostiful.registry.FItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class FSmithingTemplateItem {

    public static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    public static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;

    /// Texture IDs ///
    public static final Identifier EMPTY_ARMOR_SLOT_HELMET_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_helmet");
    public static final Identifier EMPTY_ARMOR_SLOT_CHESTPLATE_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_chestplate");
    public static final Identifier EMPTY_ARMOR_SLOT_LEGGINGS_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_leggings");
    public static final Identifier EMPTY_ARMOR_SLOT_BOOTS_TEXTURE = Identifier.ofVanilla("item/empty_armor_slot_boots");

    public static void addTemplatesToLoot() {
        FrostifulConfig config = Frostiful.getConfig();
        addTemplateToLoot(
                FItems.ICE_SKATE_UPGRADE_TEMPLATE,
                Identifier.ofVanilla("chests/igloo_chest"),
                config.combatConfig.getSkateUpgradeTemplateIglooGenerateChance()
        );
    }

    private static void addTemplateToLoot(Item template, Identifier lootTableId, float chance) {
        LootTableEvents.MODIFY.register(
                (key, tableBuilder, source, registries) -> {
                    if (source.isBuiltin() && lootTableId.equals(key.getValue())) {
                        LootPool.Builder builder = LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1f))
                                .conditionally(RandomChanceLootCondition.builder(chance))
                                .with(ItemEntry.builder(template));
                        tableBuilder.pool(builder);
                    }
                }
        );
    }

    private FSmithingTemplateItem() {
    }
}
