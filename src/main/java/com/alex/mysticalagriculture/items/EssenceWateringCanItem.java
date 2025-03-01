package com.alex.mysticalagriculture.items;

import com.alex.cucumber.helper.NBTHelper;
import com.alex.mysticalagriculture.lib.ModTooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.UUID;

public class EssenceWateringCanItem extends WateringCanItem {
    private final ChatFormatting textColor;

    public EssenceWateringCanItem(int range, double chance, ChatFormatting textColor) {
        super(range, chance);
        this.textColor = textColor;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (selected && NBTHelper.getBoolean(stack, "Active") && entity instanceof Player player) {
            var result = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);

            if (result.getType() != HitResult.Type.MISS) {
                this.doWater(stack, world, player, result.getBlockPos(), result.getDirection());
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return NBTHelper.getBoolean(stack, "Active");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        var trace = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (trace.getType() != HitResult.Type.BLOCK) {
            if (NBTHelper.getBoolean(stack, "Water") && player.isCrouching()) {
                NBTHelper.flipBoolean(stack, "Active");
            }

            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }

        if (NBTHelper.getBoolean(stack, "Water")) {
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }

        var pos = trace.getBlockPos();
        var direction = trace.getDirection();

        if (level.mayInteract(player, pos) && player.mayUseItemAt(pos.relative(direction), direction, stack)) {
            var fluid = level.getFluidState(pos);

            if (fluid.is(FluidTags.WATER)) {
                NBTHelper.setString(stack, "ID", UUID.randomUUID().toString());
                NBTHelper.setBoolean(stack, "Water", true);

                player.playSound(SoundEvents.BUCKET_FILL, 1.0F, 1.0F);

                return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
            }
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var player = context.getPlayer();

        if (player == null)
            return InteractionResult.PASS;

        if (NBTHelper.getBoolean(this.getDefaultInstance(), "Active"))
            return InteractionResult.PASS;

        return super.useOn(context);
    }
    /*@Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var player = context.getPlayer();

        if (player == null)
            return InteractionResult.PASS;

        if (NBTHelper.getBoolean(stack, "Active"))
            return InteractionResult.PASS;

        return super.onItemUseFirst(stack, context);
    }*/

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag advanced) {
        super.appendHoverText(stack, world, tooltip, advanced);

        var rangeString = String.valueOf(this.range);
        var rangeNumber = Component.literal(rangeString + "x" + rangeString).withStyle(this.textColor);

        tooltip.add(ModTooltips.TOOL_AREA.args(rangeNumber).build());
    }
}
