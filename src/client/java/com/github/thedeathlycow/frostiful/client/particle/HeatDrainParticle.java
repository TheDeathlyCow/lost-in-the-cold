package com.github.thedeathlycow.frostiful.client.particle;

import com.github.thedeathlycow.frostiful.particle.HeatDrainParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

@Environment(EnvType.CLIENT)
public class HeatDrainParticle extends AbstractSlowingParticle {

    protected HeatDrainParticle(ClientWorld clientWorld, double x, double y, double z, double vx, double vy, double vz, Vec3d destination) {
        super(clientWorld, x, y, z, vx, vy, vz);

        this.velocityX = destination.x - this.x;
        this.velocityY = destination.y - this.y;
        this.velocityZ = destination.z - this.z;

        final double slowFactor = 0.1;

        this.velocityX *= slowFactor * this.velocityMultiplier;
        this.velocityY *= slowFactor * this.velocityMultiplier;
        this.velocityZ *= slowFactor * this.velocityMultiplier;
        this.maxAge = this.random.nextBetween(1, 10);

        ThreadLocalRandom random = ThreadLocalRandom.current();
        this.scale = 0.5f * random.nextFloat();
        this.green *= random.nextFloat(0.5f, 1.0f);
    }

    @Override
    public int getBrightness(float tickDelta) {
        int brightness = super.getBrightness(tickDelta);
        int red = brightness >> 16 & 0xFF;
        return 0x0000F0 | red << 16;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<HeatDrainParticleEffect> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(HeatDrainParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            HeatDrainParticle heatDrainParticle = new HeatDrainParticle(world, x, y, z, velocityX, velocityY, velocityZ, parameters.destination());
            heatDrainParticle.setSprite(this.spriteProvider);
            return heatDrainParticle;
        }
    }
}
