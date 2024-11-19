package com.github.thedeathlycow.frostiful.test.damage;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.AfterBatch;
import net.minecraft.test.BeforeBatch;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class HotFloorTests {

    @BeforeBatch(batchId = "hotFloorTests")
    public void mockController(ServerWorld serverWorld) {
        EnvironmentManager.INSTANCE.addController(
                controller ->
                        new EnvironmentControllerDecorator(controller) {
                            @Override
                            public int getLocalTemperatureChange(World world, BlockPos pos) {
                                return 0;
                            }

                            @Override
                            public int getHeatAtLocation(World world, BlockPos pos) {
                                return 0;
                            }

                            @Override
                            public int getTemperatureEffectsChange(LivingEntity entity) {
                                return 0;
                            }
                        }
        );
    }

    @AfterBatch(batchId = "hotFloorTests")
    public void resetController(ServerWorld serverWorld) {
        EnvironmentManager.INSTANCE.peelController();
    }

    @GameTest(batchId = "hotFloorTests", templateName = "frostiful-test:sun_lichen_tests.platform")
    public void when_villager_standing_on_magma_block_then_villager_is_heated(TestContext context) {
        int temperatureChange =  Frostiful.getConfig().freezingConfig.getHeatFromHotFloor();

        final BlockPos pos = new BlockPos(1, 3, 1);
        final int initialFreeze = -1000;

        final VillagerEntity entity = context.spawnMob(EntityType.VILLAGER, pos);

        entity.thermoo$setTemperature(initialFreeze);
        context.expectEntityWithData(pos, EntityType.VILLAGER, TemperatureAware::thermoo$getTemperature, initialFreeze);

        context.setBlockState(pos.down(), Blocks.MAGMA_BLOCK.getDefaultState());
        context.waitAndRun(
                5, () -> {
                    context.addInstantFinalTask(() -> {

                                context.assertTrue(
                                        entity.thermoo$getTemperature() > initialFreeze,
                                        String.format(
                                                "Villager temperature of %d is not greater than %d",
                                                entity.thermoo$getTemperature(),
                                                initialFreeze
                                        )
                                );
                            }
                    );
                }
        );
    }

}
