package com.github.thedeathlycow.frostiful.block;

import com.github.thedeathlycow.frostiful.registry.FBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BrittleIceBlock extends Block {
    public static final IntProperty CRACKING = FBlockProperties.CRACKING;

    public static final int MAX_CRACKING = FBlockProperties.MAX_CRACKING;

    private static final int MIN_CRACK_DELAY = 20;

    private static final int MAX_CRACK_DELAY = 60;

    public BrittleIceBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(CRACKING, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CRACKING);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient()) {
            world.scheduleBlockTick(pos, this, getCrackDelay(world.getRandom()));
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.isOf(this)) {
            return;
        }
        int nextCrackingLevel = state.get(CRACKING) + 1;
        if (nextCrackingLevel <= MAX_CRACKING) {
            world.setBlockState(pos, state.with(CRACKING, nextCrackingLevel));
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS);
            world.scheduleBlockTick(pos, this, getCrackDelay(world.getRandom()));
        } else {
            world.breakBlock(pos, false, null);
        }
    }

    private static int getCrackDelay(Random random) {
        return MathHelper.nextInt(random, MIN_CRACK_DELAY, MAX_CRACK_DELAY);
    }
}