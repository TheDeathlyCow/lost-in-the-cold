package com.github.thedeathlycow.frostiful.entity;

import com.github.thedeathlycow.frostiful.particle.WindParticleEffect;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import com.github.thedeathlycow.frostiful.registry.tag.FEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class WindEntity extends Entity {

    private static final IntProvider LIFE_TICKS_PROVIDER = UniformIntProvider.create(80, 140);
    public static final Vec3d REGULAR_PUSH = new Vec3d(-0.16, 0, 0);
    public static final Vec3d ELYTRA_PUSH = new Vec3d(-1.75, 0, 0);

    public static final Predicate<Entity> CAN_BE_BLOWN = EntityPredicates.EXCEPT_SPECTATOR
            .and(EntityPredicates.VALID_ENTITY)
            .and(entity -> !entity.getType().isIn(FEntityTypeTags.HEAVY_ENTITY_TYPES));

    private float windSpeed = 1.0f;

    private int lifeTicks;

    private final int moveTickOffset;

    public WindEntity(EntityType<? extends WindEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
        this.setLifeTicks(LIFE_TICKS_PROVIDER.get(this.random));
        this.moveTickOffset = world.random.nextBetween(1, 10) - 1;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public void baseTick() {
        World world = getWorld();
        Profiler profiler = world.getProfiler();
        profiler.push("entityBaseTick");

        this.attemptTickInVoid();

        this.prevHorizontalSpeed = this.horizontalSpeed;
        this.prevPitch = this.getPitch();
        this.prevYaw = this.getYaw();

        this.firstUpdate = false;
        profiler.pop();
    }

    @Override
    public void tick() {
        this.baseTick();

        if (this.isRemoved()) {
            return;
        }

        World world = getWorld();
        var profiler = world.getProfiler();
        profiler.push("windTick");

        if (this.isOnFire()) {
            this.extinguish();
        }

        if (this.age % 10 == moveTickOffset) {

            Vec3d velocity;
            if (this.verticalCollision) {
                velocity = Vec3d.ZERO.add(0, 0, this.windSpeed * 0.5f);
            } else if (this.horizontalCollision) {
                velocity = Vec3d.ZERO.add(0, this.windSpeed * 0.5f, 0);
            } else {
                velocity = Vec3d.ZERO.add(-this.windSpeed, 0, 0);
            }

            this.setVelocity(velocity);
            this.move(MovementType.SELF, this.getVelocity());
        }


        if (!world.isClient) {
            if (this.age % 30 == 0) {
                this.playSound(FSoundEvents.ENTITY_WIND_BLOW, 0.75f, 0.9f + this.random.nextFloat() / 3);
            }

            if (this.age % 5 == 0) {
                profiler.push("windCollision");
                this.checkCollidingEntities();
                profiler.pop();
            }
        } else {

            WindParticleEffect particle = this.random.nextBoolean()
                    ? new WindParticleEffect(true)
                    : new WindParticleEffect(false);

            ParticleEffect dust = this.getDustParticle();

            for (int i = 0; i < 2; ++i) {
                world.addParticle(
                        particle,
                        this.getParticleX(0.5),
                        this.getRandomBodyY(),
                        this.getParticleZ(0.5),
                        -0.5, 0.0, 0.0
                );

                world.addParticle(
                        dust,
                        this.getParticleX(1.0),
                        this.getRandomBodyY(),
                        this.getParticleZ(1.0),
                        -0.1, 0.1, 0.0
                );
            }
        }

        this.lifeTicks--;
        if (this.lifeTicks <= 0) {
            this.dissipate();
        }

        profiler.pop();
    }

    public boolean startRiding(Entity entity, boolean force) {
        return false;
    }

    public boolean isFireImmune() {
        return true;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float speed) {
        this.windSpeed = speed;
    }

    public int getLifeTicks() {
        return this.lifeTicks;
    }

    public void setLifeTicks(int lifeTicks) {
        this.lifeTicks = lifeTicks;
    }

    protected void dissipate() {
        this.playSound(FSoundEvents.ENTITY_WIND_WOOSH, 1.0f, 1.0f);
        World world = getWorld();
        if (world.isClient) {
            ParticleEffect particle = this.getDustParticle();
            for (int i = 0; i < 20; ++i) {
                double vx = this.random.nextGaussian() * 0.02;
                double vy = this.random.nextGaussian() * 0.02;
                double vz = this.random.nextGaussian() * 0.02;
                world.addParticle(
                        particle,
                        this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0),
                        vx, vy, vz
                );
            }
        }
        this.discard();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    public void onEntityCollision(LivingEntity entity) {
        pushEntity(entity, getWorld(), this.getPos(), 1);
    }

    public static void pushEntity(LivingEntity entity, World world, Vec3d pos, double scale) {
        Vec3d push = entity.isFallFlying() ? ELYTRA_PUSH : REGULAR_PUSH;
        scale *= 1.0 - entity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);

        entity.addVelocity(push.x * scale, push.y * scale, push.z * scale);
        entity.velocityModified = true;
        if (!world.isClient && entity instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.networkHandler
                    .sendPacket(new PlaySoundS2CPacket(
                            RegistryEntry.of(FSoundEvents.ENTITY_WIND_HOWL),
                            SoundCategory.WEATHER,
                            pos.x, pos.y, pos.z,
                            1.0f, 1.0f,
                            world.getRandom().nextLong()
                    ));
        }
    }


    protected ParticleEffect getDustParticle() {
        return ParticleTypes.POOF;
    }

    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    private void checkCollidingEntities() {
        this.getWorld().getEntitiesByClass(
                        LivingEntity.class,
                        this.getBoundingBox(),
                        CAN_BE_BLOWN
                )
                .forEach(this::onEntityCollision);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("WindSpeed", NbtElement.FLOAT_TYPE)) {
            this.setWindSpeed(nbt.getFloat("WindSpeed"));
        }

        if (nbt.contains("LifeTicks", NbtElement.INT_TYPE)) {
            this.setLifeTicks(nbt.getInt("LifeTicks"));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("WindSpeed", this.getWindSpeed());

        if (this.isAlive()) {
            nbt.putInt("LifeTicks", this.getLifeTicks());
        }
    }
}
