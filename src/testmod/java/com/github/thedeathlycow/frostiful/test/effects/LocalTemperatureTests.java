package com.github.thedeathlycow.frostiful.test.effects;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class LocalTemperatureTests {

    @GameTest(templateName = "frostiful-test:effects.local_temperature")
    public void villager_is_warmed_by_torch(TestContext context) {
        BlockPos pos = new BlockPos(1, 2, 1);
        int temperature = -2000;

        EnvironmentController controller = EnvironmentManager.INSTANCE.getController();

        VillagerEntity villager = context.spawnMob(EntityType.VILLAGER, pos);
        villager.thermoo$setTemperature(temperature);

        context.expectEntityWithData(
                pos,
                EntityType.VILLAGER,
                VillagerEntity::thermoo$getTemperature,
                temperature
        );

        context.expectBlock(Blocks.TORCH, pos);

        context.waitAndRun(
                5L,
                () -> {
                    context.addInstantFinalTask(() -> context.assertTrue(
                            villager.thermoo$getTemperature() > temperature,
                            String.format(
                                    "Villager temperature of %d is not greater than %d",
                                    villager.thermoo$getTemperature(),
                                    temperature
                            )
                    ));
                }
        );
    }

    @GameTest(templateName = "frostiful-test:effects.local_temperature")
    public void villager_in_boat_is_warmed_by_torch(TestContext context) {
        BlockPos pos = new BlockPos(1, 2, 1);
        int temperature = -2000;

        EnvironmentController controller = EnvironmentManager.INSTANCE.getController();

        VillagerEntity villager = context.spawnMob(EntityType.VILLAGER, pos);
        Entity boat = context.spawnEntity(EntityType.BOAT, pos);
        villager.startRiding(boat, true);

        villager.thermoo$setTemperature(temperature);

        context.expectEntityWithData(
                pos,
                EntityType.VILLAGER,
                VillagerEntity::thermoo$getTemperature,
                temperature
        );

        context.expectEntityWithData(
                pos,
                EntityType.VILLAGER,
                e -> {
                    Entity vehicle = e.getVehicle();
                    context.assertFalse(vehicle == null, "Villager must have a vehicle");
                    return vehicle.getId();
                },
                boat.getId()
        );

        context.expectBlock(Blocks.TORCH, pos);

        context.waitAndRun(
                5L,
                () -> {
                    context.addInstantFinalTask(() -> context.assertTrue(
                            villager.thermoo$getTemperature() > temperature,
                            String.format(
                                    "Villager temperature of %d is not greater than %d",
                                    villager.thermoo$getTemperature(),
                                    temperature
                            )
                    ));
                }
        );
    }


    @GameTest(templateName = "frostiful-test:effects.local_temperature")
    public void villager_is_not_warmed(TestContext context) {
        BlockPos pos = new BlockPos(1, 2, 1);
        int temperature = -2000;

        EnvironmentController controller = EnvironmentManager.INSTANCE.getController();

        VillagerEntity villager = context.spawnMob(EntityType.VILLAGER, pos);
        villager.thermoo$setTemperature(temperature);

        context.expectEntityWithData(
                pos,
                EntityType.VILLAGER,
                VillagerEntity::thermoo$getTemperature,
                temperature
        );

        context.setBlockState(pos, Blocks.AIR.getDefaultState());
        context.dontExpectBlock(Blocks.TORCH, pos);

        context.waitAndRun(
                5L,
                () -> {
                    context.addInstantFinalTask(() -> {
                                int villagerTemperature = villager.thermoo$getTemperature();
                                context.assertTrue(
                                        villagerTemperature == temperature,
                                        String.format(
                                                "Villager temperature of %d does not match expected %d",
                                                villagerTemperature,
                                                temperature
                                        )
                                );
                            }
                    );
                }
        );
    }

}
