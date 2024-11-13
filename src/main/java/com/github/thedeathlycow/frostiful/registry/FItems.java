package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.item.*;
import com.github.thedeathlycow.frostiful.item.cloak.AbstractFrostologyCloakItem;
import com.github.thedeathlycow.frostiful.item.cloak.FrostologyCloakItem;
import com.github.thedeathlycow.frostiful.item.cloak.InertFrostologyCloakItem;
import com.github.thedeathlycow.frostiful.registry.tag.FBannerPatternTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

public class FItems {

    public static final Item FUR_HELMET = new ArmorItem(
            FArmorMaterials.FUR,
            ArmorItem.Type.HELMET,
            new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(5))
    );
    public static final Item FUR_CHESTPLATE = new ArmorItem(
            FArmorMaterials.FUR,
            ArmorItem.Type.CHESTPLATE,
            new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(5))
    );
    public static final Item FUR_LEGGINGS = new ArmorItem(
            FArmorMaterials.FUR,
            ArmorItem.Type.LEGGINGS,
            new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(5))
    );
    public static final Item FUR_BOOTS = new ArmorItem(
            FArmorMaterials.FUR,
            ArmorItem.Type.BOOTS,
            new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(5))
    );

    public static final Item FUR_PADDING = new Item(new Item.Settings());

    public static final Item FUR_UPGRADE_TEMPLATE = FurSmithingUpgradeTemplate.createItem();


    public static final Item ICE_SKATE_UPGRADE_TEMPLATE = IceSkateUpgradeTemplate.createItem();

    public static final Item FROSTY_ARMOR_TRIM_SMITHING_TEMPLATE = SmithingTemplateItem.of(FArmorTrimPatterns.FROSTY);

    public static final Item FUR_PADDED_CHAINMAIL_HELMET = new ArmorItem(
            FArmorMaterials.FUR_LINED_CHAINMAIL,
            ArmorItem.Type.HELMET,
            new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))
    );
    public static final Item FUR_PADDED_CHAINMAIL_CHESTPLATE = new ArmorItem(
            FArmorMaterials.FUR_LINED_CHAINMAIL,
            ArmorItem.Type.CHESTPLATE,
            new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))
    );
    public static final Item FUR_PADDED_CHAINMAIL_LEGGINGS = new ArmorItem(
            FArmorMaterials.FUR_LINED_CHAINMAIL,
            ArmorItem.Type.LEGGINGS,
            new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))
    );
    public static final Item FUR_PADDED_CHAINMAIL_BOOTS = new ArmorItem(
            FArmorMaterials.FUR_LINED_CHAINMAIL,
            ArmorItem.Type.BOOTS,
            new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))
    );

    public static final Item GLACIAL_HEART = new Item(
            new Item.Settings()
                    .rarity(Rarity.UNCOMMON)
    );

    public static final Item INERT_FROSTOLOGY_CLOAK = new InertFrostologyCloakItem(
            new Item.Settings()
                    .equipmentSlot(AbstractFrostologyCloakItem::getPreferredEquipmentSlot)
                    .maxCount(1)
                    .rarity(Rarity.UNCOMMON)
    );

    public static final Item FROSTOLOGY_CLOAK = new FrostologyCloakItem(
            new Item.Settings()
                    .equipmentSlot(AbstractFrostologyCloakItem::getPreferredEquipmentSlot)
                    .attributeModifiers(FrostologyCloakItem.createAttributeModifiers())
                    .rarity(Rarity.EPIC)
                    .maxCount(1)
    );

    public static final Item ICE_SKATES = new ArmorItem(
            FArmorMaterials.FUR,
            ArmorItem.Type.BOOTS,
            new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(5))
    );

    public static final Item ARMORED_ICE_SKATES = new ArmorItem(
            FArmorMaterials.FUR_LINED_CHAINMAIL,
            ArmorItem.Type.BOOTS,
            new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))
    );

    public static final Item POLAR_BEAR_FUR_TUFT = new Item(new Item.Settings());
    public static final Item WOLF_FUR_TUFT = new Item(new Item.Settings());
    public static final Item OCELOT_FUR_TUFT = new Item(new Item.Settings());
    public static final Item ICICLE = new IcicleItem(FBlocks.ICICLE, new Item.Settings());
    public static final Item COLD_SUN_LICHEN = new BlockItem(FBlocks.COLD_SUN_LICHEN, new Item.Settings());
    public static final Item COOL_SUN_LICHEN = new BlockItem(FBlocks.COOL_SUN_LICHEN, new Item.Settings());
    public static final Item WARM_SUN_LICHEN = new BlockItem(FBlocks.WARM_SUN_LICHEN, new Item.Settings());
    public static final Item HOT_SUN_LICHEN = new BlockItem(FBlocks.HOT_SUN_LICHEN, new Item.Settings());
    public static final Item FROST_WAND = new FrostWandItem(
            new Item.Settings()
                    .maxCount(1)
                    .maxDamage(250)
                    .attributeModifiers(FrostWandItem.createAttributeModifiers())
                    .component(DataComponentTypes.TOOL, FrostWandItem.createToolComponent())
                    .rarity(Rarity.RARE)
    );
    public static final Item GLACIAL_ARROW = new GlacialArrowItem(new Item.Settings());

    public static final Item FROSTOLOGER_SPAWN_EGG = new SpawnEggItem(
            FEntityTypes.FROSTOLOGER,
            0x473882, 0xBEB2EB,
            new Item.Settings()
    );
    public static final Item CHILLAGER_SPAWN_EGG = new SpawnEggItem(
            FEntityTypes.CHILLAGER,
            0x3432A8, 0xA2CCFC,
            new Item.Settings()
    );

    public static final Item BITER_SPAWN_EGG = new SpawnEggItem(
            FEntityTypes.BITER,
            0xEBFEFF,
            0x2E64C3,
            new Item.Settings()
    );

    public static final Item FROZEN_TORCH = new VerticallyAttachableBlockItem(
            FBlocks.FROZEN_TORCH,
            FBlocks.FROZEN_WALL_TORCH,
            new Item.Settings(),
            Direction.DOWN
    );

    public static final Item PACKED_SNOW = new BlockItem(FBlocks.PACKED_SNOW, new Item.Settings());
    public static final Item PACKED_SNOWBALL = new PackedSnowBallItem(new Item.Settings().maxCount(16));
    public static final Item PACKED_SNOW_BLOCK = new BlockItem(FBlocks.PACKED_SNOW_BLOCK, new Item.Settings());
    public static final Item PACKED_SNOW_BRICKS = new BlockItem(FBlocks.PACKED_SNOW_BRICKS, new Item.Settings());
    public static final Item PACKED_SNOW_BRICK_STAIRS = new BlockItem(FBlocks.PACKED_SNOW_BRICK_STAIRS, new Item.Settings());
    public static final Item PACKED_SNOW_BRICK_SLAB = new BlockItem(FBlocks.PACKED_SNOW_BRICK_SLAB, new Item.Settings());
    public static final Item PACKED_SNOW_BRICK_WALL = new BlockItem(FBlocks.PACKED_SNOW_BRICK_WALL, new Item.Settings());

    public static final Item ICE_PANE = new BlockItem(FBlocks.ICE_PANE, new Item.Settings());
    public static final Item CUT_PACKED_ICE = new BlockItem(FBlocks.CUT_PACKED_ICE, new Item.Settings());
    public static final Item CUT_PACKED_ICE_STAIRS = new BlockItem(FBlocks.CUT_PACKED_ICE_STAIRS, new Item.Settings());
    public static final Item CUT_PACKED_ICE_SLAB = new BlockItem(FBlocks.CUT_PACKED_ICE_SLAB, new Item.Settings());
    public static final Item CUT_PACKED_ICE_WALL = new BlockItem(FBlocks.CUT_PACKED_ICE_WALL, new Item.Settings());
    public static final Item CUT_BLUE_ICE = new BlockItem(FBlocks.CUT_BLUE_ICE, new Item.Settings());
    public static final Item CUT_BLUE_ICE_STAIRS = new BlockItem(FBlocks.CUT_BLUE_ICE_STAIRS, new Item.Settings());
    public static final Item CUT_BLUE_ICE_SLAB = new BlockItem(FBlocks.CUT_BLUE_ICE_SLAB, new Item.Settings());
    public static final Item CUT_BLUE_ICE_WALL = new BlockItem(FBlocks.CUT_BLUE_ICE_WALL, new Item.Settings());

    public static final Item SNOWFLAKE_BANNER_PATTERN = new BannerPatternItem(
            FBannerPatternTags.SNOWFLAKE_PATTERN_ITEM,
            new Item.Settings().maxCount(1)
    );

    public static void registerItems() {
        Frostiful.LOGGER.debug("Initialized Frostiful items");
        register("fur_helmet", FUR_HELMET);
        register("fur_chestplate", FUR_CHESTPLATE);
        register("fur_leggings", FUR_LEGGINGS);
        register("fur_boots", FUR_BOOTS);

        register("fur_padded_chainmail_helmet", FUR_PADDED_CHAINMAIL_HELMET);
        register("fur_padded_chainmail_chestplate", FUR_PADDED_CHAINMAIL_CHESTPLATE);
        register("fur_padded_chainmail_leggings", FUR_PADDED_CHAINMAIL_LEGGINGS);
        register("fur_padded_chainmail_boots", FUR_PADDED_CHAINMAIL_BOOTS);

        register("fur_padding", FUR_PADDING);
        register("fur_upgrade_template", FUR_UPGRADE_TEMPLATE);
        register("ice_skate_upgrade_template", ICE_SKATE_UPGRADE_TEMPLATE);
        register("frosty_armor_trim_smithing_template", FROSTY_ARMOR_TRIM_SMITHING_TEMPLATE);

        register("glacial_heart", GLACIAL_HEART);
        register("inert_frostology_cloak", INERT_FROSTOLOGY_CLOAK);
        register("frostology_cloak", FROSTOLOGY_CLOAK);
        register("ice_skates", ICE_SKATES);
        register("armored_ice_skates", ARMORED_ICE_SKATES);

        register("polar_bear_fur_tuft", POLAR_BEAR_FUR_TUFT);
        register("wolf_fur_tuft", WOLF_FUR_TUFT);
        register("ocelot_fur_tuft", OCELOT_FUR_TUFT);

        register("icicle", ICICLE);
        register("cold_sun_lichen", COLD_SUN_LICHEN);
        register("cool_sun_lichen", COOL_SUN_LICHEN);
        register("warm_sun_lichen", WARM_SUN_LICHEN);
        register("hot_sun_lichen", HOT_SUN_LICHEN);
        register("frost_wand", FROST_WAND);
        register("glacial_arrow", GLACIAL_ARROW);
        register("frostologer_spawn_egg", FROSTOLOGER_SPAWN_EGG);
        register("chillager_spawn_egg", CHILLAGER_SPAWN_EGG);
        register("biter_spawn_egg", BITER_SPAWN_EGG);

        register("frozen_torch", FROZEN_TORCH);

        register("packed_snow", PACKED_SNOW);
        register("packed_snowball", PACKED_SNOWBALL);
        register("packed_snow_block", PACKED_SNOW_BLOCK);
        register("packed_snow_bricks", PACKED_SNOW_BRICKS);
        register("packed_snow_brick_stairs", PACKED_SNOW_BRICK_STAIRS);
        register("packed_snow_brick_slab", PACKED_SNOW_BRICK_SLAB);
        register("packed_snow_brick_wall", PACKED_SNOW_BRICK_WALL);

        register("ice_pane", ICE_PANE);
        register("cut_packed_ice", CUT_PACKED_ICE);
        register("cut_packed_ice_stairs", CUT_PACKED_ICE_STAIRS);
        register("cut_packed_ice_slab", CUT_PACKED_ICE_SLAB);
        register("cut_packed_ice_wall", CUT_PACKED_ICE_WALL);
        register("cut_blue_ice", CUT_BLUE_ICE);
        register("cut_blue_ice_stairs", CUT_BLUE_ICE_STAIRS);
        register("cut_blue_ice_slab", CUT_BLUE_ICE_SLAB);
        register("cut_blue_ice_wall", CUT_BLUE_ICE_WALL);

        register("snowflake_banner_pattern", SNOWFLAKE_BANNER_PATTERN);
    }

    private static void register(String id, Item item) {
        Registry.register(Registries.ITEM, Identifier.of(Frostiful.MODID, id), item);
    }

}
