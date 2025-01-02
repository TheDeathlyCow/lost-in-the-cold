package com.github.thedeathlycow.frostiful.test.effects;

import com.github.thedeathlycow.frostiful.entity.component.FrostWandRootComponent;
import com.github.thedeathlycow.frostiful.registry.FComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class RootedTests {

    @GameTest(templateName = "frostiful-test:effects.platform")
    public void villager_stops_walking_when_rooted(TestContext context) {
        BlockPos start = new BlockPos(1, 2, 1);
        BlockPos end = start.add(2, 0, 2);

        MobEntity entity = context.spawnMob(EntityType.VILLAGER, start);
        FrostWandRootComponent rootComponent = FComponents.FROST_WAND_ROOT_COMPONENT.get(entity);
        rootComponent.tryRootFromFrostWand(null);

        context.startMovingTowards(entity, end, 1.0f);
        context.expectEntityAtEnd(EntityType.VILLAGER, start);
    }

    @GameTest(templateName = "frostiful-test:effects.platform")
    public void villager_can_walk_when_not_rooted(TestContext context) {
        BlockPos start = new BlockPos(1, 2, 1);
        BlockPos end = start.add(2, 0, 2);

        MobEntity entity = context.spawnMob(EntityType.VILLAGER, start);

        context.startMovingTowards(entity, end, 1.0f);
        context.expectEntityAtEnd(EntityType.VILLAGER, end);
    }

    @GameTest(templateName = "frostiful-test:effects.platform")
    public void villager_root_is_not_reset(TestContext context) {
        BlockPos start = new BlockPos(1, 2, 1);

        MobEntity entity = context.spawnMob(EntityType.VILLAGER, start);
        FrostWandRootComponent rootComponent = FComponents.FROST_WAND_ROOT_COMPONENT.get(entity);

        // initial root
        rootComponent.tryRootFromFrostWand(null);

        int initialRootTicks = rootComponent.getRootedTicks();
        context.assertTrue(rootComponent.isRooted(), "Villager is not rooted");

        context.waitAndRun(
                10L,
                () -> {
                    context.assertTrue(rootComponent.isRooted(), "Villager is not rooted for re-apply");
                    // root again, before root is expired
                    rootComponent.tryRootFromFrostWand(null);

                    int newRootTicks = rootComponent.getRootedTicks();
                    context.assertFalse(
                            newRootTicks >= initialRootTicks,
                            "Villager root ticks were not reset"
                    );

                    context.complete();
                }
        );
    }
}
