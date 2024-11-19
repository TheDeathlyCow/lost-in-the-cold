package com.github.thedeathlycow.frostiful.item.enchantment;

import com.github.thedeathlycow.frostiful.particle.HeatDrainParticleEffect;
import com.github.thedeathlycow.frostiful.util.FMathHelper;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Function;

public record HeatDrainEnchantmentEffect(
        EnchantmentLevelBasedValue heatToDrain,
        float efficiency,
        boolean drainFromEnchanted
) implements EnchantmentEntityEffect {

    public static final MapCodec<HeatDrainEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EnchantmentLevelBasedValue.CODEC
                            .fieldOf("heat_to_drain")
                            .forGetter(HeatDrainEnchantmentEffect::heatToDrain),
                    rangedFloat(0.0f, 1f, v -> "Value must be between 0 and 1 (inclusive): " + v)
                            .fieldOf("efficiency")
                            .forGetter(HeatDrainEnchantmentEffect::efficiency),
                    Codec.BOOL
                            .fieldOf("drain_from_enchanted")
                            .orElse(false)
                            .forGetter(HeatDrainEnchantmentEffect::drainFromEnchanted)
            ).apply(instance, HeatDrainEnchantmentEffect::new)
    );

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        LivingEntity owner = context.owner();
        if (owner != null && user instanceof LivingEntity livingVictim) {
            if (drainFromEnchanted) {
                // as in frozen touch curse
                this.drainHeat(owner, livingVictim, level);
            } else {
                // as in enervation
                this.drainHeat(livingVictim, owner, level);
            }
        }
    }

    private void drainHeat(LivingEntity source, LivingEntity destination, int level) {

        if (!source.thermoo$canFreeze()) {
            return;
        }

        int heatDrainedFromTarget = MathHelper.floor(this.heatToDrain.getValue(level));
        source.thermoo$addTemperature(-heatDrainedFromTarget, HeatingModes.ACTIVE);

        int heatAddedToOwner = MathHelper.floor(heatDrainedFromTarget * this.efficiency);
        destination.thermoo$addTemperature(heatAddedToOwner, HeatingModes.ACTIVE);

        if (destination.thermoo$isWarm()) {
            destination.thermoo$setTemperature(0);
        }

        if (heatDrainedFromTarget != 0) {
            addHeatDrainParticles(source, destination, level);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }

    public static void addHeatDrainParticles(LivingEntity source, LivingEntity destination, int level) {
        World world = destination.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            addHeatDrainParticles(serverWorld, source, destination, level, 0.5);
        }
    }

    public static void addHeatDrainParticles(
            ServerWorld serverWorld,
            LivingEntity source, LivingEntity destination,
            int level, double delta
    ) {
        Vec3d from = FMathHelper.getMidPoint(source.getEyePos(), source.getPos());
        final int numParticles = (level * 3) + 15;

        double fromX = from.getX();
        double fromY = from.getY();
        double fromZ = from.getZ();
        var effect = new HeatDrainParticleEffect(destination.getEyePos());
        serverWorld.spawnParticles(effect, fromX, fromY, fromZ, numParticles, delta, delta, delta, 0.3);
    }

    private static Codec<Float> rangedFloat(float min, float max, Function<Float, String> messageFactory) {
        return Codec.FLOAT
                .validate(
                        value -> value.compareTo(min) >= 0 && value.compareTo(max) <= 0
                                ? DataResult.success(value)
                                : DataResult.error(() -> messageFactory.apply(value))
                );
    }
}
