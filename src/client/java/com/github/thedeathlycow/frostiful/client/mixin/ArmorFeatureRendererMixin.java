package com.github.thedeathlycow.frostiful.client.mixin;

import com.github.thedeathlycow.frostiful.client.render.feature.CustomArmorTrimRenderer;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
    private CustomArmorTrimRenderer<A> frostifulTrimRenderer;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initCustomTrimAtlas(FeatureRendererContext<T, M> context, A innerModel, A outerModel, BakedModelManager bakery, CallbackInfo ci) {
        this.frostifulTrimRenderer = new CustomArmorTrimRenderer<>(bakery);
    }


    @WrapOperation(
            method = "renderArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderTrim(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/trim/ArmorTrim;Lnet/minecraft/client/render/entity/model/BipedEntityModel;Z)V"
            )
    )
    private void renderCustomTrim(
            ArmorFeatureRenderer<T, M, A> instance,
            RegistryEntry<ArmorMaterial> armorMaterial,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            ArmorTrim trim,
            A model,
            boolean leggings,
            Operation<Void> original
    ) {
        if (this.frostifulTrimRenderer != null) {
            this.frostifulTrimRenderer.renderCustomTrim(armorMaterial, matrices, vertexConsumers, light, trim, model, leggings);
        } else {
            original.call(instance, armorMaterial, matrices, vertexConsumers, light, trim, model, leggings);
        }
    }
}
