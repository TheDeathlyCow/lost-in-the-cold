package com.github.thedeathlycow.frostiful.client.render;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.client.model.FrostWandItemModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FrostWandItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, SimpleSynchronousResourceReloadListener {

    public static final Identifier ID = Frostiful.id("frost_wand_renderer");
    public static final Identifier INVENTORY_MODEL_ID = Frostiful.id("item/frost_wand_in_inventory");

    private final EntityModelLayer modelLayer;
    private FrostWandItemModel model;
    private ItemRenderer itemRenderer;
    private BakedModel inventoryModel;

    public FrostWandItemRenderer(EntityModelLayer modelLayer) {
        this.modelLayer = modelLayer;
    }

    /**
     * Code largely based on similar functionality in the mod
     * <a href="https://github.com/Ladysnake/Impaled/">'Impaled'</a>
     *
     * @param stack           the rendered item stack
     * @param mode            the model transformation mode
     * @param matrices        the matrix stack
     * @param vertexConsumers the vertex consumer provider
     * @param light           packed lightmap coordinates
     * @param overlay         the overlay UV passed to {@link VertexConsumer#overlay(int)}
     */
    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        boolean renderAsItem = mode == ModelTransformationMode.GUI
                || mode == ModelTransformationMode.GROUND
                || mode == ModelTransformationMode.FIXED;

        if (renderAsItem) {
            matrices.pop();
            matrices.push();
            itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, this.inventoryModel);
        } else {
            matrices.push();
            matrices.scale(0.6F, -0.6F, -0.6F);
            matrices.translate(0f, 1f, 0f);
            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(
                    vertexConsumers, this.model.getLayer(FrostWandItemModel.TEXTURE), false, stack.hasGlint()
            );

            this.model.render(matrices, vertexConsumer, light, overlay);
            matrices.pop();
        }
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        MinecraftClient client = MinecraftClient.getInstance();
        this.model = new FrostWandItemModel(client.getEntityModelLoader().getModelPart(this.modelLayer));
        this.itemRenderer = client.getItemRenderer();
        this.inventoryModel = client.getBakedModelManager().getModel(INVENTORY_MODEL_ID);
    }
}
