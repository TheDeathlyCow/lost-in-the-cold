package com.github.thedeathlycow.frostiful.client.mixin;

import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Calendar;

@Mixin(SplashTextResourceSupplier.class)
public class SplashTextResourceSupplierMixin {

    private static final SplashTextRenderer frostiful_DOWNLOAD_MUSESWIPR = new SplashTextRenderer("Download MuseSwipr on Steam!");

    @Inject(
            method = "get",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getDownloadMuseSwiprSplash(CallbackInfoReturnable<SplashTextRenderer> cir) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DATE) == 21) {
            cir.setReturnValue(frostiful_DOWNLOAD_MUSESWIPR);
        }
    }

}
