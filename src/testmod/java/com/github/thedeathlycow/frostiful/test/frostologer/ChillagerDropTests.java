package com.github.thedeathlycow.frostiful.test.frostologer;

import com.github.thedeathlycow.frostiful.entity.ChillagerEntity;
import com.github.thedeathlycow.frostiful.registry.FEntityTypes;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class ChillagerDropTests {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void regular_chillager_does_not_drop_ominous_bottle(TestContext context) {
        World world = context.getWorld();
        DamageSources damageSources = world.getDamageSources();

        ChillagerEntity chillager = context.spawnEntity(FEntityTypes.CHILLAGER, BlockPos.ORIGIN);

        chillager.damage(damageSources.genericKill(), Float.MAX_VALUE);

        context.dontExpectItemAt(Items.OMINOUS_BOTTLE, BlockPos.ORIGIN, 3f);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void chillager_captain_drops_ominous_bottle(TestContext context) {
        World world = context.getWorld();
        DamageSources damageSources = world.getDamageSources();

        ChillagerEntity chillager = context.spawnEntity(FEntityTypes.CHILLAGER, BlockPos.ORIGIN);
        chillager.setPatrolLeader(true);
        chillager.equipStack(
                EquipmentSlot.HEAD,
                Raid.getOminousBanner(chillager.getRegistryManager().getWrapperOrThrow(RegistryKeys.BANNER_PATTERN))
        );

        context.assertTrue(chillager.isCaptain(), "Chillager is not a captain!");

        chillager.damage(damageSources.genericKill(), Float.MAX_VALUE);

        context.expectItemAt(Items.OMINOUS_BOTTLE, BlockPos.ORIGIN, 3f);

        context.complete();
    }
}