package com.github.thedeathlycow.frostiful.server.world.gen.feature;

import com.github.thedeathlycow.frostiful.mixins.world.BiomeAccessor;
import com.github.thedeathlycow.frostiful.registry.FBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class FreezeBrittleFeature extends Feature<DefaultFeatureConfig> {
    public FreezeBrittleFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();

        BlockPos.Mutable floor = new BlockPos.Mutable();
        BlockPos.Mutable belowFloor = new BlockPos.Mutable();

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int x = origin.getX() + dx;
                int z = origin.getZ() + dz;
                int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
                floor.set(x, y, z);
                belowFloor.set(floor).move(Direction.DOWN, 1);

                Biome biome = world.getBiome(belowFloor).value();
                float temperature = ((BiomeAccessor) (Object) biome).frostiful$getTemperature(belowFloor);

                if (temperature <= 0.15f && temperature >= 0.14f) {
                    world.setBlockState(belowFloor, FBlocks.BRITTLE_ICE.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }

        return true;
    }
}