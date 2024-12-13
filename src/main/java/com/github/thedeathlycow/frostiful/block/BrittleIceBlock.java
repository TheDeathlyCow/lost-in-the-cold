package com.github.thedeathlycow.frostiful.block;

import com.github.thedeathlycow.frostiful.registry.FBlockProperties;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TranslucentBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BrittleIceBlock extends TranslucentBlock {

    public static final MapCodec<BrittleIceBlock> CODEC = createCodec(BrittleIceBlock::new);

    public static final IntProperty CRACKING = FBlockProperties.CRACKING;

    public static final BooleanProperty FROZEN = FBlockProperties.FROZEN;

    public BrittleIceBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.getDefaultState()
                        .with(CRACKING, 0)
                        .with(FROZEN, false)
        );
    }

    @Override
    public MapCodec<BrittleIceBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CRACKING, FROZEN);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient() && BrittleIce.canCrackIce(entity)) {
            world.scheduleBlockTick(pos, this, BrittleIce.getCrackDelay(world.getRandom()));
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.isOf(this)) {
            return;
        }
        BrittleIce.crack(this, state, world, pos, random);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState base = super.getPlacementState(ctx);

        if (base == null) {
            return null;
        }

        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return base.with(FROZEN, fluidState.isOf(Fluids.WATER));
    }
}