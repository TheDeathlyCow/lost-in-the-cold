package com.github.thedeathlycow.frostiful.survival.wind;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.block.FrozenTorchBlock;
import com.github.thedeathlycow.frostiful.config.group.FreezingConfigGroup;
import com.github.thedeathlycow.frostiful.registry.tag.FBiomeTags;
import com.github.thedeathlycow.frostiful.registry.tag.FBlockTags;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;

public final class WindManager {

    public static final WindManager INSTANCE = new WindManager();

    private int windSpawnCount = 0;

    private WindManager() {

    }

    public void resetWindSpawnCount() {
        this.windSpawnCount = 0;
    }

    public void trySpawnFreezingWind(World world, WorldChunk chunk) {
        FreezingConfigGroup config = Frostiful.getConfig().freezingConfig;

        if (!world.getDimension().natural() || this.windSpawnCount >= config.getWindSpawnCapPerSecond()) {
            return;
        }

        int chanceBound = world.isThundering()
                ? config.getWindSpawnRarityThunder()
                : config.getWindSpawnRarity();

        if (world.random.nextInt(chanceBound) != 0) {
            return;
        }

        BlockPos.Mutable spawnPos = new BlockPos.Mutable();
        boolean spawnInAir = this.setSpawnPosition(world, chunk, spawnPos);

        int temperatureChange = EnvironmentManager.INSTANCE.getController().getLocalTemperatureChange(world, spawnPos);
        if (temperatureChange >= 0) {
            return;
        }

        if (spawnInAir && !config.spawnWindInAir()) {
            return;
        }

        RegistryEntry<Biome> biome = world.getBiome(spawnPos);
        if (biome.isIn(FBiomeTags.FREEZING_WIND_NEVER_SPAWNS)) {
            return;
        }

        boolean canSpawnOnGround = (world.isRaining() && biome.isIn(FBiomeTags.FREEZING_WIND_SPAWNS_IN_STORMS))
                || biome.isIn(FBiomeTags.FREEZING_WIND_ALWAYS_SPAWNS);

        WindSpawnStrategy strategy = config.getWindSpawnStrategy().getStrategy();
        if (strategy == null) {
            return;
        }

        if ((canSpawnOnGround || spawnInAir) && strategy.spawn(world, spawnPos, spawnInAir)) {
            this.windSpawnCount++;
        }
    }

    public void extinguishBlock(BlockState state, World world, BlockPos pos, Runnable playSoundCallback) {
        if (!Frostiful.getConfig().freezingConfig.isWindDestroysTorches()) {
            return;
        }

        if (!world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            return;
        }

        if (state.isIn(FBlockTags.FROZEN_TORCHES)) {
            return;
        }

        @Nullable
        BlockState blownOutResult;

        if (state.isIn(FBlockTags.IS_OPEN_FLAME)) {
            blownOutResult = state.getFluidState().getBlockState();
        } else if (
                state.isIn(FBlockTags.HAS_OPEN_FLAME)
                        && state.contains(Properties.LIT)
                        && state.get(Properties.LIT)
        ) {
            blownOutResult = state.with(Properties.LIT, false);
        } else {
            blownOutResult = FrozenTorchBlock.freezeTorch(state);
        }

        if (blownOutResult != null) {
            world.setBlockState(pos, blownOutResult);
            playSoundCallback.run();
        }
    }


    /**
     * Sets the spawn position for the wind. Returns if the spawn was spawned in the air.
     *
     * @param world    World/level access object
     * @param chunk    The chunk of world this is happening in
     * @param blockPos The mutable blockpos to set the spawn position into
     * @return Returns true if the blockpos is an air blockpos
     */
    private boolean setSpawnPosition(World world, WorldChunk chunk, BlockPos.Mutable blockPos) {
        ChunkPos chunkPos = chunk.getPos();
        BlockPos spawnPos = world.getTopPosition(
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                world.getRandomPosInChunk(chunkPos.getStartX(), 0, chunkPos.getStartZ(), 15)
        );
        blockPos.set(spawnPos);

        boolean spawnInAir = world.random.nextBoolean();
        if (spawnInAir) {
            int topY = world.getTopY();
            blockPos.setY(
                    spawnPos.getY() + (int) world.random.nextTriangular(
                            topY,
                            ((double) topY) - spawnPos.getY()
                    )
            );
            return true;
        }

        return false;
    }

}
