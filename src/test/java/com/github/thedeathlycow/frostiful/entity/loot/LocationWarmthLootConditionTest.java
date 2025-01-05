package com.github.thedeathlycow.frostiful.entity.loot;

import com.github.thedeathlycow.frostiful.FrostifulTest;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.concurrent.atomic.AtomicInteger;

class LocationWarmthLootConditionTest {

    LootContext mockContext;

    final AtomicInteger areaTemperature = new AtomicInteger();

    @BeforeAll
    static void setup() {
        FrostifulTest.bootstrap();
    }

    @BeforeEach
    void mockLootContext() {
        mockContext = Mockito.mock(LootContext.class);

        Mockito.when(mockContext.getWorld())
                .thenReturn(null);
        Mockito.when(mockContext.get(LootContextParameters.ORIGIN))
                .thenReturn(Vec3d.ZERO);

        EnvironmentManager.INSTANCE.addController(
                controller -> new EnvironmentControllerDecorator(controller) {
                    @Override
                    public int getHeatAtLocation(World world, BlockPos pos) {
                        return areaTemperature.get();
                    }
                }
        );
    }

    @AfterEach
    void tearDown() {
        EnvironmentManager.INSTANCE.peelController();
    }

    @Test
    void areaTemperature_OutsideBoundary_false() {
        areaTemperature.set(0);
        var condition = new LocationWarmthLootCondition(NumberRange.IntRange.atLeast(10));

        Assertions.assertFalse(condition.test(mockContext));
    }

    @Test
    void areaTemperature_AtBoundary_true() {
        areaTemperature.set(10);
        var condition = new LocationWarmthLootCondition(NumberRange.IntRange.atLeast(10));

        Assertions.assertTrue(condition.test(mockContext));
    }

    @Test
    void areaTemperature_AboveBoundary_true() {
        areaTemperature.set(15);
        var condition = new LocationWarmthLootCondition(NumberRange.IntRange.atLeast(10));

        Assertions.assertTrue(condition.test(mockContext));
    }
}