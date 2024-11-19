package com.github.thedeathlycow.frostiful.test.frostologer;

import com.github.thedeathlycow.frostiful.entity.FrostologerEntity;
import com.github.thedeathlycow.frostiful.registry.FBlocks;
import com.github.thedeathlycow.frostiful.registry.FEntityTypes;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class FrostologerDestroyHeatSourcesTests {
    //region torch tests
    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void ground_torch_is_frozen(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.TORCH.getDefaultState(), FBlocks.FROZEN_TORCH);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void wall_torch_is_frozen(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.WALL_TORCH.getDefaultState(), FBlocks.FROZEN_WALL_TORCH);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void ground_redstone_torch_is_frozen(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.REDSTONE_TORCH.getDefaultState(), FBlocks.FROZEN_TORCH);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void wall_redstone_torch_is_frozen(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.REDSTONE_WALL_TORCH.getDefaultState(), FBlocks.FROZEN_WALL_TORCH);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void ground_soul_torch_is_frozen(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.SOUL_TORCH.getDefaultState(), FBlocks.FROZEN_TORCH);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void wall_soul_torch_is_frozen(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.SOUL_WALL_TORCH.getDefaultState(), FBlocks.FROZEN_WALL_TORCH);
    }
    //endregion

    //region lava tests

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void still_lava_becomes_obsidian(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.LAVA.getDefaultState(), Blocks.OBSIDIAN);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void flowing_lava_becomes_air(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.LAVA.getDefaultState()
                        .with(FluidBlock.LEVEL, 10),
                Blocks.AIR
        );
    }

    //endregion

    //region full block tests

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void glowstone_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.GLOWSTONE.getDefaultState(), Blocks.ICE);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void shroomlight_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.SHROOMLIGHT.getDefaultState(), Blocks.ICE);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void sea_lantern_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.SEA_LANTERN.getDefaultState(), Blocks.ICE);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void redstone_lamp_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.REDSTONE_LAMP.getDefaultState()
                        .with(RedstoneLampBlock.LIT, true),
                Blocks.ICE
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void furnace_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.FURNACE.getDefaultState()
                        .with(FurnaceBlock.LIT, true),
                Blocks.ICE
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void respawn_anchor_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.RESPAWN_ANCHOR.getDefaultState()
                        .with(RespawnAnchorBlock.CHARGES, 4),
                Blocks.ICE
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void jack_o_lantern_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.JACK_O_LANTERN.getDefaultState(),
                Blocks.ICE
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void redstone_ore_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.REDSTONE_ORE.getDefaultState()
                        .with(RedstoneOreBlock.LIT, true),
                Blocks.ICE
        );
    }

    //endregion

    //region protected blocks tests

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void nether_portal_is_unaffected_by_frostologer(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.NETHER_PORTAL.getDefaultState(), Blocks.NETHER_PORTAL);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void end_portal_is_unaffected_by_frostologer(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.END_PORTAL.getDefaultState(), Blocks.END_PORTAL);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void end_gateway_is_unaffected_by_frostologer(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.END_GATEWAY.getDefaultState(), Blocks.END_GATEWAY);
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void beacon_is_unaffected_by_frostologer(TestContext context) {
        runDestroyHeatSourceTest(context, Blocks.BEACON.getDefaultState(), Blocks.BEACON);
    }

    //endregion

    //region water logged tests

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void waterlogged_sea_pickle_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.SEA_PICKLE.getDefaultState()
                        .with(SeaPickleBlock.WATERLOGGED, true)
                        .with(SeaPickleBlock.PICKLES, 4),
                Blocks.ICE
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void waterlogged_ender_chest_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.ENDER_CHEST.getDefaultState()
                        .with(EnderChestBlock.WATERLOGGED, true),
                Blocks.ICE
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void waterlogged_lantern_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.LANTERN.getDefaultState()
                        .with(EnderChestBlock.WATERLOGGED, true),
                Blocks.ICE
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void waterlogged_amethyst_cluster_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.AMETHYST_CLUSTER.getDefaultState()
                        .with(AmethystClusterBlock.WATERLOGGED, true),
                Blocks.ICE
        );
    }

    // yes this is technically a possible block state
    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void waterlogged_lit_candle_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.CANDLE.getDefaultState()
                        .with(CandleBlock.WATERLOGGED, true)
                        .with(CandleBlock.LIT, true),
                Blocks.ICE
        );
    }

    // yes this is technically a possible block state
    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void waterlogged_lit_campfire_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.CAMPFIRE.getDefaultState()
                        .with(CampfireBlock.WATERLOGGED, true)
                        .with(CampfireBlock.LIT, true),
                Blocks.ICE
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void waterlogged_hot_sun_lichen_becomes_ice(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                FBlocks.HOT_SUN_LICHEN.getDefaultState()
                        .with(AmethystClusterBlock.WATERLOGGED, true),
                Blocks.ICE
        );
    }

    //endregion

    //region small blocks

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void end_rod_becomes_air(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.END_ROD.getDefaultState(),
                Blocks.AIR
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void ender_chest_becomes_air(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.ENDER_CHEST.getDefaultState(),
                Blocks.AIR
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void lantern_becomes_air(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.LANTERN.getDefaultState(),
                Blocks.AIR
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void amethyst_cluster_becomes_air(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.AMETHYST_CLUSTER.getDefaultState(),
                Blocks.AIR
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void lit_candle_becomes_air(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.CANDLE.getDefaultState()
                        .with(CandleBlock.LIT, true),
                Blocks.AIR
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void lit_campfire_becomes_air(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                Blocks.CAMPFIRE.getDefaultState()
                        .with(CampfireBlock.LIT, true),
                Blocks.AIR
        );
    }

    @GameTest(templateName = "frostiful-test:frostologer_heat_source_test_template")
    public void hot_sun_lichen_becomes_air(TestContext context) {
        runDestroyHeatSourceTest(
                context,
                FBlocks.HOT_SUN_LICHEN.getDefaultState(),
                Blocks.AIR
        );
    }

    //endregion

    private static void runDestroyHeatSourceTest(TestContext context, BlockState toPlace, Block blockAtEnd) {
        ServerWorld serverWorld = context.getWorld();
        BlockPos pos = new BlockPos(1, 2, 1);

        serverWorld.setBlockState(pos, toPlace);

        FrostologerEntity frostologer = context.spawnEntity(FEntityTypes.FROSTOLOGER, pos.add(1, 0, 1));
        frostologer.setInvulnerable(true);
        frostologer.setAiDisabled(true);
        frostologer.destroyHeatSource(serverWorld, toPlace, context.getAbsolutePos(pos));

        context.expectBlockAtEnd(blockAtEnd, pos);
    }

}
