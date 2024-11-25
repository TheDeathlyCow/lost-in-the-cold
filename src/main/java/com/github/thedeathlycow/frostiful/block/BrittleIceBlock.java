package com.github.thedeathlycow.frostiful.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BrittleIceBlock extends Block {
    public static final EnumProperty<BrittleIceState> CRACKING = BrittleIceState.CRACKING;

    public BrittleIceBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
    }
}