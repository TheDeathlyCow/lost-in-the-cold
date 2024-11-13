package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.particle.HeatDrainParticleEffect;
import com.github.thedeathlycow.frostiful.particle.WindParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class FParticleTypes {

    public static final ParticleType<HeatDrainParticleEffect> HEAT_DRAIN = FabricParticleTypes.complex(
            HeatDrainParticleEffect.CODEC,
            HeatDrainParticleEffect.PACKET_CODEC
    );
    public static final ParticleType<WindParticleEffect> WIND = FabricParticleTypes.complex(
            WindParticleEffect.CODEC,
            WindParticleEffect.PACKET_CODEC
    );
    public static final ParticleType<WindParticleEffect> WIND_FLIPPED = FabricParticleTypes.complex(
            WindParticleEffect.CODEC,
            WindParticleEffect.PACKET_CODEC
    );

    public static void registerParticleTypes() {
        Frostiful.LOGGER.debug("Initialized Frostiful particle types");
        register("heat_drain", HEAT_DRAIN);
        register("wind", WIND);
        register("wind_flipped", WIND_FLIPPED);
    }

    private static void register(String name, ParticleType<?> particle) {
        Registry.register(Registries.PARTICLE_TYPE, Frostiful.id(name), particle);
    }

    private FParticleTypes() {
    }

}
