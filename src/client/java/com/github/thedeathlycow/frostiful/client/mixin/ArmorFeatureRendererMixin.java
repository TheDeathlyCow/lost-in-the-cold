package com.github.thedeathlycow.frostiful.client.mixin;

import com.github.thedeathlycow.frostiful.client.render.feature.CustomArmorTrimRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {

    @Unique
    @Nullable
    private CustomArmorTrimRenderer<A> frostiful$customRenderer;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initCustomTrimAtlas(FeatureRendererContext<T, M> context, A innerModel, A outerModel, BakedModelManager bakery, CallbackInfo ci) {
        this.frostiful$customRenderer = new CustomArmorTrimRenderer<>(bakery);
    }


    @Inject(
            method = "renderTrim",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderCustomTrim(
            RegistryEntry<ArmorMaterial> armorMaterial,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            ArmorTrim trim,
            A model,
            boolean leggings,
            CallbackInfo ci
    ) {
        boolean rendered = this.frostiful$customRenderer != null
                && this.frostiful$customRenderer.renderCustomTrim(armorMaterial, matrices, vertexConsumers, light, trim, model, leggings);
        if (rendered) {
            ci.cancel();
        }
    }
}
