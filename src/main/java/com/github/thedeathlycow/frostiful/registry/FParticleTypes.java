package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.particle.HeatDrainParticleEffect;
import com.github.thedeathlycow.frostiful.particle.WindParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class FParticleTypes {

    public static final ParticleType<HeatDrainParticleEffect> HEAT_DRAIN = register(
            "heat_drain",
            FabricParticleTypes.complex(
                    HeatDrainParticleEffect.CODEC,
                    HeatDrainParticleEffect.PACKET_CODEC
            )
    );
    public static final ParticleType<WindParticleEffect> WIND = register(
            "wind",
            FabricParticleTypes.complex(
                    WindParticleEffect.CODEC,
                    WindParticleEffect.PACKET_CODEC
            )
    );
    public static final ParticleType<WindParticleEffect> WIND_FLIPPED = register(
            "wind_flipped",
            FabricParticleTypes.complex(
                    WindParticleEffect.CODEC,
                    WindParticleEffect.PACKET_CODEC
            )
    );

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful particle types");
    }

    private static <T extends ParticleEffect> ParticleType<T> register(String name, ParticleType<T> particle) {
        return Registry.register(Registries.PARTICLE_TYPE, Frostiful.id(name), particle);
    }

    private FParticleTypes() {
    }

}
