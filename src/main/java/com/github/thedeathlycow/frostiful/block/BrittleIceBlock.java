package com.github.thedeathlycow.frostiful.block;

import com.github.thedeathlycow.frostiful.registry.FBlockProperties;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TranslucentBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BrittleIceBlock extends TranslucentBlock {

    public static final MapCodec<BrittleIceBlock> CODEC = createCodec(BrittleIceBlock::new);

    public static final IntProperty CRACKING = FBlockProperties.CRACKING;

    public BrittleIceBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(CRACKING, 0));
    }

    @Override
    public MapCodec<BrittleIceBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CRACKING);
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
}