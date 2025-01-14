package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.block.*;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class FBlocks {

    public static final Block ICICLE = register(
            "icicle",
            IcicleBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.GLASS)
                    .ticksRandomly()
                    .strength(0.5F)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .dynamicBounds()
                    .offset(AbstractBlock.OffsetType.XZ)
                    .requiresTool()
    );

    public static final Block COLD_SUN_LICHEN = register(
            "cold_sun_lichen",
            settings -> new SunLichenBlock(
                    SunLichenBlock.COLD_LEVEL,
                    settings
            ),
            AbstractBlock.Settings.create()
                    .replaceable()
                    .mapColor(MapColor.RED)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .noCollision()
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.GLOW_LICHEN)
                    .ticksRandomly()
                    .nonOpaque()
                    .luminance(state -> 0)
    );
    public static final Block COOL_SUN_LICHEN = register(
            "cool_sun_lichen",
            settings -> new SunLichenBlock(
                    SunLichenBlock.COOL_LEVEL,
                    settings
            ),
            AbstractBlock.Settings.create()
                    .replaceable()
                    .mapColor(MapColor.RED)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .noCollision()
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.GLOW_LICHEN)
                    .ticksRandomly()
                    .nonOpaque()
                    .luminance(state -> 2)
    );
    public static final Block WARM_SUN_LICHEN = register(
            "warm_sun_lichen",
            settings -> new SunLichenBlock(
                    SunLichenBlock.WARM_LEVEL,
                    settings
            ),
            AbstractBlock.Settings.create()
                    .replaceable()
                    .mapColor(MapColor.RED)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .noCollision()
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.GLOW_LICHEN)
                    .ticksRandomly()
                    .nonOpaque()
                    .luminance(state -> 4)
    );
    public static final Block HOT_SUN_LICHEN = register(
            "hot_sun_lichen",
            settings -> new SunLichenBlock(
                    SunLichenBlock.HOT_LEVEL,
                    settings
            ),
            AbstractBlock.Settings.create()
                    .replaceable()
                    .mapColor(MapColor.RED)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .noCollision()
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.GLOW_LICHEN)
                    .ticksRandomly()
                    .nonOpaque()
                    .luminance(state -> 6)
    );

    public static final Block FROZEN_TORCH = register(
            "frozen_torch",
            FrozenTorchBlock::new,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .breakInstantly()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .sounds(BlockSoundGroup.WOOD)
    );

    public static final Block FROZEN_WALL_TORCH = register(
            "frozen_wall_torch",
            FrozenWallTorchBlock::new,
            copy(FROZEN_TORCH, true)
    );

    public static final Block PACKED_SNOW = register(
            "packed_snow",
            PackedSnowBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .replaceable()
                    .notSolid()
                    .strength(1.2f, 3.0f)
                    .requiresTool()
                    .sounds(FBlockSoundGroups.PACKED_SNOW)
                    .ticksRandomly()
                    .blockVision((state, world, pos) -> state.get(PackedSnowBlock.LAYERS) >= PackedSnowBlock.MAX_LAYERS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static final Block PACKED_SNOW_BLOCK = register(
            "packed_snow_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE_GRAY)
                    .requiresTool()
                    .strength(1.5f, 6.0f)
                    .sounds(FBlockSoundGroups.PACKED_SNOW)
    );

    public static final Block PACKED_SNOW_BRICKS = register(
            "packed_snow_bricks",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE_GRAY)
                    .requiresTool()
                    .strength(1.5f, 6.0f)
                    .sounds(FBlockSoundGroups.PACKED_SNOW)
    );

    public static final Block PACKED_SNOW_BRICK_STAIRS = register(
            "packed_snow_brick_stairs",
            settings -> new StairsBlock(
                    PACKED_SNOW_BRICKS.getDefaultState(),
                    settings
            ),
            AbstractBlock.Settings.copy(PACKED_SNOW_BRICKS)
    );

    public static final Block PACKED_SNOW_BRICK_SLAB = register(
            "packed_snow_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(PACKED_SNOW_BRICKS)
    );

    public static final Block PACKED_SNOW_BRICK_WALL = register(
            "packed_snow_brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(PACKED_SNOW_BRICKS)
    );

    public static final Block ICE_PANE = register(
            "ice_pane",
            IcePaneBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .strength(0.5f)
                    .ticksRandomly()
                    .slipperiness(0.98f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .solidBlock((state, world, pos) -> false)
    );

    public static final Block CUT_PACKED_ICE = register(
            "cut_packed_ice",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .instrument(NoteBlockInstrument.CHIME)
                    .slipperiness(0.98f)
                    .strength(0.75f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GLASS)
    );

    public static final Block CUT_PACKED_ICE_STAIRS = register(
            "cut_packed_ice_stairs",
            settings -> new StairsBlock(
                    CUT_PACKED_ICE.getDefaultState(),
                    settings
            ),
            AbstractBlock.Settings.copy(CUT_PACKED_ICE)
    );

    public static final Block CUT_PACKED_ICE_SLAB = register(
            "cut_packed_ice_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(CUT_PACKED_ICE)
    );

    public static final Block CUT_PACKED_ICE_WALL = register(
            "cut_packed_ice_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(CUT_PACKED_ICE)
    );

    public static final Block CUT_BLUE_ICE = register(
            "cut_blue_ice",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .slipperiness(0.989f)
                    .strength(2.8f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GLASS)
    );

    public static final Block CUT_BLUE_ICE_STAIRS = register(
            "cut_blue_ice_stairs",
            settings -> new StairsBlock(CUT_BLUE_ICE.getDefaultState(), settings),
            AbstractBlock.Settings.copy(CUT_BLUE_ICE)
    );

    public static final Block CUT_BLUE_ICE_SLAB = register(
            "cut_blue_ice_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(CUT_BLUE_ICE)
    );

    public static final Block CUT_BLUE_ICE_WALL = register(
            "cut_blue_ice_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(CUT_BLUE_ICE)
    );

    public static final Block ICY_TRIAL_SPAWNER = register(
            "icy_trial_spawner",
            TrialSpawnerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_AQUA)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .luminance(state -> state.get(TrialSpawnerBlock.TRIAL_SPAWNER_STATE).getLuminance())
                    .strength(50.0f)
                    .sounds(BlockSoundGroup.TRIAL_SPAWNER)
                    .blockVision(Blocks::never)
                    .nonOpaque()
    );

    public static final Block ICY_VAULT = register(
            "icy_vault",
            VaultBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_AQUA)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.VAULT)
                    .luminance(state -> state.get(VaultBlock.VAULT_STATE).getLuminance() * 5 / 6)
                    .strength(50.0f)
                    .blockVision(Blocks::never)
    );

    public static final Block BRITTLE_ICE = register(
            "brittle_ice",
            BrittleIceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .slipperiness(0.98f)
                    .ticksRandomly()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .solidBlock(Blocks::never)
    );

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful blocks");
        DispenserBlock.registerProjectileBehavior(FItems.GLACIAL_ARROW);
        DispenserBlock.registerProjectileBehavior(FItems.PACKED_SNOWBALL);
        UseBlockCallback.EVENT.register(new CampfireUseEventListener());
        BlockEntityType.TRIAL_SPAWNER.addSupportedBlock(ICY_TRIAL_SPAWNER);
        BlockEntityType.VAULT.addSupportedBlock(ICY_VAULT);
    }

    private static Block register(
            String id,
            Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings
    ) {
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, Frostiful.id(id));
        Block block = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.BLOCK, key, block);
    }

    private static AbstractBlock.Settings copy(Block block, boolean copyTranslationKey) {
        AbstractBlock.Settings settings = AbstractBlock.Settings.create().lootTable(block.getLootTableKey());
        if (copyTranslationKey) {
            settings = settings.overrideTranslationKey(block.getTranslationKey());
        }
        return settings;
    }

    private FBlocks() {

    }
}
