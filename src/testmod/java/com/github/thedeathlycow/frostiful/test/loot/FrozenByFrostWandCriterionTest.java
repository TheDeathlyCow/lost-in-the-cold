package com.github.thedeathlycow.frostiful.test.loot;

import com.github.thedeathlycow.frostiful.entity.advancement.FrozenByFrostWandCriterion;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class FrozenByFrostWandCriterionTest {

    private static final int NUM_PREDICATES = 3;

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void three_creeper_mobs_to_empty_predicates_is_true(TestContext context) {
        List<LootContext> creepers = createLootContexts(
                context,
                EntityType.CREEPER,
                EntityType.CREEPER,
                EntityType.CREEPER
        );

        FrozenByFrostWandCriterion.Conditions conditions = new FrozenByFrostWandCriterion.Conditions(
                Optional.empty(),
                List.of(),
                NumberRange.IntRange.ANY
        );

        context.assertTrue(conditions.matches(creepers), "Conditions do not match!");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void three_creeper_mobs_to_three_creeper_predicates_is_true(TestContext context) {
        List<LootContext> creepers = createLootContexts(
                context,
                EntityType.CREEPER,
                EntityType.CREEPER,
                EntityType.CREEPER
        );

        FrozenByFrostWandCriterion.Conditions conditions = createConditions();

        context.assertTrue(conditions.matches(creepers), "Conditions do not match!");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void five_creeper_mobs_to_three_creeper_predicates_is_true(TestContext context) {
        List<LootContext> creepers = createLootContexts(
                context,
                EntityType.CREEPER,
                EntityType.CREEPER,
                EntityType.CREEPER,
                EntityType.CREEPER,
                EntityType.CREEPER
        );

        FrozenByFrostWandCriterion.Conditions conditions = createConditions();

        context.assertTrue(conditions.matches(creepers), "Conditions do not match!");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void two_creeper_mobs_to_three_creeper_predicates_is_false(TestContext context) {
        List<LootContext> creepers = createLootContexts(
                context,
                EntityType.CREEPER,
                EntityType.CREEPER
        );

        FrozenByFrostWandCriterion.Conditions conditions = createConditions();

        context.assertFalse(conditions.matches(creepers), "Conditions do match, but they should NOT!");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void two_creepers_and_one_zombie_to_three_creeper_predicates_is_false(TestContext context) {
        List<LootContext> creepers = createLootContexts(
                context,
                EntityType.CREEPER,
                EntityType.CREEPER,
                EntityType.ZOMBIE
        );

        FrozenByFrostWandCriterion.Conditions conditions = createConditions();

        context.assertFalse(conditions.matches(creepers), "Conditions do match, but they should NOT!");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void zero_mobs_to_three_creeper_predicates_is_false(TestContext context) {
        List<LootContext> creepers = List.of();

        FrozenByFrostWandCriterion.Conditions conditions = createConditions();

        context.assertFalse(conditions.matches(creepers), "Conditions do match, but they should NOT!");
        context.complete();
    }

    @SafeVarargs
    private static List<LootContext> createLootContexts(
            TestContext testContext,
            EntityType<? extends MobEntity>... entityTypes
    ) {
        ServerPlayerEntity mockPlayer = createMockPlayer(testContext);
        List<LootContext> contexts = new ArrayList<>();

        for (EntityType<? extends MobEntity> type : entityTypes) {
            LootContext context = EntityPredicate.createAdvancementEntityLootContext(
                    mockPlayer,
                    testContext.spawnMob(type, BlockPos.ORIGIN)
            );
            contexts.add(context);
        }

        return contexts;
    }

    private static ServerPlayerEntity createMockPlayer(TestContext context) {
        ServerPlayerEntity mockPlayer = Mockito.mock(ServerPlayerEntity.class);

        Mockito.when(mockPlayer.getServerWorld())
                .thenReturn(context.getWorld());
        Mockito.when(mockPlayer.getPos())
                .thenReturn(Vec3d.ZERO);

        return mockPlayer;
    }

    private static FrozenByFrostWandCriterion.Conditions createConditions() {
        List<LootContextPredicate> predicates = new ArrayList<>();

        for (int i = 0; i < NUM_PREDICATES; i++) {
            LootContextPredicate context = EntityPredicate.contextPredicateFromEntityPredicate(
                    EntityPredicate.Builder.create()
                            .type(EntityType.CREEPER)
            );
            predicates.add(context);
        }

        return new FrozenByFrostWandCriterion.Conditions(
                Optional.empty(),
                predicates,
                NumberRange.IntRange.ANY
        );
    }
}