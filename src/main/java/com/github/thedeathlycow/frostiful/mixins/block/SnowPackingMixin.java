package com.github.thedeathlycow.frostiful.mixins.block;

import com.github.thedeathlycow.frostiful.block.FBlocks;
import com.github.thedeathlycow.frostiful.block.PackedSnowBlock;
import com.github.thedeathlycow.frostiful.tag.entitytype.FEntityTypeTags;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)
public abstract class SnowPackingMixin {

    @Inject(
            method = "onEntityCollision",
            at = @At("TAIL")
    )
    private void smushSnowWhenSteppedOnByHeavyEntity(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {

        boolean maySmushSnow = state.getBlock() == Blocks.SNOW
                && entity.getType().isIn(FEntityTypeTags.HEAVY_ENTITY_TYPES)
                && !world.isClient
                && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);

        if (maySmushSnow) {

            int layers = state.get(SnowBlock.LAYERS);

            if (layers < 1) {
                return;
            }

            BlockState packedSnow = FBlocks.PACKED_SNOW.getDefaultState()
                    .with(PackedSnowBlock.LAYERS, layers);

            world.setBlockState(pos, packedSnow);
            entity.playSound(SoundEvents.BLOCK_SNOW_PLACE, 1.0f, 1.0f);
        }
    }

}
