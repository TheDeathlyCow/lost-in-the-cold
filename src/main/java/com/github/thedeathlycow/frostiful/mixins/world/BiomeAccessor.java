package com.github.thedeathlycow.frostiful.mixins.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Biome.class)
public interface BiomeAccessor {
    @Invoker("getTemperature")
    float frostiful$getTemperature(BlockPos pos);
}