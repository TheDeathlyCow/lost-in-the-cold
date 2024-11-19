package com.github.thedeathlycow.frostiful.survival.wind;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.entity.FreezingWindEntity;
import com.github.thedeathlycow.frostiful.entity.WindEntity;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import com.github.thedeathlycow.frostiful.server.network.PointWindSpawnPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class PointWindSpawnStrategy implements WindSpawnStrategy {

    private static final int SIZE = 3;
    private static final int POWER_SCALE = 6;


    @Override
    public boolean spawn(World world, BlockPos spawnPos, boolean isInAir) {

        if (!(world instanceof ServerWorld serverWorld)) {
            return false;
        }
        Box box = new Box(spawnPos).expand(SIZE);
        Vec3d center = box.getCenter();
        PointWindSpawnPacket.sendToNearbyPlayersFromServer(serverWorld, spawnPos, center);

        for (BlockPos pos : BlockPos.iterateOutwards(spawnPos, SIZE / 2, SIZE / 2, SIZE / 2)) {

            WindManager.INSTANCE.extinguishBlock(
                    world.getBlockState(pos),
                    world,
                    pos,
                    () -> {
                        serverWorld.playSound(
                                null,
                                center.x, center.y, center.z,
                                FSoundEvents.ENTITY_FREEZING_WIND_BLOWOUT,
                                SoundCategory.AMBIENT,
                                0.75f,
                                0.9f + serverWorld.random.nextFloat() / 3
                        );
                    }
            );
        }

        world.getEntitiesByClass(LivingEntity.class, box, WindEntity.CAN_BE_BLOWN)
                .forEach(entity -> {
                    WindEntity.pushEntity(entity, world, center, POWER_SCALE);
                    FreezingWindEntity.freezeEntity(
                            entity,
                            Frostiful.getConfig().freezingConfig.getFreezingWindFrost() * POWER_SCALE
                    );
                });

        return true;
    }

    public static Vec3d randomParticlePos(Vec3d origin, Random random) {
        return new Vec3d(
                origin.x + SIZE * (2.0 * random.nextDouble() - 1.0),
                origin.y + SIZE * (2.0 * random.nextDouble() - 1.0),
                origin.z + SIZE * (2.0 * random.nextDouble() - 1.0)
        );
    }
}
