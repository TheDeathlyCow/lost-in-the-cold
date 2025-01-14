package com.github.thedeathlycow.frostiful.mixins.item;

import com.github.thedeathlycow.frostiful.registry.FArmorTrimPatterns;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.item.equipment.trim.ArmorTrimPatterns;
import net.minecraft.registry.Registerable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorTrimPatterns.class)
public class ArmorTrimPatternsMixin {


    @Inject(
            method = "bootstrap",
            at = @At("TAIL")
    )
    private static void bootstrapCustomTrims(Registerable<ArmorTrimPattern> registry, CallbackInfo ci) {
        FArmorTrimPatterns.bootstrap(registry);
    }

}
