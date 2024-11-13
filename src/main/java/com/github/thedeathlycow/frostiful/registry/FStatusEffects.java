package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.entity.effect.SimpleStatusEffect;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class FStatusEffects {


    public static final RegistryEntry<StatusEffect> WARMTH = registerReference(
            "warmth",
            new SimpleStatusEffect(
                    StatusEffectCategory.BENEFICIAL, 0xE3963E
            ).addAttributeModifier(
                    ThermooAttributes.FROST_RESISTANCE,
                    Frostiful.id("effect.warmth"),
                    1.0,
                    EntityAttributeModifier.Operation.ADD_VALUE
            )
    );

    public static final RegistryEntry<StatusEffect> FROST_BITE = registerReference(
            "frost_bite",
            new SimpleStatusEffect(
                    StatusEffectCategory.HARMFUL, 0x4287F5
            ).addAttributeModifier(
                    ThermooAttributes.FROST_RESISTANCE,
                    Frostiful.id("effect.frost_base"),
                    -1.0,
                    EntityAttributeModifier.Operation.ADD_VALUE
            )
    );

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful status effects");
    }

    private static RegistryEntry<StatusEffect> registerReference(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Frostiful.id(name), statusEffect);
    }

    private FStatusEffects() {
    }
}
