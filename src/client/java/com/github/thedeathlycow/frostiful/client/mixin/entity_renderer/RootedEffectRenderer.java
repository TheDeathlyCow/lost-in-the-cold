package com.github.thedeathlycow.frostiful.client.mixin.entity_renderer;

import com.github.thedeathlycow.frostiful.entity.RootedEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
@Environment(EnvType.CLIENT)
public class RootedEffectRenderer<T extends LivingEntity, M extends EntityModel<T>> {

    private BlockRenderManager frostiful$blockRenderManager;
    private final BlockState frostiful$BLOCK_STATE = Blocks.ICE.getDefaultState();

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initAddon(EntityRendererFactory.Context ctx, M model, float shadowRadius, CallbackInfo ci) {
        this.frostiful$blockRenderManager = ctx.getBlockRenderManager();
    }


    @Inject(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "TAIL"
            )
    )
    private void renderIceOnEntity(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (livingEntity instanceof RootedEntity rootedEntity && rootedEntity.frostiful$isRooted()) {
            matrixStack.push();
            float blockSize = 1.75f;
            Box boundingBox = livingEntity.getBoundingBox();
            BlockPos blockPos = BlockPos.ofFloored(livingEntity.getX(), boundingBox.minY, livingEntity.getZ());
            matrixStack.scale(
                    blockSize * (float)boundingBox.getLengthX(),
                    blockSize * (float)boundingBox.getLengthY(),
                    blockSize * (float)boundingBox.getLengthZ()
            );
            matrixStack.translate(-0.5, -0.3, -0.5);
            this.frostiful$renderBlock(livingEntity, matrixStack, vertexConsumerProvider, blockPos);
            matrixStack.pop();
        }
    }

    private void frostiful$renderBlock(LivingEntity livingEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, BlockPos blockPos) {
        this.frostiful$blockRenderManager
                .getModelRenderer()
                .render(
                        livingEntity.getWorld(),
                        this.frostiful$blockRenderManager.getModel(frostiful$BLOCK_STATE),
                        frostiful$BLOCK_STATE,
                        blockPos,
                        matrixStack,
                        vertexConsumerProvider.getBuffer(
                                RenderLayers.getMovingBlockLayer(frostiful$BLOCK_STATE)
                        ),
                        false,
                        Random.create(),
                        frostiful$BLOCK_STATE.getRenderingSeed(blockPos),
                        OverlayTexture.DEFAULT_UV
                );
    }

}
