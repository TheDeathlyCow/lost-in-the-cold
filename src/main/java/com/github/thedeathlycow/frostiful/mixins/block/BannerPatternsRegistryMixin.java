package com.github.thedeathlycow.frostiful.mixins.block;

import com.github.thedeathlycow.frostiful.registry.FBannerPatterns;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BannerPatterns;
import net.minecraft.registry.Registerable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerPatterns.class)
public class BannerPatternsRegistryMixin {

    @Inject(
            method = "bootstrap",
            at = @At(
                    value = "TAIL",
                    shift = At.Shift.BEFORE
            )
    )
    private static void registerFrostifulBannerPatterns(
            Registerable<BannerPattern> registry, CallbackInfo ci
    ) {
        FBannerPatterns.bootstrap(registry);
    }

}
