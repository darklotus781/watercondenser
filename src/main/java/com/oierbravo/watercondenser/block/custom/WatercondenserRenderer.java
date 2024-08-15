package com.oierbravo.watercondenser.block.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.oierbravo.watercondenser.entity.ModBlockEntities;
import com.oierbravo.watercondenser.entity.WatercondenserBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


/**
 *  Code adapted from https://github.com/EwyBoy/ITank/blob/1.18.2/src/main/java/com/ewyboy/itank/client/TankRenderer.java
 *
 */
public class WatercondenserRenderer implements BlockEntityRenderer<WatercondenserBlockEntity> {

    public WatercondenserRenderer(BlockEntityRendererProvider.Context context) {
    }
    public static void register() {
        BlockEntityRenderers.register(ModBlockEntities.WATERCONDENSER_ENTITY.get(), WatercondenserRenderer::new);
    }
    @Override
    public void render(@NotNull WatercondenserBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        FluidStack fluidStack = pBlockEntity.getFluidHandler().getFluidInTank(0);
        if (!fluidStack.isEmpty()) {
            int amount = fluidStack.getAmount();
            int total = pBlockEntity.getFluidHandler().getTankCapacity(0);
            this.renderFluidInTank(pBlockEntity.getLevel(), pBlockEntity.getBlockPos(), fluidStack, pPoseStack, pBufferSource, (amount / (float) total), pPackedLight, pPackedOverlay);
        }
    }

    private void renderFluidInTank(BlockAndTintGetter world, BlockPos pos, FluidStack fluidStack, PoseStack matrix, MultiBufferSource buffer, float percent, int pPackedLight, int pPackedOverlay) {
        matrix.pushPose();
        matrix.translate(0.5d, 0.29d, 0.5d);

        Matrix4f matrix4f = matrix.last().pose();
        Matrix3f matrix3f = matrix.last().normal();

        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluid);


        TextureAtlasSprite fluidTexture = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(clientFluid.getStillTexture(fluidStack));

        int color = clientFluid.getTintColor(fluidStack);

        VertexConsumer builder = buffer.getBuffer(RenderType.translucent());
        this.renderTopFluidFace(matrix, fluidTexture, matrix4f, matrix3f, builder, color, percent, pPackedLight, pPackedOverlay);
        matrix.popPose();

    }

    private void renderTopFluidFace(PoseStack matrix, TextureAtlasSprite sprite, Matrix4f matrix4f, Matrix3f normalMatrix, VertexConsumer builder, int color, float percent, int pPackedLight, int pPackedOverlay) {
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = ((color) & 0xFF) / 255f;
        float a = ((color >> 24) & 0xFF) / 255f;

        float width = 12 / 16f;
        float height = 7 / 16f;

        float minU = sprite.getU(3F / 16F);
        float maxU = sprite.getU(13F / 16F);
        float minV = sprite.getV(3F / 16F);
        float maxV = sprite.getV(13F / 16F);

        float pY = -height / 2 + percent * height;

        builder.addVertex(matrix4f, -width / 2, pY , -width / 2).setColor(r, g, b, a)
                .setUv(minU, minV)
                .setLight(pPackedLight)
                .setOverlay(pPackedOverlay)
                .setNormal(0, 1, 0);

        builder.addVertex(matrix4f, -width / 2, pY, width / 2).setColor(r, g, b, a)
                .setUv(minU, maxV)
                .setLight(pPackedLight)
                .setOverlay(pPackedOverlay)
                .setNormal(0, 1, 0);

        builder.addVertex(matrix4f, width / 2, pY, width / 2).setColor(r, g, b, a)
                .setUv(maxU, maxV)
                .setLight(pPackedLight)
                .setOverlay(pPackedOverlay)
                .setNormal(0, 1, 0);

        builder.addVertex(matrix4f, width / 2, pY, -width / 2).setColor(r, g, b, a)
                .setUv(maxU, minV)
                .setLight(pPackedLight)
                .setOverlay(pPackedOverlay)
                .setNormal(0, 1, 0);
    }

}
