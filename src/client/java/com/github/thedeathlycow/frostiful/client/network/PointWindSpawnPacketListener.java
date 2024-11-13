package com.github.thedeathlycow.frostiful.client.network;

import com.github.thedeathlycow.frostiful.particle.WindParticleEffect;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import com.github.thedeathlycow.frostiful.server.network.PointWindSpawnPacket;
import com.github.thedeathlycow.frostiful.survival.wind.PointWindSpawnStrategy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class PointWindSpawnPacketListener implements ClientPlayNetworking.PlayPayloadHandler<PointWindSpawnPacket> {

    private static final int PARTICLE_COUNT = 50;

    private static final ParticleEffect[] WIND_PARTICLES = new ParticleEffect[]{
            new WindParticleEffect(true),
            new WindParticleEffect(false),
            ParticleTypes.SNOWFLAKE,
            ParticleTypes.SNOWFLAKE,
            ParticleTypes.SNOWFLAKE
    };

    @Override
    public void receive(PointWindSpawnPacket payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            displayWind(context.client().world, payload.position());
        });
    }

    private static void displayWind(ClientWorld world, Vec3d pos) {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            for (ParticleEffect particleEffect : WIND_PARTICLES) {
                addParticle(particleEffect, world, pos);
            }
        }

        world.playSound(
                pos.x, pos.y, pos.z,
                FSoundEvents.ENTITY_WIND_BLOW,
                SoundCategory.AMBIENT,
                0.75f,
                0.9f + world.random.nextFloat() / 3,
                true
        );
    }

    private static void addParticle(ParticleEffect particleEffect, ClientWorld world, Vec3d origin) {
        double vx = world.random.nextGaussian() * 0.02;
        double vy = world.random.nextGaussian() * 0.02;
        double vz = world.random.nextGaussian() * 0.02;
        Vec3d rPos = PointWindSpawnStrategy.randomParticlePos(origin, world.random);
        world.addParticle(
                particleEffect,
                rPos.x, rPos.y, rPos.z,
                vx, vy, vz
        );
    }


}
