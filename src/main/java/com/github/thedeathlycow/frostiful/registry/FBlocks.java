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
import net.minecraft.sound.BlockSoundGroup;

public class FBlocks {

    public static final Block ICICLE = register(
            "icicle",
            new IcicleBlock(
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
            )
    );

    public static final Block COLD_SUN_LICHEN = register(
            "cold_sun_lichen",
            new SunLichenBlock(
                    SunLichenBlock.COLD_LEVEL,
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
            )
    );
    public static final Block COOL_SUN_LICHEN = register(
            "cool_sun_lichen",
            new SunLichenBlock(
                    SunLichenBlock.COOL_LEVEL,
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
            )
    );
    public static final Block WARM_SUN_LICHEN = register(
            "warm_sun_lichen",
            new SunLichenBlock(
                    SunLichenBlock.WARM_LEVEL,
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
            )
    );
    public static final Block HOT_SUN_LICHEN = register(
            "hot_sun_lichen",
            new SunLichenBlock(
                    SunLichenBlock.HOT_LEVEL,
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
            )
    );

    public static final Block FROZEN_TORCH = register(
            "frozen_torch",
            new FrozenTorchBlock(
                    AbstractBlock.Settings.create()
                            .noCollision()
                            .breakInstantly()
                            .pistonBehavior(PistonBehavior.DESTROY)
                            .sounds(BlockSoundGroup.WOOD)
            )
    );

    public static final Block FROZEN_WALL_TORCH = register(
            "frozen_wall_torch",
            new FrozenWallTorchBlock(
                    AbstractBlock.Settings.copy(FROZEN_TORCH)
                            .dropsLike(FROZEN_TORCH)
            )
    );

