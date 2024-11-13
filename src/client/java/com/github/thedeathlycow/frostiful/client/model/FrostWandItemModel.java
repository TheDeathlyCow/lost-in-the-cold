package com.github.thedeathlycow.frostiful.client.model;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
 * Made with Block Bench
 */
@Environment(EnvType.CLIENT)
public class FrostWandItemModel extends Model {
    public static final Identifier TEXTURE = Frostiful.id("textures/entity/frost_wand.png");

    private static final int FULL_BRIGHTNESS = (15 << 4) | (15 << 20); // packed lightmap coordinates are (block << 4) | (sky << 20)

    private final ModelPart root;

    public FrostWandItemModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData rod = root.addChild("rod", ModelPartBuilder.create().uv(32, 9).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(20, 8).cuboid(-2.0F, -5.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -33.0F, -1.0F, 2.0F, 28.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData top = root.addChild("top", ModelPartBuilder.create().uv(8, 0).cuboid(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -37.0F, 0.0F));

        ModelPartData icicles = top.addChild("icicles", ModelPartBuilder.create().uv(20, 25).cuboid(-3.0F, 1.0F, -3.0F, 0.0F, 5.0F, 6.0F, new Dilation(0.0F))
                .uv(20, 20).cuboid(3.0F, 1.0F, -3.0F, 0.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData south_r1 = icicles.addChild("south_r1", ModelPartBuilder.create().uv(20, 10).cuboid(-3.0F, 1.0F, -3.0F, 0.0F, 5.0F, 6.0F, new Dilation(0.0F))
                .uv(20, 15).cuboid(3.0F, 1.0F, -3.0F, 0.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData icicle = root.addChild("icicle", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -26.0F, 0.0F));

        ModelPartData main = icicle.addChild("main", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

        ModelPartData cube_r1 = main.addChild("cube_r1", ModelPartBuilder.create().uv(8, 8).cuboid(-3.0F, -26.0F, 0.0F, 6.0F, 17.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube_r2 = main.addChild("cube_r2", ModelPartBuilder.create().uv(8, 8).cuboid(-3.0F, -26.0F, 0.0F, 6.0F, 17.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData sides = icicle.addChild("sides", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData west_r1 = sides.addChild("west_r1", ModelPartBuilder.create().uv(32, 16).cuboid(-3.0F, -6.0F, 0.0F, 6.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -16.0F, 0.0F, -0.1745F, 1.5708F, 0.0F));

        ModelPartData east_r1 = sides.addChild("east_r1", ModelPartBuilder.create().uv(32, 16).cuboid(-3.0F, -6.0F, 0.0F, 6.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, -16.0F, 0.0F, -0.1745F, -1.5708F, 0.0F));

        ModelPartData south_r2 = sides.addChild("south_r2", ModelPartBuilder.create().uv(32, 16).cuboid(-3.0F, -6.0F, 0.0F, 6.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.0F, 4.0F, -0.1745F, 0.0F, 0.0F));

        ModelPartData north_r1 = sides.addChild("north_r1", ModelPartBuilder.create().uv(32, 16).cuboid(-3.0F, -6.0F, 0.0F, 6.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.0F, -4.0F, 0.1745F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.root.render(matrices, vertices, FULL_BRIGHTNESS, overlay, color);
    }
}
