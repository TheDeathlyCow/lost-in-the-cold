package com.github.thedeathlycow.frostiful.block;

import com.github.thedeathlycow.frostiful.registry.FBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BrittleIceBlock extends Block {
    public static final IntProperty CRACKING = FBlockProperties.CRACKING;

    public BrittleIceBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
    }
}