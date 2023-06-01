package com.alex.mysticalagriculture.client.blockentity;

import com.alex.mysticalagriculture.blockentities.InfusionAltarBlockEntity;
import com.alex.mysticalagriculture.client.ModRenderTypes;
import com.alex.mysticalagriculture.init.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.Vec3f;

public class InfusionAltarRenderer implements BlockEntityRenderer<InfusionAltarBlockEntity> {

    public InfusionAltarRenderer(BlockEntityRendererFactory.Context context) { }

    @Override
    public void render(InfusionAltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var inventory = entity.getInventory();
        var minecraft = MinecraftClient.getInstance();
        var stack = inventory.getStack(1).isEmpty() ? inventory.getStack(0) : inventory.getStack(1);

        if (!stack.isEmpty()) {
            matrices.push();
            matrices.translate(0.5D, 1.1D, 0.5D);
            float scale = stack.getItem() instanceof BlockItem ?  0.95F : 0.75F;
            matrices.scale(scale, scale, scale);
            double tick = System.currentTimeMillis() / 800.0D;
            matrices.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) ((tick * 40.0D) % 360)));
            minecraft.getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
            matrices.pop();
        }

        var pos = entity.getPos();
        var world = entity.getWorld();
        var builder = vertexConsumers.getBuffer(ModRenderTypes.GHOST);

        matrices.push();
        matrices.translate(-pos.getX(), -pos.getY(), -pos.getZ());

        entity.getPedestalPositions().forEach(aoePos -> {
            if (world != null && world.isAir(aoePos)) {
                matrices.push();
                matrices.translate(aoePos.getX(), aoePos.getY(), aoePos.getZ());
                minecraft.getBlockRenderManager().renderBlock(Blocks.INFUSION_PEDESTAL.getDefaultState(), aoePos, world, matrices, builder, false, world.getRandom());
                matrices.pop();
            }
        });

        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(InfusionAltarBlockEntity blockEntity) {
        return true;
    }
}
