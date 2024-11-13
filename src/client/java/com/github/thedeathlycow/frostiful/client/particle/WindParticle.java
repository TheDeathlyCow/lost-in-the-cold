package com.github.thedeathlycow.frostiful.client.particle;

import com.github.thedeathlycow.frostiful.particle.WindParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class WindParticle extends SpriteBillboardParticle {

    private static final Vector3f FROM = Util.make(new Vector3f(0.5F, 0.5F, 0.5F), Vector3f::normalize);
    private static final Vector3f TO = new Vector3f(-1.0F, -1.0F, 0.0F);

    private final SpriteProvider spriteProvider;

    private static final Quaternionf FRONT_ROTATION = new Quaternionf().rotationX(-MathHelper.PI);
    private static final Quaternionf BACK_ROTATION = new Quaternionf().rotationYXZ(-MathHelper.PI, MathHelper.PI, 0.0f);

    protected WindParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z);
        this.spriteProvider = spriteProvider;
        this.velocityX *= 2;
        this.scale *= 3;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        this.buildGeometry(vertexConsumer, camera, tickDelta, true, (quaternion) -> {
            quaternion.mul(FRONT_ROTATION);
        });
        this.buildGeometry(vertexConsumer, camera, tickDelta, false, (quaternion) -> {
            quaternion.mul(BACK_ROTATION);
        });
    }

    private void buildGeometry(
            VertexConsumer vertexConsumer,
            Camera camera,
            float tickDelta,
            boolean flip,
            Consumer<Quaternionf> rotator
    ) {
        Vec3d cameraPos = camera.getPos();
        float dx = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.getX());
        float dy = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.getY());
        float dz = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.getZ());
        var quaternion = new Quaternionf().setAngleAxis(0.0f, FROM.x(), FROM.y(), FROM.z());
        rotator.accept(quaternion);
        TO.rotate(quaternion);
        var points = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };

        float size = this.getSize(tickDelta) * (flip ? -1 : 1);

        for (int i = 0; i < 4; ++i) {
            Vector3f point = points[i];
            point.rotate(quaternion);
            point.mul(size);
            point.add(dx, dy, dz);
        }

        int brightness = this.getBrightness(tickDelta);
        this.vertex(vertexConsumer, points[0], this.getMaxU(), this.getMaxV(), brightness);
        this.vertex(vertexConsumer, points[1], this.getMaxU(), this.getMinV(), brightness);
        this.vertex(vertexConsumer, points[2], this.getMinU(), this.getMinV(), brightness);
        this.vertex(vertexConsumer, points[3], this.getMinU(), this.getMaxV(), brightness);
    }

    private void vertex(VertexConsumer vertexConsumer, Vector3f pos, float u, float v, int light) {
        vertexConsumer.vertex(pos.x(), pos.y(), pos.z())
                .texture(u, v)
                .color(this.red, this.green, this.blue, this.alpha)
                .light(light);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<WindParticleEffect> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(WindParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new WindParticle(world, x, y, z, this.spriteProvider);
        }
    }
}
