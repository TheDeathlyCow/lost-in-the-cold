package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.entity.component.BrushableComponent;
import com.github.thedeathlycow.frostiful.entity.component.LivingEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.WolfEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class FComponents implements EntityComponentInitializer {

    public static final ComponentKey<LivingEntityComponents> ENTITY_COMPONENTS = ComponentRegistry.getOrCreate(
            Frostiful.id("living_entity"),
            LivingEntityComponents.class
    );

    public static final ComponentKey<BrushableComponent> BRUSHABLE_COMPONENT = ComponentRegistry.getOrCreate(
            Frostiful.id("brushable"),
            BrushableComponent.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(
                LivingEntity.class,
                ENTITY_COMPONENTS,
                LivingEntityComponents::new
        );
        registry.registerFor(
                PolarBearEntity.class,
                BRUSHABLE_COMPONENT,
                BrushableComponent::new
        );
        registry.registerFor(
                OcelotEntity.class,
                BRUSHABLE_COMPONENT,
                BrushableComponent::new
        );
        registry.registerFor(
                WolfEntity.class,
                BRUSHABLE_COMPONENT,
                BrushableComponent::new
        );
    }
}
