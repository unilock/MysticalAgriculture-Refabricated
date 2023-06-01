package com.alex.mysticalagriculture.client.blockentity;

import com.alex.mysticalagriculture.blockentities.AwakeningPedestalBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.Vec3f;

public class AwakeningPedestalRenderer implements BlockEntityRenderer<AwakeningPedestalBlockEntity>  {
    public AwakeningPedestalRenderer(BlockEntityRendererFactory.Context context) { }

    @Override
    public void render(AwakeningPedestalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var stack = entity.getInventory().getStack(0);

        if (!stack.isEmpty()) {
            matrices.push();
            matrices.translate(0.5D, 1.2D, 0.5D);
            float scale = stack.getItem() instanceof BlockItem ?  0.95F : 0.75F;
            matrices.scale(scale, scale, scale);
            double tick = System.currentTimeMillis() / 800.0D;
            matrices.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) ((tick * 40.0D) % 360)));
            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
            matrices.pop();
        }
    }
}
