package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class FPotions {

    public static final RegistryEntry<Potion> FREEZING = registerReference(
            "freezing",
            create("freezing", new StatusEffectInstance(FStatusEffects.FROST_BITE, 180 * 20, 0))
    );
    public static final RegistryEntry<Potion> FREEZING_LONG = registerReference(
            "long_freezing",
            create("freezing", new StatusEffectInstance(FStatusEffects.FROST_BITE, 2 * 180 * 20, 0))
    );
    public static final RegistryEntry<Potion> FREEZING_STRONG = registerReference(
            "strong_freezing",
            create("freezing", new StatusEffectInstance(FStatusEffects.FROST_BITE, 90 * 20, 1))
    );

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful potions");
        FabricBrewingRecipeRegistryBuilder.BUILD.register(
                builder -> {
                    builder.registerPotionRecipe(Potions.AWKWARD, FItems.FROZEN_ROD, FPotions.FREEZING);
                    builder.registerPotionRecipe(FPotions.FREEZING, Items.REDSTONE, FPotions.FREEZING_LONG);
                    builder.registerPotionRecipe(FPotions.FREEZING, Items.GLOWSTONE_DUST, FPotions.FREEZING_STRONG);
                }
        );
    }

    private static Potion create(String name, StatusEffectInstance... effects) {
        return new Potion(String.format("%s.%s", Frostiful.MODID, name), effects);
    }

    private static RegistryEntry<Potion> registerReference(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Frostiful.id(name), potion);
    }

    private FPotions() {

    }

}
