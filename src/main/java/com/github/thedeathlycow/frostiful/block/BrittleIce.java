package com.github.thedeathlycow.frostiful.block;

import com.github.thedeathlycow.frostiful.registry.FBlockProperties;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import com.github.thedeathlycow.frostiful.registry.tag.FEntityTypeTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public final class BrittleIce {

    private static final int MIN_CRACK_DELAY = 5;

    private static final int MAX_CRACK_DELAY = 10;

    public static final int MAX_CRACKING = FBlockProperties.MAX_CRACKING;

    public static boolean canCrackIce(Entity entity) {
        return !entity.getType().isIn(FEntityTypeTags.DOES_NOT_BREAK_BRITTLE_ICE);
    }

    public static void crack(Block block, BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int nextCrackingLevel = state.get(FBlockProperties.CRACKING) + 1;
        if (nextCrackingLevel <= MAX_CRACKING) {
            world.setBlockState(pos, state.with(FBlockProperties.CRACKING, nextCrackingLevel));
            world.playSound(null, pos, FSoundEvents.BLOCK_BRITTLE_ICE_CRACK, SoundCategory.BLOCKS);
            world.scheduleBlockTick(pos, block, getCrackDelay(random));
        } else {
            world.breakBlock(pos, false, null);
            if (state.get(FBlockProperties.FROZEN)) {
                world.setBlockState(pos, Blocks.WATER.getDefaultState());
            }
        }
    }

    public static int getCrackDelay(Random random) {
        return MathHelper.nextInt(random, MIN_CRACK_DELAY, MAX_CRACK_DELAY);
    }

    private BrittleIce() {

    }
}