    public static final Block PACKED_SNOW = register(
            "packed_snow",
            new PackedSnowBlock(
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.WHITE)
                            .replaceable()
                            .notSolid()
                            .strength(1.2f, 3.0f)
                            .requiresTool()
                            .sounds(FBlockSoundGroups.PACKED_SNOW)
                            .ticksRandomly()
                            .blockVision((state, world, pos) -> {
                                return state.get(PackedSnowBlock.LAYERS) >= PackedSnowBlock.MAX_LAYERS;
                            })
                            .pistonBehavior(PistonBehavior.DESTROY)
            )
    );

    public static final Block PACKED_SNOW_BLOCK = register(
            "packed_snow_block",
            new Block(
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.WHITE_GRAY)
                            .requiresTool()
                            .strength(1.5f, 6.0f)
                            .sounds(FBlockSoundGroups.PACKED_SNOW)
            )
    );

    public static final Block PACKED_SNOW_BRICKS = register(
            "packed_snow_bricks",
            new Block(
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.WHITE_GRAY)
                            .requiresTool()
                            .strength(1.5f, 6.0f)
                            .sounds(FBlockSoundGroups.PACKED_SNOW)
            )
    );

    public static final Block PACKED_SNOW_BRICK_STAIRS = register(
            "packed_snow_brick_stairs",
            new StairsBlock(
                    PACKED_SNOW_BRICKS.getDefaultState(),
                    AbstractBlock.Settings.copy(PACKED_SNOW_BRICKS)
            )
    );

    public static final Block PACKED_SNOW_BRICK_SLAB = register(
            "packed_snow_brick_slab",
            new SlabBlock(AbstractBlock.Settings.copy(PACKED_SNOW_BRICKS))
    );

    public static final Block PACKED_SNOW_BRICK_WALL = register(
            "packed_snow_brick_wall",
            new WallBlock(AbstractBlock.Settings.copy(PACKED_SNOW_BRICKS))
    );

    public static final Block ICE_PANE = register(
            "ice_pane",
            new IcePaneBlock(
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.PALE_PURPLE)
                            .strength(0.5f)
                            .ticksRandomly()
                            .slipperiness(0.98f)
                            .sounds(BlockSoundGroup.GLASS)
                            .nonOpaque()
                            .solidBlock((state, world, pos) -> false)
            )
    );

    public static final Block CUT_PACKED_ICE = register(
            "cut_packed_ice",
            new Block(
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.PALE_PURPLE)
                            .instrument(NoteBlockInstrument.CHIME)
                            .slipperiness(0.98f)
                            .strength(0.75f)
                            .requiresTool()
                            .sounds(BlockSoundGroup.GLASS)
            )
    );

    public static final Block CUT_PACKED_ICE_STAIRS = register(
            "cut_packed_ice_stairs",
            new StairsBlock(
                    CUT_PACKED_ICE.getDefaultState(),
                    AbstractBlock.Settings.copy(CUT_PACKED_ICE)
            )
    );

    public static final Block CUT_PACKED_ICE_SLAB = register(
            "cut_packed_ice_slab",
            new SlabBlock(AbstractBlock.Settings.copy(CUT_PACKED_ICE))
    );

    public static final Block CUT_PACKED_ICE_WALL = register(
            "cut_packed_ice_wall",
            new WallBlock(AbstractBlock.Settings.copy(CUT_PACKED_ICE))
    );

    public static final Block CUT_BLUE_ICE = register(
            "cut_blue_ice",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .slipperiness(0.989f)
                    .strength(2.8f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GLASS)
            )
    );

    public static final Block CUT_BLUE_ICE_STAIRS = register(
            "cut_blue_ice_stairs",
            new StairsBlock(
                    CUT_BLUE_ICE.getDefaultState(),
                    AbstractBlock.Settings.copy(CUT_BLUE_ICE)
            )
    );

    public static final Block CUT_BLUE_ICE_SLAB = register(
            "cut_blue_ice_slab",
            new SlabBlock(AbstractBlock.Settings.copy(CUT_BLUE_ICE))
    );

    public static final Block CUT_BLUE_ICE_WALL = register(
            "cut_blue_ice_wall",
            new WallBlock(AbstractBlock.Settings.copy(CUT_BLUE_ICE))
    );

    public static final Block ICY_TRIAL_SPAWNER = register(
            "icy_trial_spawner",
            new TrialSpawnerBlock(
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.DARK_AQUA)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .luminance(state -> state.get(TrialSpawnerBlock.TRIAL_SPAWNER_STATE).getLuminance())
                            .strength(50.0f)
                            .sounds(BlockSoundGroup.TRIAL_SPAWNER)
                            .blockVision(Blocks::never)
                            .nonOpaque()
            )
    );
    public static final Block ICY_VAULT = register(
            "icy_vault",
            new VaultBlock(
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.DARK_AQUA)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .nonOpaque()
                            .sounds(BlockSoundGroup.VAULT)
                            .luminance(state -> state.get(VaultBlock.VAULT_STATE).getLuminance() * 5 / 6)
                            .strength(50.0f)
                            .blockVision(Blocks::never)
            )
    );

    public static final Block BRITTLE_ICE = register(
            "brittle_ice",
            new BrittleIceBlock(
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.PALE_PURPLE)
                            .slipperiness(0.98f)
                            .ticksRandomly()
                            .strength(0.5f)
                            .sounds(BlockSoundGroup.GLASS)
                            .nonOpaque()
                            .solidBlock(Blocks::never)
            )
    );

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful blocks");
        DispenserBlock.registerProjectileBehavior(FItems.GLACIAL_ARROW);
        DispenserBlock.registerProjectileBehavior(FItems.PACKED_SNOWBALL);
        UseBlockCallback.EVENT.register(new CampfireUseEventListener());
        BlockEntityType.TRIAL_SPAWNER.addSupportedBlock(ICY_TRIAL_SPAWNER);
        BlockEntityType.VAULT.addSupportedBlock(ICY_VAULT);
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registries.BLOCK, Frostiful.id(id), block);
    }

    private FBlocks() {

    }

}
