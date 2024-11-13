package com.github.thedeathlycow.frostiful.client.render.feature;

import com.github.thedeathlycow.frostiful.client.registry.FTexturedRenderLayers;
import com.github.thedeathlycow.frostiful.registry.tag.FTrimTags;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class CustomArmorTrimRenderer<A extends BipedEntityModel<? extends LivingEntity>> {

    private final SpriteAtlasTexture atlas;

    public CustomArmorTrimRenderer(BakedModelManager bakery) {
        this.atlas = bakery.getAtlas(FTexturedRenderLayers.ARMOR_TRIMS_ATLAS_TEXTURE);
    }

    public boolean renderCustomTrim(
            RegistryEntry<ArmorMaterial> armorMaterial,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            ArmorTrim trim,
            A model,
            boolean leggings
    ) {
        if (!trim.getPattern().isIn(FTrimTags.CUSTOM_PATTERNS)) {
            return false;
        }

        Identifier id = leggings
                ? trim.getLeggingsModelId(armorMaterial)
                : trim.getGenericModelId(armorMaterial);

        Sprite sprite = this.atlas.getSprite(id);

        VertexConsumer vertexConsumer = sprite.getTextureSpecificVertexConsumer(
                vertexConsumers.getBuffer(FTexturedRenderLayers.ARMOR_TRIMS_RENDER_LAYER)
        );
        model.render(
                matrices,
                vertexConsumer,
                light,
                OverlayTexture.DEFAULT_UV
        );

        return true;
    }


}
