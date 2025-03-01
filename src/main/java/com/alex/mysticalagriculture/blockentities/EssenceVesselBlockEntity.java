package com.alex.mysticalagriculture.blockentities;

import com.alex.cucumber.blockentity.BaseInventoryBlockEntity;
import com.alex.cucumber.inventory.BaseItemStackHandler;
import com.alex.mysticalagriculture.api.crop.ICropProvider;
import com.alex.mysticalagriculture.init.ModBlockEntities;
import com.alex.mysticalagriculture.util.EssenceVesselType;
import com.alex.mysticalagriculture.util.RecipeIngredientCache;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class EssenceVesselBlockEntity extends BaseInventoryBlockEntity {
    private static final int MAX_STACK_SIZE = 40;
    private final BaseItemStackHandler inventory;

    public EssenceVesselBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ESSENCE_VESSEL, pos, state);
        this.inventory = BaseItemStackHandler.create(1, this::markDirtyAndDispatch, handler -> {
            handler.setDefaultSlotLimit(MAX_STACK_SIZE);
            handler.setCanInsert((slot, stack) -> canInsertStack(stack));
        });
    }

    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    private static boolean canInsertStack(ItemStack stack) {
        return RecipeIngredientCache.INSTANCE.isValidVesselItem(stack);
    }
}

