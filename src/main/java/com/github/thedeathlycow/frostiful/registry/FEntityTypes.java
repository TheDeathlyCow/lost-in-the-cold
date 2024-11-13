package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.entity.*;
import com.github.thedeathlycow.frostiful.sound.FSoundEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class FEntityTypes {

    public static final EntityType<FrostologerEntity> FROSTOLOGER = EntityType.Builder.create(
                    FrostologerEntity::new,
                    SpawnGroup.MONSTER
            )
            .dimensions(0.6f, 1.95f)
            .maxTrackingRange(4)
            .build();

    public static final EntityType<BiterEntity> BITER = EntityType.Builder.create(
                    BiterEntity::new,
                    SpawnGroup.MONSTER
            )
            .dimensions(1.0f, 2.0f)
            .maxTrackingRange(8)
            .build();

    public static final EntityType<ChillagerEntity> CHILLAGER = EntityType.Builder.create(
                    ChillagerEntity::new,
                    SpawnGroup.MONSTER
            )
            .dimensions(0.6f, 1.95f)
            .maxTrackingRange(8)
            .build();

    public static final EntityType<GlacialArrowEntity> GLACIAL_ARROW = EntityType.Builder.<GlacialArrowEntity>create(
                    GlacialArrowEntity::new,
                    SpawnGroup.CREATURE
            )
            .dimensions(0.5f, 0.5f)
            .build();

    public static final EntityType<FrostSpellEntity> FROST_SPELL = EntityType.Builder.<FrostSpellEntity>create(
                    FrostSpellEntity::new,
                    SpawnGroup.MISC
            )
            .dimensions(3f / 8f, 3f / 8f)
            .maxTrackingRange(8)
            .trackingTickInterval(10)
            .build();

    public static final EntityType<PackedSnowballEntity> PACKED_SNOWBALL = EntityType.Builder.<PackedSnowballEntity>create(
                    PackedSnowballEntity::new,
                    SpawnGroup.MISC
            )
            .dimensions(3f / 8f, 3f / 8f)
            .maxTrackingRange(8)
            .trackingTickInterval(10)
            .build();

    public static final EntityType<ThrownIcicleEntity> THROWN_ICICLE = EntityType.Builder.<ThrownIcicleEntity>create(
                    ThrownIcicleEntity::new,
                    SpawnGroup.MISC
            )
            .dimensions(0.25f, 0.25f)
            .maxTrackingRange(8)
            .trackingTickInterval(10)
            .build();

    public static final EntityType<FreezingWindEntity> FREEZING_WIND = EntityType.Builder.create(
                    FreezingWindEntity::new,
                    SpawnGroup.AMBIENT
            )
            .dimensions(2.0f, 2.0f)
            .maxTrackingRange(8)
            .trackingTickInterval(10)
            .build();

    public static void registerEntities() {
        Frostiful.LOGGER.debug("Initialized Frostiful entity types");
        register("frostologer", FROSTOLOGER);
        register("chillager", CHILLAGER);
        register("biter", BITER);
        register("glacial_arrow", GLACIAL_ARROW);
        register("frost_spell", FROST_SPELL);
        register("packed_snowball", PACKED_SNOWBALL);
        register("thrown_icicle", THROWN_ICICLE);
        register("freezing_wind", FREEZING_WIND);

        FabricDefaultAttributeRegistry.register(FROSTOLOGER, FrostologerEntity.createFrostologerAttributes());
        FabricDefaultAttributeRegistry.register(CHILLAGER, ChillagerEntity.createChillagerAttributes());
        FabricDefaultAttributeRegistry.register(BITER, BiterEntity.createBiterAttributes());

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(
                ((world, entity, killedEntity) -> {
                    if (entity.getType() == BITER) {
                        entity.playSound(FSoundEvents.ENTITY_BITER_BURP, 1.0f, 1.0f);
                    }
                })
        );
    }

    private static <T extends Entity> void register(String id, EntityType<T> type) {
        Registry.register(Registries.ENTITY_TYPE, Frostiful.id(id), type);
    }
}
