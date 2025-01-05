package com.github.thedeathlycow.frostiful.test.sun_lichen;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.registry.FBlocks;
import com.github.thedeathlycow.frostiful.test.FrostifulGameTest;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.AfterBatch;
import net.minecraft.test.BeforeBatch;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Function;

@SuppressWarnings("unused")
public final class SunLichenCollisionTests implements FabricGameTest {

    @BeforeBatch(batchId = "sunLichenCollision")
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
                        }
        );
    }

    @AfterBatch(batchId = "sunLichenCollision")
    public void resetController(ServerWorld serverWorld) {
        EnvironmentManager.INSTANCE.peelController();
    }


    @GameTest(batchId = "sunLichenCollision", templateName = "frostiful-test:sun_lichen_tests.platform")
    public void cool_lichen_does_not_damage(TestContext context) {
        final BlockPos pos = new BlockPos(1, 2, 1);

        final MobEntity entity = context.spawnMob(EntityType.VILLAGER, pos);
        context.expectEntityWithData(pos, EntityType.VILLAGER, LivingEntity::getHealth, entity.getMaxHealth());

        context.setBlockState(pos, FBlocks.HOT_SUN_LICHEN.getDefaultState());
        context.expectEntityWithDataEnd(pos, EntityType.VILLAGER, LivingEntity::getHealth, entity.getMaxHealth());
    }

    @GameTest(batchId = "sunLichenCollision", templateName = "frostiful-test:sun_lichen_tests.platform")
    public void hot_lichen_damages(TestContext context) {
        final BlockPos pos = new BlockPos(1, 2, 1);

        final MobEntity entity = context.spawnMob(EntityType.VILLAGER, pos);

        context.expectEntityWithData(pos, EntityType.VILLAGER, LivingEntity::getHealth, entity.getMaxHealth());

        context.setBlockState(pos, FBlocks.HOT_SUN_LICHEN.getDefaultState());
        context.expectEntityWithDataEnd(pos, EntityType.VILLAGER, LivingEntity::getHealth, entity.getMaxHealth() - 1.0f);
    }

    @GameTest(batchId = "sunLichenCollision", templateName = "frostiful-test:sun_lichen_tests.platform")
    public void cold_lichen_does_not_warm(TestContext context) {
        final BlockPos pos = new BlockPos(1, 2, 1);

        final MobEntity entity = context.spawnMob(EntityType.VILLAGER, pos);
        final Function<VillagerEntity, Integer> frostGetter = TemperatureAware::thermoo$getTemperature;

        entity.thermoo$setTemperature(0);
        context.expectEntityWithData(pos, EntityType.VILLAGER, frostGetter, 0);

        context.setBlockState(pos, FBlocks.COLD_SUN_LICHEN.getDefaultState());

        context.expectEntityWithDataEnd(pos, EntityType.VILLAGER, frostGetter, 0);
    }

    @GameTest(batchId = "sunLichenCollision", templateName = "frostiful-test:sun_lichen_tests.platform")
    public void sun_lichen_does_not_overheat(TestContext context) {
        final BlockPos pos = new BlockPos(1, 2, 1);

        final MobEntity entity = context.spawnMob(EntityType.VILLAGER, pos);
        final Function<VillagerEntity, Integer> frostGetter = TemperatureAware::thermoo$getTemperature;

        int freezeAmount = -500;
        entity.thermoo$setTemperature(freezeAmount);
        context.expectEntityWithData(pos, EntityType.VILLAGER, frostGetter, freezeAmount);

        context.setBlockState(pos, FBlocks.HOT_SUN_LICHEN.getDefaultState());

        context.expectEntityWithDataEnd(pos, EntityType.VILLAGER, frostGetter, 0);
    }

    @GameTest(batchId = "sunLichenCollision", templateName = "frostiful-test:sun_lichen_tests.platform")
    public void warm_villager_is_burned_by_hot_sun_lichen(TestContext context) {
        final BlockPos pos = new BlockPos(1, 2, 1);
        int temperature = 500;
        MobEntity entity = setupWarmVillagerTest(context, pos, temperature);

        context.setBlockState(pos, FBlocks.HOT_SUN_LICHEN.getDefaultState());

        context.waitAndRun(1L, () -> {
            context.expectEntityWithData(pos, EntityType.VILLAGER, TemperatureAware::thermoo$getTemperature, temperature);
            context.expectEntityWithData(pos, EntityType.VILLAGER, Entity::isOnFire, true);

            context.complete();
        });
    }

    @GameTest(batchId = "sunLichenCollision", templateName = "frostiful-test:sun_lichen_tests.platform")
    public void warm_villager_is_not_burned_by_cool_sun_lichen(TestContext context) {
        final BlockPos pos = new BlockPos(1, 2, 1);
        int temperature = 500;
        MobEntity entity = setupWarmVillagerTest(context, pos, temperature);

        context.setBlockState(pos, FBlocks.COOL_SUN_LICHEN.getDefaultState());

        context.waitAndRun(1L, () -> {
            context.expectEntityWithData(pos, EntityType.VILLAGER, TemperatureAware::thermoo$getTemperature, temperature);
            context.expectEntityWithData(pos, EntityType.VILLAGER, Entity::isOnFire, false);

            context.complete();
        });
    }

    @GameTest(batchId = "sunLichenCollision", templateName = "frostiful-test:sun_lichen_tests.platform")
    public void hot_lichen_warms(TestContext context) {
        expectWarmLichenWarmsVillager(context, FBlocks.HOT_SUN_LICHEN);
    }

    @GameTest(batchId = "sunLichenCollision", templateName = "frostiful-test:sun_lichen_tests.platform")
    public void warm_lichen_warms(TestContext context) {
        expectWarmLichenWarmsVillager(context, FBlocks.WARM_SUN_LICHEN);
    }

    @GameTest(batchId = "sunLichenCollision", templateName = "frostiful-test:sun_lichen_tests.platform")
    public void cool_lichen_warms(TestContext context) {
        expectWarmLichenWarmsVillager(context, FBlocks.COOL_SUN_LICHEN);
    }

    private static void expectWarmLichenWarmsVillager(TestContext context, Block warmLichen) {
        final BlockPos pos = new BlockPos(1, 2, 1);

        final MobEntity entity = context.spawnMob(EntityType.VILLAGER, pos);
        final Function<VillagerEntity, Integer> frostGetter = TemperatureAware::thermoo$getTemperature;

        int initialTemperature = -2000;
        entity.thermoo$setTemperature(initialTemperature);
        context.expectEntityWithData(pos, EntityType.VILLAGER, frostGetter, initialTemperature);

        context.setBlockState(pos, warmLichen.getDefaultState());

        context.waitAndRun(10, () -> {
            context.addInstantFinalTask(() -> {
                context.assertTrue(
                        entity.thermoo$getTemperature() > -2000,
                        String.format(
                                "Villager temperature of %d is not greater than %d",
                                entity.thermoo$getTemperature(),
                                initialTemperature
                        )
                );
            });
        });
    }

    private static MobEntity setupWarmVillagerTest(TestContext context, BlockPos pos, int temperature) {
        final MobEntity entity = context.spawnMob(EntityType.VILLAGER, pos);

        EntityAttributeInstance maxTemperature = entity.getAttributeInstance(ThermooAttributes.MAX_TEMPERATURE);
        context.assertFalse(maxTemperature == null, "Villager does not have a max temperature attribute");
        maxTemperature.addTemporaryModifier(
                new EntityAttributeModifier(
                        FrostifulGameTest.id("max_temperature"),
                        40,
                        EntityAttributeModifier.Operation.ADD_VALUE
                )
        );

        entity.thermoo$setTemperature(temperature);
        context.expectEntityWithData(pos, EntityType.VILLAGER, TemperatureAware::thermoo$getTemperature, temperature);

        return entity;
    }
}
