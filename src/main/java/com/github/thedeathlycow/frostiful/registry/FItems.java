package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.item.*;
import com.github.thedeathlycow.frostiful.item.cloak.AbstractFrostologyCloakItem;
import com.github.thedeathlycow.frostiful.item.cloak.FrostologyCloakItem;
import com.github.thedeathlycow.frostiful.item.cloak.InertFrostologyCloakItem;
import com.github.thedeathlycow.frostiful.registry.tag.FBannerPatternTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.*;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FItems {

    public static final Item FUR_HELMET = register(
            "fur_helmet",
            settings -> new ArmorItem(
                    FArmorMaterials.FUR,
                    EquipmentType.HELMET,
                    settings.maxDamage(EquipmentType.HELMET.getMaxDamage(5))
            )
    );
    public static final Item FUR_CHESTPLATE = register(
            "fur_chestplate",
            settings -> new ArmorItem(
                    FArmorMaterials.FUR,
                    EquipmentType.CHESTPLATE,
                    settings.maxDamage(EquipmentType.CHESTPLATE.getMaxDamage(5))
            )
    );
    public static final Item FUR_LEGGINGS = register(
            "fur_leggings",
            settings -> new ArmorItem(
                    FArmorMaterials.FUR,
                    EquipmentType.LEGGINGS,
                    settings.maxDamage(EquipmentType.LEGGINGS.getMaxDamage(5))
            )
    );
    public static final Item FUR_BOOTS = register(
            "fur_boots",
            settings -> new ArmorItem(
                    FArmorMaterials.FUR,
                    EquipmentType.BOOTS,
                    settings.maxDamage(EquipmentType.BOOTS.getMaxDamage(5))
            )
    );

    public static final Item FUR_PADDING = register("fur_padding");

    public static final Item FUR_UPGRADE_TEMPLATE = register(
            "fur_upgrade_template",
            FurSmithingUpgradeTemplate.createItem()
    );


    public static final Item ICE_SKATE_UPGRADE_TEMPLATE = register(
            "ice_skate_upgrade_template",
            IceSkateUpgradeTemplate.createItem()
    );

    public static final Item FROSTY_ARMOR_TRIM_SMITHING_TEMPLATE = register(
            "frosty_armor_trim_smithing_template",
            SmithingTemplateItem.of(FArmorTrimPatterns.FROSTY)
    );

    public static final Item FUR_PADDED_CHAINMAIL_HELMET = register(
            "fur_padded_chainmail_helmet",
            settings -> new ArmorItem(
                    FArmorMaterials.FUR_LINED_CHAINMAIL,
                    EquipmentType.HELMET,
                    settings
                            .maxDamage(EquipmentType.HELMET.getMaxDamage(15))
            )
    );
    public static final Item FUR_PADDED_CHAINMAIL_CHESTPLATE = register(
            "fur_padded_chainmail_chestplate",
            settings -> new ArmorItem(
                    FArmorMaterials.FUR_LINED_CHAINMAIL,
                    EquipmentType.CHESTPLATE,
                    settings.maxDamage(EquipmentType.CHESTPLATE.getMaxDamage(15))
            )
    );
    public static final Item FUR_PADDED_CHAINMAIL_LEGGINGS = register(
            "fur_padded_chainmail_leggings",
            settings -> new ArmorItem(
                    FArmorMaterials.FUR_LINED_CHAINMAIL,
                    EquipmentType.LEGGINGS,
                    settings.maxDamage(EquipmentType.LEGGINGS.getMaxDamage(15))
            )
    );
    public static final Item FUR_PADDED_CHAINMAIL_BOOTS = register(
            "fur_padded_chainmail_boots",
            settings -> new ArmorItem(
                    FArmorMaterials.FUR_LINED_CHAINMAIL,
                    EquipmentType.BOOTS,
                    settings.maxDamage(EquipmentType.BOOTS.getMaxDamage(15))
            )
    );

    public static final Item GLACIAL_HEART = register(
            "glacial_heart",
            settings -> new Item(settings.rarity(Rarity.UNCOMMON))
    );

    public static final Item INERT_FROSTOLOGY_CLOAK = register(
            "inert_frostology_cloak",
            settings -> new InertFrostologyCloakItem(
                    settings.equipmentSlot(AbstractFrostologyCloakItem::getPreferredEquipmentSlot)
                            .maxCount(1)
                            .rarity(Rarity.UNCOMMON)
            )
    );

    public static final Item FROSTOLOGY_CLOAK = register(
            "frostology_cloak",
            settings -> new FrostologyCloakItem(
                    settings.equipmentSlot(AbstractFrostologyCloakItem::getPreferredEquipmentSlot)
                            .attributeModifiers(FrostologyCloakItem.createAttributeModifiers())
                            .rarity(Rarity.EPIC)
                            .maxCount(1)
            )
    );

    public static final Item ICE_SKATES = register(
            "ice_skates",
            new ArmorItem(
                    FArmorMaterials.FUR,
                    EquipmentType.BOOTS,
                    new Item.Settings()
                            .maxDamage(EquipmentType.BOOTS.getMaxDamage(5))
            )
    );

    public static final Item ARMORED_ICE_SKATES = register(
            "armored_ice_skates",
            settings -> new ArmorItem(
                    FArmorMaterials.FUR_LINED_CHAINMAIL,
                    EquipmentType.BOOTS,
                    settings.maxDamage(EquipmentType.BOOTS.getMaxDamage(15))
            )
    );

    public static final Item POLAR_BEAR_FUR_TUFT = register("polar_bear_fur_tuft");
    public static final Item WOLF_FUR_TUFT = register("wolf_fur_tuft");
    public static final Item OCELOT_FUR_TUFT = register("ocelot_fur_tuft");

    public static final Item ICICLE = register(FBlocks.ICICLE);
    public static final Item COLD_SUN_LICHEN = register(FBlocks.COLD_SUN_LICHEN);
    public static final Item COOL_SUN_LICHEN = register(FBlocks.COOL_SUN_LICHEN);
    public static final Item WARM_SUN_LICHEN = register(FBlocks.WARM_SUN_LICHEN);
    public static final Item HOT_SUN_LICHEN = register(FBlocks.HOT_SUN_LICHEN);

    public static final Item FROST_WAND = register(
            "frost_wand",
            settings -> new FrostWandItem(
                    settings
                            .maxCount(1)
                            .maxDamage(250)
                            .attributeModifiers(FrostWandItem.createAttributeModifiers())
                            .component(DataComponentTypes.TOOL, FrostWandItem.createToolComponent())
                            .rarity(Rarity.RARE)
            )
    );
    public static final Item GLACIAL_ARROW = register(
            "glacial_arrow",
            GlacialArrowItem::new
    );

    public static final Item FROSTOLOGER_SPAWN_EGG = register(
            "frostologer_spawn_egg",
            settings -> new SpawnEggItem(
                    FEntityTypes.FROSTOLOGER,
                    0x473882, 0xBEB2EB,
                    settings
            )
    );
    public static final Item CHILLAGER_SPAWN_EGG = register(
            "chillager_spawn_egg",
            settings -> new SpawnEggItem(
                    FEntityTypes.CHILLAGER,
                    0x3432A8, 0xA2CCFC,
                    settings
            )
    );

    public static final Item BITER_SPAWN_EGG = register(
            "biter_spawn_egg",
            settings -> new SpawnEggItem(
                    FEntityTypes.BITER,
                    0xEBFEFF,
                    0x2E64C3,
                    settings
            )
    );

    public static final Item FROZEN_TORCH = register(
            "frozen_torch",
            settings -> new VerticallyAttachableBlockItem(
                    FBlocks.FROZEN_TORCH,
                    FBlocks.FROZEN_WALL_TORCH,
                    Direction.DOWN,
                    settings
            )
    );

    public static final Item PACKED_SNOW = register(FBlocks.PACKED_SNOW);
    public static final Item PACKED_SNOWBALL = register(
            "packed_snowball",
            settings -> new PackedSnowBallItem(settings.maxCount(16))
    );
    public static final Item PACKED_SNOW_BLOCK = register(FBlocks.PACKED_SNOW_BLOCK);
    public static final Item PACKED_SNOW_BRICKS = register(FBlocks.PACKED_SNOW_BRICKS);
    public static final Item PACKED_SNOW_BRICK_STAIRS = register(FBlocks.PACKED_SNOW_BRICK_STAIRS);
    public static final Item PACKED_SNOW_BRICK_SLAB = register(FBlocks.PACKED_SNOW_BRICK_SLAB);
    public static final Item PACKED_SNOW_BRICK_WALL = register(FBlocks.PACKED_SNOW_BRICK_WALL);

    public static final Item ICE_PANE = register(FBlocks.ICE_PANE);
    public static final Item CUT_PACKED_ICE = register(FBlocks.CUT_PACKED_ICE);
    public static final Item CUT_PACKED_ICE_STAIRS = register(FBlocks.CUT_PACKED_ICE_STAIRS);
    public static final Item CUT_PACKED_ICE_SLAB = register(FBlocks.CUT_PACKED_ICE_SLAB);
    public static final Item CUT_PACKED_ICE_WALL = register(FBlocks.CUT_PACKED_ICE_WALL);
    public static final Item CUT_BLUE_ICE = register(FBlocks.CUT_BLUE_ICE);
    public static final Item CUT_BLUE_ICE_STAIRS = register(FBlocks.CUT_BLUE_ICE_STAIRS);
    public static final Item CUT_BLUE_ICE_SLAB = register(FBlocks.CUT_BLUE_ICE_SLAB);
    public static final Item CUT_BLUE_ICE_WALL = register(FBlocks.CUT_BLUE_ICE_WALL);

    public static final Item SNOWFLAKE_BANNER_PATTERN = register(
            "snowflake_banner_pattern",
            settings -> new BannerPatternItem(FBannerPatternTags.SNOWFLAKE_PATTERN_ITEM, settings.maxCount(1))
    );

    public static final Item ICICLE_BANNER_PATTERN = register(
            "icicle_banner_pattern",
            settings -> new BannerPatternItem(FBannerPatternTags.ICICLE_PATTERN_ITEM, settings.maxCount(1))
    );

    public static final Item FROSTOLOGY_BANNER_PATTERN = register(
            "frostology_banner_pattern",
            settings -> new BannerPatternItem(FBannerPatternTags.FROSTOLOGY_PATTERN_ITEM, settings.maxCount(1))
    );

    public static final Item ICY_TRIAL_SPAWNER = register(FBlocks.ICY_TRIAL_SPAWNER);

    public static final Item ICY_VAULT = register(FBlocks.ICY_VAULT);

    public static final Item CASTLE_KEY = register("castle_key");

    public static final Item OMINOUS_CASTLE_KEY = register("ominous_castle_key");

    public static final Item BRITTLE_ICE = register(FBlocks.BRITTLE_ICE);

    public static final Item FROZEN_ROD = register("frozen_rod");

    public static final Item GLACIAL_ARMOR_TRIM_SMITHING_TEMPLATE = register(
            "glacial_armor_trim_smithing_template",
            SmithingTemplateItem.of(FArmorTrimPatterns.GLACIAL)
    );

    public static final Item SNOW_MAN_ARMOR_TRIM_SMITHING_TEMPLATE = register(
            "snow_man_armor_trim_smithing_template",
            SmithingTemplateItem.of(FArmorTrimPatterns.SNOW_MAN)
    );

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful items");
        FSmithingTemplateItem.addTemplatesToLoot();
    }

    private static Item register(Block block) {
        return register(block, new Item.Settings());
    }

    private static Item register(Block block, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, block.getRegistryEntry().registryKey().getValue());
        return register(key, keyedSettings -> new BlockItem(block, keyedSettings), settings);
    }

    private static Item register(String id) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Frostiful.id(id));
        return register(key, Item::new, new Item.Settings());
    }

    private static Item register(String id, Function<Item.Settings, Item> factory) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Frostiful.id(id));
        return register(key, factory, new Item.Settings());
    }

    private static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.ITEM, key, item);
    }

    private FItems() {

    }
}
