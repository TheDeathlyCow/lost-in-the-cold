package com.github.thedeathlycow.frostiful.block;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.FrostifulConfig;
import com.github.thedeathlycow.frostiful.registry.FStatusEffects;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class CampfireUseEventListener implements UseBlockCallback {

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {

        if (player.isSpectator()) {
            return ActionResult.PASS;
        }

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (state.isIn(BlockTags.CAMPFIRES) && Boolean.TRUE.equals(state.get(CampfireBlock.LIT))) {
            ItemStack stack = player.getStackInHand(hand);
            stack = player.isCreative() ? stack.copy() : stack;

            if (!stack.isEmpty() && stack.isIn(ItemTags.LOGS_THAT_BURN)) {
                if (!world.isClient) {
                    warmNearbyEntities(world, pos);
                    addSmokeParticles(world, pos);
                    player.incrementStat(Stats.INTERACT_WITH_CAMPFIRE);
                    Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity) player, pos, stack);
                    stack.decrement(1);
                }
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    private static void addSmokeParticles(World world, BlockPos pos) {
        if (world instanceof ServerWorld serverWorld) {

            Vec3d position = pos.toCenterPos().add(0, -0.3, 0);

            serverWorld.spawnParticles(
                    ParticleTypes.SMOKE,
                    position.x, position.y, position.z,
                    15,
                    0.5, 0.7, 0.5,
                    1e-3
            );
            serverWorld.spawnParticles(
                    ParticleTypes.LAVA,
                    position.x, position.y, position.z,
                    8,
                    0.5, 0.7, 0.5,
                    1e-2
            );

        }
    }

    private static void warmNearbyEntities(World world, BlockPos pos) {
        FrostifulConfig config = Frostiful.getConfig();
        final double boxLength = config.freezingConfig.getCampfireWarmthSearchRadius();
        final int duration = config.freezingConfig.getCampfireWarmthTime();

        // get all nearby living entities that do not have warmth or
        // who have a weak warmth effect
        List<LivingEntity> nearbyEntities = world.getEntitiesByClass(
                LivingEntity.class,
                Box.of(Vec3d.ofCenter(pos), boxLength, boxLength, boxLength),
                (entity) -> {
                    StatusEffectInstance instance = entity.getStatusEffect(FStatusEffects.WARMTH);
                    return !entity.isSpectator() && (instance == null || (instance.getAmplifier() == 0 && instance.getDuration() < duration));
                }
        );

        // apply warmth effect to all nearby entities
        for (LivingEntity entity : nearbyEntities) {
            StatusEffectInstance instance = new StatusEffectInstance(FStatusEffects.WARMTH, duration, 0, true, true);
            entity.addStatusEffect(instance);
        }

        world.playSound(null, pos, FSoundEvents.CAMPFIRE_HISS, SoundCategory.BLOCKS, 0.5F, 1.5f);
    }
}
